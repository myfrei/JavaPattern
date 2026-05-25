package com.patterns.demo.patterns.behavioral.templatemethod;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TemplateMethodTest {

    // ─── good: скелет общий, различаются только хуки ───
    @Test
    void skeletonIsSharedHooksDiffer() {
        assertThat(new GoodTemplate.Tea().prepare())
                .containsExactly("boil water", "steep tea", "pour into cup", "add lemon");
        assertThat(new GoodTemplate.Coffee().prepare())
                .containsExactly("boil water", "brew coffee", "pour into cup", "add sugar");
    }

    @Test
    void fixedStepsCannotBeLost() {
        // оба варианта гарантированно содержат шаги скелета
        assertThat(new GoodTemplate.Coffee().prepare()).contains("boil water", "pour into cup");
    }

    // ─── bad: копипаст потерял шаг ───
    @Test
    void copiedAlgorithmDropsAStep() {
        assertThat(new BadTemplate.Coffee().prepare()).doesNotContain("boil water"); // шаг потерян
        assertThat(new BadTemplate.Tea().prepare()).contains("boil water");
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunKeepsSkeleton() {
        PatternDemoResponse r = new TemplateMethodDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(4);
        assertThat(r.getVerdict()).startsWith("PASS");
    }

    @Test
    void badRunLostStep() {
        PatternDemoResponse r = new TemplateMethodDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getVerdict()).startsWith("FAIL");
    }
}
