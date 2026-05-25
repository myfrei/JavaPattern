package com.patterns.demo.patterns.microservices.communication.sidecar;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SidecarTest {

    // ─── good: бизнес-логика чистая, сквозное в сайдкаре ───
    @Test
    void appStaysCleanSidecarWraps() {
        assertThat(new GoodSidecar.OrderApp().handle("#42")).isEqualTo("order:#42"); // без инфры
        var trace = new GoodSidecar.Sidecar(new GoodSidecar.OrderApp()).process("#42");
        assertThat(trace).contains("TLS terminate", "rate-limit check", "metrics emit", "biz: order:#42");
    }

    // ─── bad: инфра-код внутри сервиса ───
    @Test
    void appEmbedsInfra() {
        var trace = new BadSidecar.OrderApp().handle("#42");
        assertThat(trace).anyMatch(l -> l.contains("(inline)")); // инфра в сервисе
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunSeparatesConcerns() {
        PatternDemoResponse r = new SidecarDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).anyMatch(i -> i.getCreatedBy().equals("cross-cutting"));
        assertThat(r.getVerdict()).startsWith("PASS");
    }

    @Test
    void badRunMixesConcerns() {
        PatternDemoResponse r = new SidecarDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getInstances()).hasSize(1);
        assertThat(r.getVerdict()).startsWith("FAIL");
    }
}
