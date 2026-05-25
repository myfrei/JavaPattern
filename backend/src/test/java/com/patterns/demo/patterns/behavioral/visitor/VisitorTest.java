package com.patterns.demo.patterns.behavioral.visitor;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VisitorTest {

    // ─── good: двойная диспетчеризация ───
    @Test
    void visitorComputesPerType() {
        GoodVisitor.AreaVisitor area = new GoodVisitor.AreaVisitor();
        assertThat(new GoodVisitor.Circle(2).accept(area)).isEqualTo(13); // round(π·4)
        assertThat(new GoodVisitor.Square(3).accept(area)).isEqualTo(9);
    }

    @Test
    void newOperationIsAnotherVisitor() {
        GoodVisitor.NameVisitor name = new GoodVisitor.NameVisitor();
        assertThat(new GoodVisitor.Circle(2).accept(name)).isEqualTo("Circle(r=2)");
    }

    // ─── bad: лестница instanceof ───
    @Test
    void instanceofLadderHandlesKnownTypes() {
        assertThat(BadVisitor.area(new GoodVisitor.Circle(2))).isEqualTo(13);
        assertThat(BadVisitor.area(new GoodVisitor.Square(3))).isEqualTo(9);
        assertThatThrownBy(() -> BadVisitor.area("not a shape"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunVisitsAll() {
        PatternDemoResponse r = new VisitorDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(2);
        assertThat(r.getVerdict()).startsWith("PASS");
    }

    @Test
    void badRunUsesInstanceof() {
        PatternDemoResponse r = new VisitorDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getVerdict()).startsWith("FAIL");
    }
}
