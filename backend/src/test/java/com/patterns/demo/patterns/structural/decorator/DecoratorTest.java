package com.patterns.demo.patterns.structural.decorator;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DecoratorTest {

    // ─── good: обёртки складывают цену и подпись ───
    @Test
    void wrappersComposeCostAndLabel() {
        GoodDecorator.Beverage drink = new GoodDecorator.Sugar(new GoodDecorator.Milk(new GoodDecorator.Espresso()));
        assertThat(drink.cost()).isEqualTo(110);
        assertThat(drink.describe()).isEqualTo("espresso, milk, sugar");
    }

    @Test
    void layersAreIndependentlyComposable() {
        assertThat(new GoodDecorator.Milk(new GoodDecorator.Espresso()).cost()).isEqualTo(100);
        assertThat(new GoodDecorator.Sugar(new GoodDecorator.Espresso()).describe()).isEqualTo("espresso, sugar");
    }

    // ─── bad: класс на каждую комбинацию ───
    @Test
    void combinationClassesExplode() {
        assertThat(new BadDecorator.EspressoWithMilkAndSugar().cost()).isEqualTo(110);
        assertThat(BadDecorator.classCount()).isEqualTo(4); // 2 добавки → 4 класса
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunBuildsLayers() {
        PatternDemoResponse r = new DecoratorDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(3);
        assertThat(r.getVerdict()).startsWith("PASS").contains("110");
    }

    @Test
    void badRunListsCombinationClasses() {
        PatternDemoResponse r = new DecoratorDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getVerdict()).startsWith("FAIL");
    }
}
