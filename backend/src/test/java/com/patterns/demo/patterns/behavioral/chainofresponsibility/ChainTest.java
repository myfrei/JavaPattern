package com.patterns.demo.patterns.behavioral.chainofresponsibility;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChainTest {

    // ─── good: запрос обрабатывает нужное звено ───
    @Test
    void directorApprovesMidAmount() {
        var trace = GoodChain.run(new GoodChain.Request(15_000));
        assertThat(trace).anySatisfy(tr -> {
            assertThat(tr.actor()).isEqualTo("Director");
            assertThat(tr.result()).contains("APPROVED");
        });
    }

    @Test
    void teamLeadApprovesSmallAmount() {
        var trace = GoodChain.run(new GoodChain.Request(500));
        assertThat(trace).anySatisfy(tr -> {
            assertThat(tr.actor()).isEqualTo("TeamLead");
            assertThat(tr.result()).contains("APPROVED");
        });
    }

    // ─── bad: монолит выбирает ту же ветку, но без цепочки ───
    @Test
    void monolithPicksDirectorBranch() {
        assertThat(BadChain.approve(15_000).approver()).isEqualTo("Director");
        assertThat(BadChain.approve(500).approver()).isEqualTo("TeamLead");
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunVerdictPass() {
        PatternDemoResponse r = new ChainDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getVerdict()).startsWith("PASS");
        assertThat(r.getInstances()).isNotEmpty();
        assertThat(r.getSteps()).isNotEmpty();
    }

    @Test
    void badRunVerdictFail() {
        PatternDemoResponse r = new ChainDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getVerdict()).startsWith("FAIL");
    }
}
