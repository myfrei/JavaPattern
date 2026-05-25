package com.patterns.demo.patterns.microservices.communication.servicediscovery;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceDiscoveryTest {

    // ─── good: реестр резолвит и переживает гибель инстанса ───
    @Test
    void registryResolvesRoundRobinAndFailsOver() {
        GoodServiceDiscovery.Registry reg = new GoodServiceDiscovery.Registry();
        reg.register("order", "a");
        reg.register("order", "b");
        assertThat(reg.instances("order")).containsExactly("a", "b");

        assertThat(reg.resolve("order")).isEqualTo("a"); // rr 0
        assertThat(reg.resolve("order")).isEqualTo("b"); // rr 1

        assertThat(reg.deregister("order", "a")).isTrue();
        assertThat(reg.resolve("order")).isEqualTo("b"); // failover прозрачен
    }

    // ─── bad: хардкод адреса ломается при переезде ───
    @Test
    void hardcodedAddressBreaksAfterReschedule() {
        assertThat(BadServiceDiscovery.callSucceeds(BadServiceDiscovery.HARDCODED, BadServiceDiscovery.HARDCODED)).isTrue();
        assertThat(BadServiceDiscovery.callSucceeds(BadServiceDiscovery.HARDCODED, "10.0.0.7:8080")).isFalse();
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunResolvesThroughRegistry() {
        PatternDemoResponse r = new ServiceDiscoveryDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(2);
        assertThat(r.getVerdict()).startsWith("PASS");
    }

    @Test
    void badRunFailsAfterReschedule() {
        PatternDemoResponse r = new ServiceDiscoveryDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getVerdict()).startsWith("FAIL");
        assertThat(r.getSteps()).anyMatch(s -> !s.isOk());
    }
}
