package com.patterns.demo.patterns.microservices.resilience.bulkhead;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BulkheadTest {

    // ─── good: отдельные пулы изолируют насыщение ───
    @Test
    void saturatedPoolDoesNotStarveOthers() {
        GoodBulkhead.Pool report = new GoodBulkhead.Pool("reportPool", 2);
        GoodBulkhead.Pool payment = new GoodBulkhead.Pool("paymentPool", 2);

        assertThat(report.acquire()).isTrue();
        assertThat(report.acquire()).isTrue();
        assertThat(report.acquire()).isFalse(); // report полон

        assertThat(payment.acquire()).isTrue(); // payment изолирован — проходит
    }

    // ─── bad: общий пул голодает payment ───
    @Test
    void sharedPoolStarvesUnrelatedCalls() {
        BadBulkhead.SharedPool shared = new BadBulkhead.SharedPool(4);
        for (int i = 0; i < 4; i++) assertThat(shared.acquire()).isTrue(); // report занял всё
        assertThat(shared.acquire()).isFalse(); // payment голодает
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunIsolatesPools() {
        PatternDemoResponse r = new BulkheadDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(2);
        assertThat(r.getVerdict()).startsWith("PASS");
    }

    @Test
    void badRunStarves() {
        PatternDemoResponse r = new BulkheadDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getInstances()).hasSize(1);
        assertThat(r.getVerdict()).startsWith("FAIL");
    }
}
