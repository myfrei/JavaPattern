package com.patterns.demo.patterns.structural.facade;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FacadeTest {

    // ─── good: один вызов оркеструет подсистемы ───
    @Test
    void placeOrderOrchestratesSubsystems() {
        String out = new GoodFacade.OrderFacade().placeOrder("SKU-42", 2599);
        assertThat(out).isEqualTo("shipped SKU-42");
    }

    @Test
    void placeOrderHandlesOutOfStock() {
        assertThat(new GoodFacade.OrderFacade().placeOrder("UNKNOWN", 2599)).isEqualTo("out of stock");
    }

    // ─── bad: клиент сам зовёт все подсистемы ───
    @Test
    void clientOrchestratesManually() {
        assertThat(BadFacade.checkout("SKU-42", 2599)).isEqualTo("shipped SKU-42");
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunHidesSubsystems() {
        PatternDemoResponse r = new FacadeDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(4); // facade + 3 подсистемы
        assertThat(r.getVerdict()).startsWith("PASS");
    }

    @Test
    void badRunExposesSubsystems() {
        PatternDemoResponse r = new FacadeDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getInstances()).hasSize(3); // без фасада
        assertThat(r.getVerdict()).startsWith("FAIL");
    }
}
