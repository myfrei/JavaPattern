package com.patterns.demo.patterns.microservices.data.eventsourcing;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventSourcingTest {

    // ─── good: свёртка лога + replay в прошлое ───
    @Test
    void foldsEventsAndReplaysHistory() {
        GoodEventSourcing.Account acc = new GoodEventSourcing.Account();
        acc.apply("deposit 100");
        acc.apply("withdraw 30");
        acc.apply("deposit 50");

        assertThat(acc.balance()).isEqualTo(120);
        assertThat(acc.balanceAt(2)).isEqualTo(70);   // состояние на версию 2
        assertThat(acc.history()).hasSize(3);
    }

    // ─── bad: только текущее значение, истории нет ───
    @Test
    void mutableAccountKeepsNoHistory() {
        BadEventSourcing.Account acc = new BadEventSourcing.Account();
        acc.deposit(100);
        acc.withdraw(30);
        acc.deposit(50);
        assertThat(acc.balance()).isEqualTo(120); // итог тот же, но прошлого не восстановить
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunBuildsLog() {
        PatternDemoResponse r = new EventSourcingDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(3);
        assertThat(r.getVerdict()).startsWith("PASS").contains("70");
    }

    @Test
    void badRunHasNoHistory() {
        PatternDemoResponse r = new EventSourcingDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getVerdict()).startsWith("FAIL");
        assertThat(r.getSteps()).anyMatch(s -> !s.isOk());
    }
}
