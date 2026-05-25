package com.patterns.demo.patterns.creational.builder;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BuilderTest {

    // ─── good: пошаговая сборка ───
    @Test
    void buildsPizzaFromNamedSteps() {
        GoodBuilder.Pizza pizza = GoodBuilder.Pizza.builder()
                .size("L").crust("тонкое").cheese(true)
                .topping("ветчина").topping("грибы")
                .build();
        assertThat(pizza.size()).isEqualTo("L");
        assertThat(pizza.toppings()).containsExactly("ветчина", "грибы");
        assertThat(pizza.describe()).contains("L").contains("ветчина");
    }

    @Test
    void buildValidatesRequiredField() {
        assertThatThrownBy(() -> GoodBuilder.Pizza.builder().topping("сыр").build())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void resultIsImmutable() {
        GoodBuilder.Pizza pizza = GoodBuilder.Pizza.builder().size("M").topping("ham").build();
        assertThatThrownBy(() -> pizza.toppings().add("hack"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    // ─── bad: телескопический конструктор ───
    @Test
    void telescopingConstructorIsPositionAndErrorProne() {
        // хотели ham=true, mushroom=false, но переставили флаги
        BadBuilder.Pizza wrong = new BadBuilder.Pizza("L", "тонкое", true, false, true);
        assertThat(wrong.ham()).isFalse();      // ветчины нет
        assertThat(wrong.mushroom()).isTrue();  // зато грибы — не то, что хотели
        assertThat(wrong.describe()).contains("грибы").doesNotContain("ветчина");
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunAssemblesParts() {
        PatternDemoResponse r = new BuilderDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).isNotEmpty();
        assertThat(r.getVerdict()).startsWith("PASS");
        assertThat(r.getSteps()).isNotEmpty();
    }

    @Test
    void badRunIsTelescoping() {
        PatternDemoResponse r = new BuilderDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getVerdict()).startsWith("FAIL");
        assertThat(r.getSteps()).isNotEmpty();
    }
}
