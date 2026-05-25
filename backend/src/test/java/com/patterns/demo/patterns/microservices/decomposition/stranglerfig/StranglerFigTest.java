package com.patterns.demo.patterns.microservices.decomposition.stranglerfig;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StranglerFigTest {

    // ─── good: маршруты переключаются по одному ───
    @Test
    void routesMigrateIncrementally() {
        GoodStrangler.Router router = new GoodStrangler.Router();
        router.migrate("/login");
        assertThat(router.handle("/login")).isEqualTo("new");
        assertThat(router.handle("/cart")).isEqualTo("legacy"); // ещё не мигрирован
        assertThat(router.migratedCount()).isEqualTo(1);

        router.migrate("/cart");
        assertThat(router.handle("/cart")).isEqualTo("new");
    }

    // ─── bad: big-bang переключение ───
    @Test
    void bigBangSwitchesEverythingAtOnce() {
        assertThat(BadStrangler.bigBang(4)).contains("4");
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunMigratesAllRoutes() {
        PatternDemoResponse r = new StranglerFigDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(4);
        assertThat(r.getSteps()).hasSize(4);
        assertThat(r.getVerdict()).startsWith("PASS");
    }

    @Test
    void badRunIsBigBang() {
        PatternDemoResponse r = new StranglerFigDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getVerdict()).startsWith("FAIL");
    }
}
