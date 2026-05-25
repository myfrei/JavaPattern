package com.patterns.demo.patterns.creational.factorymethod;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

/**
 * Гоняет один сценарий «отправить уведомление по трём каналам» через
 * правильный Factory Method и через монолитный {@code switch}, собирая
 * пошаговый отчёт для пиксельной визуализации на фронте.
 */
@Service
public class FactoryMethodDemoService {

    private static final String MSG = "Заказ #42 оплачен";

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Factory Method — подкласс решает, какой продукт создать");
        r.setDescription(
            "Клиент перебирает фабрики и для каждой вызывает фабричный метод create(). " +
            "Он не знает конкретных классов продуктов — только интерфейс Notifier. " +
            "Новый канал = новая фабрика-подкласс, клиент не меняется.");
        r.setCode(GOOD_CODE);

        long t = 0;
        for (GoodFactory.NotifierFactory f : GoodFactory.factories()) {
            GoodFactory.Notifier product = f.create();
            String creator = f.getClass().getSimpleName();
            r.getInstances().add(new PatternDemoResponse.InstanceInfo(product.name(), creator));
            r.getSteps().add(new PatternDemoResponse.Step(
                t++, product.name(), creator + ".create()", "→ new " + product.name(), true));
            r.getSteps().add(new PatternDemoResponse.Step(
                t++, product.name(), "notify()", product.send(MSG), true));
        }

        r.setVerdict("PASS — 3 продукта через фабричный метод");
        r.setExplanation(
            "Каждая фабрика-подкласс переопределяет только create(). Клиент работает с " +
            "абстракциями Creator/Notifier, поэтому добавление канала не трогает существующий код.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Factory Method — антипаттерн: монолитный switch");
        r.setDescription(
            "Тот же сценарий, но один метод send(channel, msg) знает про все каналы и " +
            "создаёт объекты через switch. Работает, но не расширяется без правки метода.");
        r.setCode(BAD_CODE);

        r.getInstances().add(new PatternDemoResponse.InstanceInfo(
            "NotificationService", "switch(channel)"));

        long t = 0;
        for (String ch : new String[]{"EMAIL", "SMS", "PUSH"}) {
            String out = BadFactory.send(ch, MSG);
            r.getSteps().add(new PatternDemoResponse.Step(
                t++, "NotificationService", "switch → case " + ch, out, true));
        }

        r.setVerdict("FAIL — один switch знает все каналы");
        r.setExplanation(
            "Вся логика выбора продукта зашита в switch. Новый канал = править этот метод; " +
            "связанность высокая, ветки легко сломать, изолированно тестировать нечего.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Factory Method");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            interface Notifier { String send(String msg); }

            abstract class NotifierFactory {
                abstract Notifier create();            // фабричный метод
                String notify(String msg) {            // логика на абстракции
                    return create().send(msg);
                }
            }

            class EmailFactory extends NotifierFactory {
                Notifier create() { return new EmailNotifier(); }
            }
            // SmsFactory, PushFactory — аналогично

            for (NotifierFactory f : List.of(new EmailFactory(),
                                             new SmsFactory(),
                                             new PushFactory())) {
                f.notify("Заказ #42 оплачен");          // клиент не знает классов продуктов
            }
            """;

    private static final String BAD_CODE = """
            // Один метод знает про все каналы и создаёт объекты сам
            class NotificationService {
                String send(String channel, String msg) {
                    switch (channel) {
                        case "EMAIL": return new EmailNotifier().send(msg);
                        case "SMS":   return new SmsNotifier().send(msg);
                        case "PUSH":  return new PushNotifier().send(msg);
                        default: throw new IllegalArgumentException(channel);
                    }
                    // новый канал? правим switch и рискуем сломать ветки
                }
            }
            """;
}
