package com.patterns.demo.patterns.structural.flyweight;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FlyweightTest {

    // ─── good: пул отдаёт один и тот же объект на одинаковый тип ───
    @Test
    void factoryReusesSharedType() {
        GoodFlyweight.TreeFactory factory = new GoodFlyweight.TreeFactory();
        GoodFlyweight.TreeType a = factory.get("oak", "green");
        GoodFlyweight.TreeType b = factory.get("oak", "green");
        assertThat(a).isSameAs(b);                 // переиспользован
        factory.get("pine", "dark");
        assertThat(factory.distinctTypes()).isEqualTo(2);
    }

    // ─── bad: каждое дерево держит свою копию тяжёлых данных ───
    @Test
    void eachTreeDuplicatesHeavyState() {
        BadFlyweight.Tree t1 = new BadFlyweight.Tree(0, 0, "oak", "green");
        BadFlyweight.Tree t2 = new BadFlyweight.Tree(1, 1, "oak", "green");
        assertThat(t1.type()).isNotSameAs(t2.type()); // данные продублированы
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunSharesTypes() {
        PatternDemoResponse r = new FlyweightDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(5);
        assertThat(r.getVerdict()).startsWith("PASS").contains("2 общих");
    }

    @Test
    void badRunDuplicatesTypes() {
        PatternDemoResponse r = new FlyweightDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getInstances()).hasSize(5);
        assertThat(r.getVerdict()).startsWith("FAIL");
    }
}
