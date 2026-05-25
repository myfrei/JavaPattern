package com.patterns.demo.patterns.microservices.data.outbox;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OutboxTest {

    // ─── good: атомарная запись + relay публикует ───
    @Test
    void orderAndOutboxAreAtomicThenRelayed() {
        GoodOutbox.Db db = new GoodOutbox.Db();
        db.saveOrder("Order#1", "OrderCreated");
        assertThat(db.rows()).containsExactly("Order#1");
        assertThat(db.outbox()).containsExactly("OrderCreated"); // в той же транзакции

        GoodOutbox.Relay relay = new GoodOutbox.Relay();
        relay.poll(db);
        assertThat(relay.published()).containsExactly("OrderCreated");
        assertThat(db.outbox()).isEmpty(); // отправленное помечено
    }

    // ─── bad: dual write теряет сообщение при падении ───
    @Test
    void dualWriteLosesMessageOnCrash() {
        BadOutbox.Db db = new BadOutbox.Db();
        BadOutbox.Broker broker = new BadOutbox.Broker();
        db.save("Order#1");
        // краш до broker.publish(...)
        assertThat(db.rows()).containsExactly("Order#1"); // заказ есть
        assertThat(broker.published()).isEmpty();          // сообщение потеряно
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunPublishesReliably() {
        PatternDemoResponse r = new OutboxDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(3);
        assertThat(r.getVerdict()).startsWith("PASS");
    }

    @Test
    void badRunLosesMessage() {
        PatternDemoResponse r = new OutboxDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getVerdict()).startsWith("FAIL");
        assertThat(r.getSteps()).anyMatch(s -> !s.isOk());
    }
}
