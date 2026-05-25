package com.patterns.demo.patterns.behavioral.interpreter;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InterpreterTest {

    // ─── good: AST вычисляется рекурсией ───
    @Test
    void astEvaluatesRecursively() {
        GoodInterpreter.Expr ast = new GoodInterpreter.Sub(
            new GoodInterpreter.Add(new GoodInterpreter.Num(5), new GoodInterpreter.Num(3)),
            new GoodInterpreter.Num(2));
        assertThat(ast.interpret()).isEqualTo(6);
        assertThat(ast.show()).isEqualTo("((5 + 3) - 2)");
    }

    // ─── bad: ad-hoc парсинг ограничен ───
    @Test
    void adHocParserHandlesPlusMinusOnly() {
        assertThat(BadInterpreter.eval("5 + 3 - 2")).isEqualTo(6);
        assertThat(BadInterpreter.eval("10 - 4")).isEqualTo(6);
        assertThatThrownBy(() -> BadInterpreter.eval("2 + 3 * 4"))
                .isInstanceOf(IllegalArgumentException.class); // умножение не поддержано
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunBuildsAst() {
        PatternDemoResponse r = new InterpreterDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(5); // Num,Num,Add,Num,Sub
        assertThat(r.getVerdict()).startsWith("PASS").contains("6");
    }

    @Test
    void badRunIsAdHoc() {
        PatternDemoResponse r = new InterpreterDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getVerdict()).startsWith("FAIL");
    }
}
