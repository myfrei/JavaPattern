package com.patterns.demo.patterns.microservices.data.cqrs;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CqrsTest {

    // ─── good: запрос читает из проекции ───
    @Test
    void readSideServesProjection() {
        GoodCqrs.WriteSide write = new GoodCqrs.WriteSide();
        GoodCqrs.ReadSide read = new GoodCqrs.ReadSide();
        write.deposit(100); read.project(write.balance());
        write.deposit(50);  read.project(write.balance());

        assertThat(write.balance()).isEqualTo(150);
        assertThat(read.query()).isEqualTo("balance: 150");
    }

    // ─── bad: одна модель пересчитывает по логу ───
    @Test
    void singleModelRecomputesOnQuery() {
        BadCqrs model = new BadCqrs();
        model.deposit(100);
        model.deposit(50);
        assertThat(model.query()).isEqualTo(150);
        assertThat(model.logSize()).isEqualTo(2); // запрос проходит по всему логу
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunSeparatesSides() {
        PatternDemoResponse r = new CqrsDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(2);
        assertThat(r.getVerdict()).startsWith("PASS");
    }

    @Test
    void badRunHasOneModel() {
        PatternDemoResponse r = new CqrsDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getInstances()).hasSize(1);
        assertThat(r.getVerdict()).startsWith("FAIL");
    }
}
