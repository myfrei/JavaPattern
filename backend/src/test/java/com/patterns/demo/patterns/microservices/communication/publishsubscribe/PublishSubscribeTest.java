package com.patterns.demo.patterns.microservices.communication.publishsubscribe;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PublishSubscribeTest {

    // ─── good: брокер доставляет всем независимо ───
    @Test
    void brokerFansOutToAllSubscribers() {
        GoodPubSub.Broker broker = new GoodPubSub.Broker();
        broker.subscribe("EmailConsumer");
        broker.subscribe("AnalyticsConsumer");
        broker.subscribe("InventoryConsumer");

        assertThat(broker.publish("OrderPlaced")).hasSize(3);
        assertThat(broker.subscriberCount()).isEqualTo(3);
    }

    // ─── bad: синхронная цепочка обрывается на сбое ───
    @Test
    void syncChainStopsAtFailure() {
        var trace = new BadPubSub.Publisher().publish("OrderPlaced");
        assertThat(trace).hasSize(2);                       // Inventory не достигнут
        assertThat(trace.get(1)).contains("FAIL");
        assertThat(trace).noneMatch(l -> l.contains("Inventory"));
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunDeliversToAll() {
        PatternDemoResponse r = new PublishSubscribeDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(3);
        assertThat(r.getVerdict()).startsWith("PASS");
    }

    @Test
    void badRunCascades() {
        PatternDemoResponse r = new PublishSubscribeDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getVerdict()).startsWith("FAIL");
        assertThat(r.getSteps()).anyMatch(s -> !s.isOk());
    }
}
