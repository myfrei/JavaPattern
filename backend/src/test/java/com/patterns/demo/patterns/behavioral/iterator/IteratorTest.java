package com.patterns.demo.patterns.behavioral.iterator;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class IteratorTest {

    // ─── good: обход через for-each / итератор ───
    @Test
    void iteratesInOrderViaForEach() {
        GoodIterator.Repo repo = new GoodIterator.Repo("a", "b", "c");
        List<String> seen = new ArrayList<>();
        for (String s : repo) seen.add(s);
        assertThat(seen).containsExactly("a", "b", "c");
    }

    @Test
    void iteratorReportsEnd() {
        var it = new GoodIterator.Repo("only").iterator();
        assertThat(it.hasNext()).isTrue();
        assertThat(it.next()).isEqualTo("only");
        assertThat(it.hasNext()).isFalse();
    }

    // ─── bad: клиент завязан на массив ───
    @Test
    void clientReachesIntoArray() {
        BadIterator.Repo repo = new BadIterator.Repo("a", "b", "c");
        assertThat(repo.size()).isEqualTo(3);
        assertThat(repo.data[0]).isEqualTo("a"); // обнажённая структура
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunWalksAll() {
        PatternDemoResponse r = new IteratorDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(3);
        assertThat(r.getSteps()).hasSize(3);
        assertThat(r.getVerdict()).startsWith("PASS");
    }

    @Test
    void badRunIsCoupled() {
        PatternDemoResponse r = new IteratorDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getVerdict()).startsWith("FAIL");
    }
}
