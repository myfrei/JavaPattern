package com.patterns.demo.patterns.microservices.communication.publishsubscribe;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Рассылает событие OrderPlaced трём получателям: через брокер с независимым
 * fan-out (good) и через синхронную цепочку с каскадным сбоем (bad). Отдаёт отчёт
 * для визуализации «брокер → подписчики».
 */
@Service
public class PublishSubscribeDemoService {

    private static final String[] SUBS = {"EmailConsumer", "AnalyticsConsumer", "InventoryConsumer"};

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Publish-Subscribe — брокер и независимый fan-out");
        r.setDescription(
            "Издатель публикует OrderPlaced в брокер, тот доставляет событие всем подписчикам независимо. " +
            "Издатель не знает получателей; сбой одного не мешает остальным.");
        r.setCode(GOOD_CODE);

        GoodPubSub.Broker broker = new GoodPubSub.Broker();
        long t = 0;
        for (String s : SUBS) {
            broker.subscribe(s);
            r.getInstances().add(new PatternDemoResponse.InstanceInfo(s, "subscriber"));
            r.getSteps().add(new PatternDemoResponse.Step(t++, s, "subscribe()", "подписан", true));
        }

        r.getSteps().add(new PatternDemoResponse.Step(t++, "Broker", "publish(OrderPlaced)", "fan-out " + broker.subscriberCount(), true));
        for (String s : broker.publish("OrderPlaced")) {
            r.getSteps().add(new PatternDemoResponse.Step(t++, s, "deliver", "получил OrderPlaced", true));
        }

        r.setVerdict("PASS — событие доставлено " + broker.subscriberCount() + " подписчикам независимо");
        r.setExplanation(
            "Брокер развязывает издателя и подписчиков: каждый получает событие независимо, добавление " +
            "подписчика не трогает издателя, а сбой одного не рушит доставку остальным.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Publish-Subscribe — антипаттерн: синхронная цепочка");
        r.setDescription(
            "Издатель сам по очереди синхронно вызывает получателей. AnalyticsConsumer падает — и " +
            "InventoryConsumer уже не получает событие: сбой одного каскадом блокирует остальных.");
        r.setCode(BAD_CODE);

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("EmailConsumer", "ok"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("AnalyticsConsumer", "fail"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("InventoryConsumer", "skipped"));

        List<String> trace = new BadPubSub.Publisher().publish("OrderPlaced");
        long t = 0;
        r.getSteps().add(new PatternDemoResponse.Step(t++, "EmailConsumer", "call (sync)", trace.get(0), true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "AnalyticsConsumer", "call (sync)", trace.get(1), false));
        r.getSteps().add(new PatternDemoResponse.Step(t, "InventoryConsumer", "call (sync)", "не вызван — цепочка оборвана ✗", false));

        r.setVerdict("FAIL — синхронная цепочка: сбой Analytics блокирует Inventory");
        r.setExplanation(
            "Прямые синхронные вызовы связывают издателя с получателями и их порядком: сбой одного рвёт " +
            "доставку дальше. Publish-Subscribe разносит это через брокер и независимый fan-out.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Publish-Subscribe");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            class Broker {
                private final List<String> subscribers = new ArrayList<>();
                void subscribe(String s) { subscribers.add(s); }
                List<String> publish(String event) {
                    return new ArrayList<>(subscribers);   // независимый fan-out
                }
            }

            broker.subscribe("EmailConsumer");
            broker.subscribe("AnalyticsConsumer");
            broker.publish("OrderPlaced");   // все получают независимо
            """;

    private static final String BAD_CODE = """
            // Издатель синхронно вызывает получателей по очереди
            emailConsumer.handle(event);       // ok
            analyticsConsumer.handle(event);   // FAIL → исключение
            inventoryConsumer.handle(event);   // не вызван — цепочка оборвана
            """;
}
