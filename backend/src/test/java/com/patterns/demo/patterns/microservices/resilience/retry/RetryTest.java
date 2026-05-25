package com.patterns.demo.patterns.microservices.resilience.retry;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RetryTest {

    // ─── good: повтор доводит до успеха с backoff ───
    @Test
    void retriesUntilSuccessWithBackoff() {
        GoodRetry.FlakyService svc = new GoodRetry.FlakyService(2);
        List<GoodRetry.Attempt> log = GoodRetry.run(svc, 5);

        assertThat(log).hasSize(3);
        assertThat(log.get(2).ok()).isTrue();              // успех на 3-й
        assertThat(svc.calls()).isEqualTo(3);
        assertThat(log).extracting(GoodRetry.Attempt::backoffMs).containsExactly(0L, 100L, 200L);
    }

    @Test
    void givesUpAfterMaxAttempts() {
        GoodRetry.FlakyService svc = new GoodRetry.FlakyService(5);
        List<GoodRetry.Attempt> log = GoodRetry.run(svc, 3);
        assertThat(log).hasSize(3);
        assertThat(log).noneMatch(GoodRetry.Attempt::ok);  // все 3 неудачны
    }

    // ─── bad: одна попытка ───
    @Test
    void noRetryFailsOnTransientError() {
        assertThat(BadRetry.once(new GoodRetry.FlakyService(2))).isFalse();
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunSucceedsOnThirdAttempt() {
        PatternDemoResponse r = new RetryDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(3);
        assertThat(r.getVerdict()).startsWith("PASS");
    }

    @Test
    void badRunFails() {
        PatternDemoResponse r = new RetryDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getVerdict()).startsWith("FAIL");
    }
}
