package com.patterns.demo.patterns.behavioral.memento;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MementoTest {

    // ─── good: снимок хранит копию значения ───
    @Test
    void restoreReturnsExactPastState() {
        GoodMemento.Editor editor = new GoodMemento.Editor();
        editor.type("A");
        GoodMemento.Memento s1 = editor.save();
        editor.type("B");
        GoodMemento.Memento s2 = editor.save();
        editor.type("C");
        assertThat(editor.content()).isEqualTo("ABC");

        editor.restore(s2);
        assertThat(editor.content()).isEqualTo("AB");
        editor.restore(s1);
        assertThat(editor.content()).isEqualTo("A");
    }

    // ─── bad: «снимок» делит ссылку на состояние ───
    @Test
    void sharedReferenceSnapshotIsCorrupted() {
        BadMemento.Editor editor = new BadMemento.Editor();
        editor.type("A");
        var snapshot = editor.lines; // ссылка, не копия
        editor.type("B");
        assertThat(snapshot).containsExactly("A", "B"); // снимок «протёк»
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunRestores() {
        PatternDemoResponse r = new MementoDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(2); // S1, S2
        assertThat(r.getVerdict()).startsWith("PASS");
    }

    @Test
    void badRunFailsToRestore() {
        PatternDemoResponse r = new MementoDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getVerdict()).startsWith("FAIL");
    }
}
