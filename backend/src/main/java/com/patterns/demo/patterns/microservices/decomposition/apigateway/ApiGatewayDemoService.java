package com.patterns.demo.patterns.microservices.decomposition.apigateway;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Собирает «дашборд» из User/Orders/Inventory: одним запросом через шлюз (good)
 * и тремя прямыми запросами клиента (bad). Отдаёт отчёт для визуализации
 * «клиент → gateway → fan-out».
 */
@Service
public class ApiGatewayDemoService {

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("API Gateway — единая точка входа и агрегация");
        r.setDescription(
            "Клиент делает один запрос к шлюзу. Шлюз сам обращается к Users, Orders и Inventory и " +
            "склеивает ответ. Клиент не знает топологию бэкенда и делает 1 round-trip вместо трёх.");
        r.setCode(GOOD_CODE);

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Client", "client"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Gateway", "gateway"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("UserService", "service"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("OrderService", "service"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("InventoryService", "service"));

        GoodApiGateway.Dashboard dash = new GoodApiGateway.Gateway().getDashboard("u1");

        long t = 0;
        r.getSteps().add(new PatternDemoResponse.Step(t++, "Gateway", "Client → GET /dashboard", "1 round-trip клиента", true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "UserService", "Gateway → users.get", dash.user(), true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "OrderService", "Gateway → orders.list", dash.orders().toString(), true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "InventoryService", "Gateway → inventory.stock", "stock=" + dash.stock(), true));
        r.getSteps().add(new PatternDemoResponse.Step(t, "Gateway", "→ Client", "агрегированный ответ", true));

        r.setVerdict("PASS — 1 запрос клиента, агрегация на шлюзе");
        r.setExplanation(
            "Шлюз инкапсулирует топологию и склейку: клиент развязан с числом и адресами сервисов, " +
            "а сетевых round-trip от клиента становится один вместо трёх.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("API Gateway — антипаттерн: болтливый клиент");
        r.setDescription(
            "Без шлюза клиент сам ходит в каждый сервис. На один экран — три сетевых round-trip, и клиент " +
            "жёстко знает адреса и состав всех сервисов.");
        r.setCode(BAD_CODE);

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Client", "client"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("UserService", "service"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("OrderService", "service"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("InventoryService", "service"));

        List<String> roundTrips = BadApiGateway.clientFetchesEverything("u1");
        String[] hits = {"UserService", "OrderService", "InventoryService"};
        long t = 0;
        for (int i = 0; i < roundTrips.size(); i++) {
            r.getSteps().add(new PatternDemoResponse.Step(t++, hits[i], "Client → " + hits[i], roundTrips.get(i), false));
        }

        r.setVerdict("FAIL — " + roundTrips.size() + " round-trip клиента, связан со всеми сервисами");
        r.setExplanation(
            "Клиент знает каждый сервис и платит сетевым round-trip за каждый. Меняется топология — правится " +
            "клиент. API Gateway убирает это, став единым фасадом и точкой агрегации.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("API Gateway");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            class Gateway {                                   // единая точка входа
                Dashboard getDashboard(String userId) {
                    return new Dashboard(
                        users.get(userId),                    // шлюз сам ходит
                        orders.list(userId),                  // в нужные сервисы
                        inventory.stock("SKU-1"));            // и агрегирует
                }
            }

            // клиент:
            GET /dashboard?user=u1                            // 1 round-trip
            """;

    private static final String BAD_CODE = """
            // Болтливый клиент сам ходит в каждый сервис
            GET user-service/users/u1                         // round-trip 1
            GET order-service/orders?user=u1                  // round-trip 2
            GET inventory-service/stock/SKU-1                 // round-trip 3
            // клиент знает адреса всех сервисов и платит за каждый запрос
            """;
}
