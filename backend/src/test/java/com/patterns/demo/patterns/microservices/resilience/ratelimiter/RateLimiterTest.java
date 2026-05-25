package com.patterns.demo.patterns.microservices.resilience.ratelimiter;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RateLimiterTest {

    // ─── good: token bucket пропускает лимит, лишнее → 429 ───
    @Test
    void bucketAllowsUpToCapacityThenRejects() {
        GoodRateLimiter.TokenBucket bucket = new GoodRateLimiter.TokenBucket(3);
        assertThat(bucket.tryAcquire()).isTrue();
        assertThat(bucket.tryAcquire()).isTrue();
        assertThat(bucket.tryAcquire()).isTrue();
        assertThat(bucket.tokens()).isZero();
        assertThat(bucket.tryAcquire()).isFalse(); // 429
        assertThat(bucket.tryAcquire()).isFalse();
    }

    @Test
    void refillRestoresTokens() {
        GoodRateLimiter.TokenBucket bucket = new GoodRateLimiter.TokenBucket(2);
        bucket.tryAcquire(); bucket.tryAcquire();
        assertThat(bucket.tryAcquire()).isFalse();
        bucket.refill();
        assertThat(bucket.tryAcquire()).isTrue();
    }

    // ─── bad: без лимита всё проходит ───
    @Test
    void noLimiterAcceptsEverything() {
        BadRateLimiter.Service svc = new BadRateLimiter.Service();
        for (int i = 0; i < 5; i++) svc.handle();
        assertThat(svc.load()).isEqualTo(5); // перегрузка
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunRejectsExcess() {
        PatternDemoResponse r = new RateLimiterDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(5);
        assertThat(r.getInstances()).filteredOn(i -> i.getCreatedBy().equals("429")).hasSize(2);
        assertThat(r.getVerdict()).startsWith("PASS");
    }

    @Test
    void badRunOverloads() {
        PatternDemoResponse r = new RateLimiterDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getVerdict()).startsWith("FAIL");
    }
}
