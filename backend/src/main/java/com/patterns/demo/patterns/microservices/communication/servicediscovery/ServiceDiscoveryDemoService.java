package com.patterns.demo.patterns.microservices.communication.servicediscovery;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

/**
 * Резолвит order-service с двумя инстансами и переживает гибель одного из них:
 * через реестр (good) и через хардкод адреса (bad). Отдаёт отчёт для визуализации
 * «реестр + живые инстансы».
 */
@Service
public class ServiceDiscoveryDemoService {

    private static final String A = "10.0.0.1:8080";
    private static final String B = "10.0.0.2:8080";

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Service Discovery — резолв через реестр");
        r.setDescription(
            "Два инстанса order-service регистрируются в реестре. Клиент резолвит сервис по имени " +
            "(round-robin). Когда один инстанс снимается, резолв прозрачно уходит на оставшийся.");
        r.setCode(GOOD_CODE);

        r.getInstances().add(new PatternDemoResponse.InstanceInfo(A, "instance"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo(B, "instance"));

        GoodServiceDiscovery.Registry reg = new GoodServiceDiscovery.Registry();
        long t = 0;
        reg.register("order-service", A);
        r.getSteps().add(new PatternDemoResponse.Step(t++, A, "register order-service", "instance up", true));
        reg.register("order-service", B);
        r.getSteps().add(new PatternDemoResponse.Step(t++, B, "register order-service", "instance up", true));

        String r1 = reg.resolve("order-service");
        r.getSteps().add(new PatternDemoResponse.Step(t++, r1, "resolve()", "→ " + r1, true));
        String r2 = reg.resolve("order-service");
        r.getSteps().add(new PatternDemoResponse.Step(t++, r2, "resolve()", "→ " + r2, true));

        reg.deregister("order-service", A);
        r.getSteps().add(new PatternDemoResponse.Step(t++, A, "deregister", "instance down", true));
        String r3 = reg.resolve("order-service");
        r.getSteps().add(new PatternDemoResponse.Step(t, r3, "resolve()", "→ " + r3 + " (failover прозрачен)", true));

        r.setVerdict("PASS — клиент резолвит через реестр, переезд инстанса прозрачен");
        r.setExplanation(
            "Адреса инстансов живут в реестре, а не в клиенте. Подъём, гибель и масштабирование инстансов " +
            "не требуют правок клиента — он всегда получает актуальный живой адрес.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Service Discovery — антипаттерн: хардкод host:port");
        r.setDescription(
            "Клиент жёстко зашил адрес " + BadServiceDiscovery.HARDCODED + ". Пока инстанс там — всё ок, но " +
            "после пересоздания (новый адрес) вызовы по старому адресу падают.");
        r.setCode(BAD_CODE);

        r.getInstances().add(new PatternDemoResponse.InstanceInfo(BadServiceDiscovery.HARDCODED, "hardcoded"));

        String live = BadServiceDiscovery.HARDCODED;
        long t = 0;
        boolean ok1 = BadServiceDiscovery.callSucceeds(BadServiceDiscovery.HARDCODED, live);
        r.getSteps().add(new PatternDemoResponse.Step(t++, BadServiceDiscovery.HARDCODED, "call (hardcoded)", ok1 ? "ok" : "FAIL", ok1));

        live = "10.0.0.7:8080"; // инстанс пересоздан с новым адресом
        r.getSteps().add(new PatternDemoResponse.Step(t++, "scheduler", "instance rescheduled", "→ " + live, false));

        boolean ok2 = BadServiceDiscovery.callSucceeds(BadServiceDiscovery.HARDCODED, live);
        r.getSteps().add(new PatternDemoResponse.Step(t, BadServiceDiscovery.HARDCODED, "call (hardcoded)", ok2 ? "ok" : "FAIL: no route to host", ok2));

        r.setVerdict("FAIL — хардкод host:port: переезд инстанса ломает клиента");
        r.setExplanation(
            "Адрес зашит в клиент, поэтому любое изменение расположения инстанса рвёт связь. Service Discovery " +
            "убирает привязку, отдавая живые адреса из реестра по имени сервиса.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Service Discovery");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            class Registry {
                void register(String service, String instance) { ... }
                String resolve(String service) {                 // round-robin живых
                    List<String> in = pool.get(service);
                    return in.get(rr++ % in.size());
                }
            }

            reg.register("order-service", "10.0.0.1:8080");
            reg.register("order-service", "10.0.0.2:8080");
            String addr = reg.resolve("order-service");          // живой адрес
            """;

    private static final String BAD_CODE = """
            // Клиент хардкодит адрес сервиса
            String addr = "10.0.0.1:8080";   // зашито в код
            call(addr);                       // ок, пока инстанс там
            // инстанс пересоздан → 10.0.0.7:8080
            call(addr);                       // FAIL: no route to host
            """;
}
