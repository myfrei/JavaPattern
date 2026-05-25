package com.patterns.demo.patterns.creational.prototype;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PrototypeTest {

    // ─── good: глубокая копия независима ───
    @Test
    void deepCopyIsIndependent() {
        GoodPrototype.Document original = new GoodPrototype.Document("Шаблон", List.of("draft"));
        GoodPrototype.Document clone = original.copy();
        clone.addTag("v2");

        assertThat(clone.tags()).containsExactly("draft", "v2");
        assertThat(original.tags()).containsExactly("draft");        // оригинал цел
        assertThat(clone.tags()).isNotSameAs(original.tags());       // разные списки
    }

    // ─── bad: поверхностная копия делит вложенный список ───
    @Test
    void shallowCopySharesNestedList() {
        BadPrototype.Document original = new BadPrototype.Document("Шаблон", new ArrayList<>(List.of("draft")));
        BadPrototype.Document clone = original.copy();
        clone.addTag("v2");

        assertThat(clone.tags()).isSameAs(original.tags());          // один и тот же список
        assertThat(original.tags()).contains("v2");                 // оригинал испорчен
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunKeepsOriginalIntact() {
        PatternDemoResponse r = new PrototypeDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(2);
        assertThat(r.getVerdict()).startsWith("PASS");
        assertThat(r.getSteps()).isNotEmpty();
    }

    @Test
    void badRunCorruptsOriginal() {
        PatternDemoResponse r = new PrototypeDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getInstances()).hasSize(2);
        assertThat(r.getVerdict()).startsWith("FAIL");
        assertThat(r.getSteps()).isNotEmpty();
    }
}
