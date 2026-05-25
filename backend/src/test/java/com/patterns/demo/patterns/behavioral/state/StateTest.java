package com.patterns.demo.patterns.behavioral.state;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StateTest {

    // ─── good: переходы инкапсулированы в состояниях ───
    @Test
    void documentAdvancesThroughStates() {
        GoodState.Document doc = new GoodState.Document();
        assertThat(doc.state()).isEqualTo("Draft");
        doc.publish();
        assertThat(doc.state()).isEqualTo("Review");
        doc.publish();
        assertThat(doc.state()).isEqualTo("Published");
        doc.publish();
        assertThat(doc.state()).isEqualTo("Published"); // финальное — no-op
    }

    // ─── bad: тот же workflow через switch ───
    @Test
    void switchVersionFollowsSameTransitions() {
        BadState.Document doc = new BadState.Document();
        doc.publish();
        doc.publish();
        assertThat(doc.state()).isEqualTo("Published");
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunReachesPublished() {
        PatternDemoResponse r = new StateDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(3);
        assertThat(r.getVerdict()).startsWith("PASS").contains("Published");
    }

    @Test
    void badRunIsMonolithic() {
        PatternDemoResponse r = new StateDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getVerdict()).startsWith("FAIL");
    }
}
