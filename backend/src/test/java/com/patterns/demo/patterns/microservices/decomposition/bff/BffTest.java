package com.patterns.demo.patterns.microservices.decomposition.bff;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BffTest {

    // ─── good: payload заточен под клиента ───
    @Test
    void mobileGetsTrimmedPayload() {
        var mobile = new GoodBff.MobileBff().product();
        assertThat(mobile).containsOnlyKeys("name", "price");
        assertThat(new GoodBff.WebBff().product()).hasSize(5);
    }

    // ─── bad: общий API → overfetch ───
    @Test
    void generalApiOverfetches() {
        assertThat(BadBff.generalProduct()).hasSize(5); // мобильный получает всё
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunTailorsPerClient() {
        PatternDemoResponse r = new BffDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(2);
        assertThat(r.getVerdict()).startsWith("PASS");
    }

    @Test
    void badRunOverfetches() {
        PatternDemoResponse r = new BffDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getVerdict()).startsWith("FAIL");
    }
}
