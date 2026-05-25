package com.patterns.demo.patterns.creational.singleton;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SingletonDemoServiceTest {

    private final SingletonDemoService service = new SingletonDemoService();

    @Test
    void goodRunYieldsSingleInstance() {
        PatternDemoResponse r = service.runGood();

        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(1);
        assertThat(r.getVerdict()).startsWith("PASS");
        assertThat(r.getSteps()).isNotEmpty();
        assertThat(r.getCode()).contains("volatile");
    }

    @Test
    void badRunProducesStructuredReport() {
        PatternDemoResponse r = service.runBad();

        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getSteps()).isNotEmpty();
        assertThat(r.getInstances()).isNotEmpty(); // от 1 (повезло) до THREAD_COUNT (гонка)
        assertThat(r.getVerdict()).isNotBlank();
        assertThat(r.getExplanation()).isNotBlank();
    }
}
