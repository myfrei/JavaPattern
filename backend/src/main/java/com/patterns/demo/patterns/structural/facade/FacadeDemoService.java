package com.patterns.demo.patterns.structural.facade;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

/**
 * Оформляет заказ SKU-42 на $25.99: одним вызовом фасада (good) и ручной
 * оркестрацией подсистем на стороне клиента (bad). Отдаёт отчёт для визуализации
 * «фасад прячет подсистемы».
 */
@Service
public class FacadeDemoService {

    private static final String SKU = "SKU-42";
    private static final int CENTS = 2599;

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Facade — один вход вместо трёх подсистем");
        r.setDescription(
            "Клиент зовёт OrderFacade.placeOrder(). Фасад сам резервирует товар, проводит оплату и " +
            "оформляет доставку в правильном порядке. Клиент не знает ни подсистем, ни их последовательности.");
        r.setCode(GOOD_CODE);

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("OrderFacade", "facade"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Inventory", "subsystem"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Payment", "subsystem"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Shipping", "subsystem"));

        String out = new GoodFacade.OrderFacade().placeOrder(SKU, CENTS);

        long t = 0;
        r.getSteps().add(new PatternDemoResponse.Step(t++, "OrderFacade", "placeOrder(SKU-42)", "единый вход", true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "Inventory", "reserve()", "зарезервирован", true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "Payment", "charge(2599)", "оплачено", true));
        r.getSteps().add(new PatternDemoResponse.Step(t, "Shipping", "ship()", out, true));

        r.setVerdict("PASS — заказ оформлен одним вызовом: " + out);
        r.setExplanation(
            "Фасад инкапсулирует подсистемы и порядок их вызова. Клиент развязан со складом, оплатой и " +
            "доставкой — менять их внутренности можно, не трогая клиентский код.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Facade — антипаттерн: клиент сам оркеструет подсистемы");
        r.setDescription(
            "Без фасада клиент создаёт Inventory, Payment, Shipping и вызывает их в правильном порядке " +
            "сам. Он связан с тремя подсистемами, и последовательность — его ответственность.");
        r.setCode(BAD_CODE);

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Inventory", "клиент знает"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Payment", "клиент знает"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Shipping", "клиент знает"));

        String out = BadFacade.checkout(SKU, CENTS);

        long t = 0;
        r.getSteps().add(new PatternDemoResponse.Step(t++, "Inventory", "client → reserve()", "шаг 1 (помнит клиент)", false));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "Payment", "client → charge()", "шаг 2 (помнит клиент)", false));
        r.getSteps().add(new PatternDemoResponse.Step(t, "Shipping", "client → ship()", out + " · шаг 3", false));

        r.setVerdict("FAIL — клиент связан с 3 подсистемами и их порядком");
        r.setExplanation(
            "Знание о подсистемах и последовательности размазано по клиенту: легко перепутать порядок " +
            "(отгрузить до оплаты) и тяжело менять подсистемы. Facade убирает связанность за один вход.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Facade");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            class OrderFacade {                       // единый вход
                private final Inventory inventory = new Inventory();
                private final Payment payment = new Payment();
                private final Shipping shipping = new Shipping();

                String placeOrder(String sku, int cents) {
                    if (!inventory.reserve(sku)) return "out of stock";
                    payment.charge(cents);
                    return shipping.ship(sku);
                }
            }

            facade.placeOrder("SKU-42", 2599);        // клиент не знает подсистем
            """;

    private static final String BAD_CODE = """
            // Клиент сам создаёт подсистемы и помнит порядок вызовов
            var inv = new Inventory();
            var pay = new Payment();
            var ship = new Shipping();

            inv.reserve(sku);     // шаг 1
            pay.charge(cents);    // шаг 2  — перепутаешь порядок, отгрузишь без оплаты
            ship.ship(sku);       // шаг 3
            """;
}
