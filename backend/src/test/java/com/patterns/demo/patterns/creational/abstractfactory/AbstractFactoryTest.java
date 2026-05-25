package com.patterns.demo.patterns.creational.abstractfactory;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AbstractFactoryTest {

    // ─── good: фабрика отдаёт согласованное семейство ───
    @Test
    void winFactoryProducesConsistentFamily() {
        var f = new GoodAbstractFactory.WinFactory();
        assertThat(f.family()).isEqualTo("Windows");
        assertThat(f.createButton().render()).contains("Win");
        assertThat(f.createCheckbox().render()).contains("Win");
    }

    @Test
    void forOsSelectsFamily() {
        assertThat(GoodAbstractFactory.forOs("mac").family()).isEqualTo("macOS");
        assertThat(GoodAbstractFactory.forOs("windows").family()).isEqualTo("Windows");
    }

    // ─── bad: ручной выбор мешает семейства ───
    @Test
    void badToolbarMixesFamilies() {
        List<BadAbstractFactory.Widget> widgets = BadAbstractFactory.buildToolbar();
        long distinctFamilies = widgets.stream().map(BadAbstractFactory.Widget::family).distinct().count();
        assertThat(distinctFamilies).isEqualTo(2); // семейства перемешаны
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunKeepsOneFamily() {
        PatternDemoResponse r = new AbstractFactoryDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        long families = r.getInstances().stream()
                .map(PatternDemoResponse.InstanceInfo::getCreatedBy).distinct().count();
        assertThat(families).isEqualTo(1);
        assertThat(r.getVerdict()).startsWith("PASS");
    }

    @Test
    void badRunHasMixedFamilies() {
        PatternDemoResponse r = new AbstractFactoryDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        long families = r.getInstances().stream()
                .map(PatternDemoResponse.InstanceInfo::getCreatedBy).distinct().count();
        assertThat(families).isEqualTo(2);
        assertThat(r.getVerdict()).startsWith("FAIL");
    }
}
