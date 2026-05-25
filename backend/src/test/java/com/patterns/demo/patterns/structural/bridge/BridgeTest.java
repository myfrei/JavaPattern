package com.patterns.demo.patterns.structural.bridge;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BridgeTest {

    // ─── good: любая фигура × любой рендерер через композицию ───
    @Test
    void anyShapeWorksWithAnyRenderer() {
        assertThat(new GoodBridge.Circle(new GoodBridge.VectorRenderer()).draw()).isEqualTo("vector[circle]");
        assertThat(new GoodBridge.Circle(new GoodBridge.RasterRenderer()).draw()).isEqualTo("raster(circle)");
        assertThat(new GoodBridge.Square(new GoodBridge.VectorRenderer()).draw()).isEqualTo("vector[square]");
        assertThat(new GoodBridge.Square(new GoodBridge.RasterRenderer()).draw()).isEqualTo("raster(square)");
    }

    // ─── bad: отдельный класс на каждую пару ───
    @Test
    void combinationClassesExplode() {
        assertThat(new BadBridge.VectorCircle().draw()).isEqualTo("vector[circle]");
        assertThat(BadBridge.classCount()).isEqualTo(4); // 2×2
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunCoversFourCombos() {
        PatternDemoResponse r = new BridgeDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(4);
        assertThat(r.getSteps()).hasSize(4);
        assertThat(r.getVerdict()).startsWith("PASS");
    }

    @Test
    void badRunListsCombinationClasses() {
        PatternDemoResponse r = new BridgeDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getInstances()).hasSize(4);
        assertThat(r.getVerdict()).startsWith("FAIL");
    }
}
