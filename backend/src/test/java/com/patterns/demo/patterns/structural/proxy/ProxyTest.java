package com.patterns.demo.patterns.structural.proxy;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProxyTest {

    // ─── good: прокси кэширует и срезает дорогие вызовы ───
    @Test
    void proxyCachesRepeatedCalls() {
        GoodProxy.RealService real = new GoodProxy.RealService();
        GoodProxy.CachingProxy proxy = new GoodProxy.CachingProxy(real);

        assertThat(proxy.fetch(1)).isEqualTo("data#1");
        proxy.fetch(1); // HIT
        proxy.fetch(2); // MISS

        assertThat(real.calls()).isEqualTo(2);     // 3 запроса → 2 реальных вызова
        assertThat(proxy.isCached(1)).isTrue();
    }

    // ─── bad: каждый вызов идёт в дорогой сервис ───
    @Test
    void directCallsAreAlwaysExpensive() {
        BadProxy.DirectService direct = new BadProxy.DirectService();
        direct.fetch(1);
        direct.fetch(1);
        assertThat(direct.calls()).isEqualTo(2);   // повтор не сэкономлен
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunUsesCache() {
        PatternDemoResponse r = new ProxyDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(3);
        assertThat(r.getVerdict()).startsWith("PASS").contains("2");
    }

    @Test
    void badRunHasNoCache() {
        PatternDemoResponse r = new ProxyDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getInstances()).hasSize(2);
        assertThat(r.getVerdict()).startsWith("FAIL").contains("3");
    }
}
