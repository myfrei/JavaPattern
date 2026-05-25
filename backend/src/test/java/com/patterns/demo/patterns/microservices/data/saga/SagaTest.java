package com.patterns.demo.patterns.microservices.data.saga;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SagaTest {

    private static List<GoodSaga.Step> scenario() {
        return List.of(
            new GoodSaga.Step("createOrder", true, "cancel order"),
            new GoodSaga.Step("chargePayment", true, "refund"),
            new GoodSaga.Step("reserveInventory", false, "restock"));
    }

    // ─── good: компенсации в обратном порядке ───
    @Test
    void failureTriggersCompensationsInReverse() {
        List<String> trace = GoodSaga.run(scenario());
        assertThat(trace).contains("✗ reserveInventory FAILED → compensating", "↩ refund", "↩ cancel order");
        // refund (chargePayment) откатывается раньше cancel order (createOrder)
        assertThat(trace.indexOf("↩ refund")).isLessThan(trace.indexOf("↩ cancel order"));
    }

    @Test
    void allOkNeedsNoCompensation() {
        List<String> trace = GoodSaga.run(List.of(
            new GoodSaga.Step("a", true, "ca"),
            new GoodSaga.Step("b", true, "cb")));
        assertThat(trace).containsExactly("✓ a", "✓ b");
    }

    // ─── bad: без компенсаций ───
    @Test
    void noCompensationLeavesPartialCommit() {
        List<String> trace = BadSaga.run(scenario());
        assertThat(trace).noneMatch(l -> l.startsWith("↩"));
        assertThat(trace.get(trace.size() - 1)).contains("FAILED");
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunCompensates() {
        PatternDemoResponse r = new SagaDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getVerdict()).startsWith("PASS");
        assertThat(r.getSteps()).anyMatch(s -> s.getAction().equals("compensate"));
    }

    @Test
    void badRunIsInconsistent() {
        PatternDemoResponse r = new SagaDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getVerdict()).startsWith("FAIL");
    }
}
