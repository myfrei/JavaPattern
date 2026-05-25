package com.patterns.demo.patterns.behavioral.strategy;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StrategyTest {

    // ─── good: подмена стратегии в рантайме ───
    @Test
    void swappableStrategiesProduceDifferentResults() {
        GoodStrategy.Cart cart = new GoodStrategy.Cart();
        assertThat(cart.checkout(1000)).isEqualTo(1000); // NoDiscount по умолчанию

        cart.setDiscount(new GoodStrategy.Percent(10));
        assertThat(cart.checkout(1000)).isEqualTo(900);

        cart.setDiscount(new GoodStrategy.Coupon(150));
        assertThat(cart.checkout(1000)).isEqualTo(850);
    }

    // ─── bad: те же формулы через switch ───
    @Test
    void switchVersionMatchesResults() {
        assertThat(BadStrategy.checkout(1000, "percent", 10)).isEqualTo(900);
        assertThat(BadStrategy.checkout(1000, "coupon", 150)).isEqualTo(850);
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunSwapsStrategies() {
        PatternDemoResponse r = new StrategyDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(3);
        assertThat(r.getVerdict()).startsWith("PASS");
    }

    @Test
    void badRunUsesSwitch() {
        PatternDemoResponse r = new StrategyDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getVerdict()).startsWith("FAIL");
    }
}
