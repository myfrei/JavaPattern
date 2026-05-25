package com.patterns.demo.patterns.structural.adapter;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AdapterTest {

    // ─── good: адаптер транслирует интерфейс и единицы ───
    @Test
    void adapterTranslatesCentsToDollars() {
        GoodAdapter.PaymentGateway gateway = new GoodAdapter.BankAdapter(new GoodAdapter.LegacyBank());
        assertThat(gateway.pay(2599)).isEqualTo("transfer 25.99 USD");
    }

    // ─── bad: прямые вызовы дублируют конвертацию и ловят баг ───
    @Test
    void directCallsAreDuplicatedAndBugProne() {
        assertThat(BadAdapter.checkoutCorrect(2599)).isEqualTo("transfer 25.99 USD");
        assertThat(BadAdapter.checkoutBuggy(2599)).isEqualTo("transfer 2599.00 USD"); // списали $2599
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunGoesThroughAdapter() {
        PatternDemoResponse r = new AdapterDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(3);
        assertThat(r.getVerdict()).startsWith("PASS");
        assertThat(r.getSteps()).isNotEmpty();
    }

    @Test
    void badRunHasBug() {
        PatternDemoResponse r = new AdapterDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getVerdict()).startsWith("FAIL");
        assertThat(r.getSteps()).anyMatch(s -> !s.isOk());
    }
}
