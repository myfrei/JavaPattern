package com.patterns.demo.patterns.behavioral.mediator;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MediatorTest {

    // ─── good: посредник доставляет всем, кроме отправителя ───
    @Test
    void roomDeliversToEveryoneElse() {
        GoodMediator.ChatRoom room = new GoodMediator.ChatRoom();
        room.register("Alice"); room.register("Bob"); room.register("Carol");

        assertThat(room.send("Alice", "hi")).containsExactly("Bob", "Carol");
        assertThat(room.connections()).isEqualTo(3); // линейно
    }

    // ─── bad: полносвязная сеть ───
    @Test
    void meshConnectsEveryoneToEveryone() {
        BadMediator.User alice = new BadMediator.User("Alice");
        BadMediator.User bob = new BadMediator.User("Bob");
        alice.connect(bob);
        assertThat(alice.send("hi")).containsExactly("Bob");
        assertThat(BadMediator.meshConnections(3)).isEqualTo(6); // N×(N−1)
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunRoutesThroughMediator() {
        PatternDemoResponse r = new MediatorDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(3);
        assertThat(r.getVerdict()).startsWith("PASS");
    }

    @Test
    void badRunIsMesh() {
        PatternDemoResponse r = new MediatorDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getVerdict()).startsWith("FAIL").contains("6");
    }
}
