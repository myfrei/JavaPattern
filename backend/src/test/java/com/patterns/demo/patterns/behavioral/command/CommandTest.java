package com.patterns.demo.patterns.behavioral.command;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommandTest {

    // ─── good: история и откат ───
    @Test
    void undoRestoresPreviousState() {
        GoodCommand.Document doc = new GoodCommand.Document();
        GoodCommand.Invoker inv = new GoodCommand.Invoker();
        inv.run(new GoodCommand.AppendCommand(doc, "Hello "));
        inv.run(new GoodCommand.AppendCommand(doc, "World"));
        assertThat(doc.text()).isEqualTo("Hello World");

        assertThat(inv.undo()).isTrue();
        assertThat(doc.text()).isEqualTo("Hello ");
        assertThat(inv.historySize()).isEqualTo(1);
    }

    @Test
    void undoOnEmptyHistoryIsNoop() {
        assertThat(new GoodCommand.Invoker().undo()).isFalse();
    }

    // ─── bad: нет истории ───
    @Test
    void directAppendHasNoUndo() {
        BadCommand.Document doc = new BadCommand.Document();
        doc.append("Hello ");
        doc.append("World");
        assertThat(doc.text()).isEqualTo("Hello World"); // и откатить нечем
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunUndoes() {
        PatternDemoResponse r = new CommandDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(2);
        assertThat(r.getVerdict()).startsWith("PASS");
    }

    @Test
    void badRunHasNoUndo() {
        PatternDemoResponse r = new CommandDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getVerdict()).startsWith("FAIL");
    }
}
