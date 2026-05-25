package com.patterns.demo.patterns.microservices.resilience.circuitbreaker;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CircuitBreakerTest {

    // ─── good: размыкание, fast-fail, восстановление ───
    @Test
    void opensAfterThresholdAndFastFails() {
        var breaker = new GoodCircuitBreaker.Breaker(3);
        var ds = new GoodCircuitBreaker.Downstream(); // unhealthy

        breaker.call(ds); breaker.call(ds); breaker.call(ds);
        assertThat(breaker.state()).isEqualTo(GoodCircuitBreaker.State.OPEN);
        assertThat(ds.calls()).isEqualTo(3);

        assertThat(breaker.call(ds)).isEqualTo("fast-fail");
        assertThat(ds.calls()).isEqualTo(3); // downstream не вызван — вызов сэкономлен
    }

    @Test
    void halfOpenRecoversToClosed() {
        var breaker = new GoodCircuitBreaker.Breaker(2);
        var ds = new GoodCircuitBreaker.Downstream();
        breaker.call(ds); breaker.call(ds);           // OPEN
        breaker.halfOpen();
        ds.setHealthy(true);
        assertThat(breaker.call(ds)).isEqualTo("ok");
        assertThat(breaker.state()).isEqualTo(GoodCircuitBreaker.State.CLOSED);
    }

    // ─── bad: каждый вызов бьёт по downstream ───
    @Test
    void noBreakerHitsDownstreamEveryTime() {
        var ds = new BadCircuitBreaker.Downstream();
        for (int i = 0; i < 5; i++) ds.call();
        assertThat(ds.calls()).isEqualTo(5);
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunOpensAndRecovers() {
        PatternDemoResponse r = new CircuitBreakerDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(3);
        assertThat(r.getVerdict()).startsWith("PASS");
    }

    @Test
    void badRunHammersDownstream() {
        PatternDemoResponse r = new CircuitBreakerDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getVerdict()).startsWith("FAIL").contains("5");
    }
}
