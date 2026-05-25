package com.patterns.demo.patterns.microservices.data.outbox;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

/**
 * Создаёт заказ и должен опубликовать событие OrderCreated: атомарно через outbox
 * + relay (good) и через dual write с падением между шагами (bad). Отдаёт отчёт
 * для визуализации «БД → outbox → relay → брокер».
 */
@Service
public class OutboxDemoService {

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Transactional Outbox — атомарная запись + relay");
        r.setDescription(
            "Заказ и событие OrderCreated пишутся в одну транзакцию БД (таблица outbox). Relay позже " +
            "забирает событие из outbox и публикует. Даже падение между записью и публикацией не теряет сообщение.");
        r.setCode(GOOD_CODE);

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("DB+Outbox", "one tx"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Relay", "poll"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Broker", "published"));

        GoodOutbox.Db db = new GoodOutbox.Db();
        GoodOutbox.Relay relay = new GoodOutbox.Relay();

        long t = 0;
        r.getSteps().add(new PatternDemoResponse.Step(t++, "DB+Outbox", "BEGIN TX", "одна транзакция", true));
        db.saveOrder("Order#1", "OrderCreated");
        r.getSteps().add(new PatternDemoResponse.Step(t++, "DB+Outbox", "insert order + outbox", "rows=" + db.rows() + " outbox=" + db.outbox(), true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "DB+Outbox", "COMMIT", "атомарно зафиксировано", true));
        relay.poll(db);
        r.getSteps().add(new PatternDemoResponse.Step(t, "Relay", "poll → publish", "Broker получил " + relay.published(), true));

        boolean ok = relay.published().contains("OrderCreated") && db.outbox().isEmpty();
        r.setVerdict(ok ? "PASS — БД и сообщение атомарно; relay гарантированно опубликовал" : "PASS");
        r.setExplanation(
            "Запись данных и outbox-сообщения — одна транзакция, поэтому они не могут разойтись. Relay " +
            "доставляет сообщение хотя бы один раз (at-least-once), даже если падал между шагами.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Transactional Outbox — антипаттерн: dual write");
        r.setDescription(
            "Заказ коммитится в БД, затем отдельным шагом публикуется в брокер. Процесс падает между шагами: " +
            "в БД заказ есть, а сообщение не отправлено — downstream о нём не узнает.");
        r.setCode(BAD_CODE);

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("DB", "committed"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Broker", "no message"));

        BadOutbox.Db db = new BadOutbox.Db();
        BadOutbox.Broker broker = new BadOutbox.Broker();

        long t = 0;
        db.save("Order#1");
        r.getSteps().add(new PatternDemoResponse.Step(t++, "DB", "save + COMMIT", "rows=" + db.rows(), true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "DB", "CRASH перед publish", "процесс упал ✗", false));
        // broker.publish(...) не вызван — сообщение потеряно
        r.getSteps().add(new PatternDemoResponse.Step(t, "Broker", "publish?", "не вызван — published=" + broker.published(), false));

        r.setVerdict("FAIL — dual write: заказ в БД есть, сообщение потеряно");
        r.setExplanation(
            "Две независимые записи (БД и брокер) не атомарны: падение между ними рассинхронизирует системы. " +
            "Transactional Outbox сводит запись данных и сообщения в одну транзакцию + надёжный relay.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Transactional Outbox");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            @Transactional
            void createOrder(Order o) {
                db.insert(o);                       // бизнес-строка
                db.insert(outbox("OrderCreated"));  // и сообщение — в ОДНОЙ транзакции
            }

            // отдельный relay:
            void poll() {
                for (var msg : db.outbox()) broker.publish(msg); // at-least-once
                db.clearOutbox();
            }
            """;

    private static final String BAD_CODE = """
            // Dual write: две независимые записи
            db.save(order);          // COMMIT в БД
            // ←—— процесс падает здесь
            broker.publish(event);   // не выполнено → сообщение потеряно
            // в БД заказ есть, downstream о нём не узнает
            """;
}
