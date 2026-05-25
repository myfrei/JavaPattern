package com.patterns.demo.patterns.behavioral.interpreter;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

/**
 * Вычисляет «5 + 3 - 2»: через AST из классов выражений (good) и через ad-hoc
 * парсинг строки (bad). Отдаёт отчёт для визуализации дерева/последовательности
 * вычисления.
 */
@Service
public class InterpreterDemoService {

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Interpreter — грамматика в классах, AST и рекурсия");
        r.setDescription(
            "Выражение 5 + 3 - 2 представлено деревом Sub(Add(5, 3), 2). interpret() вычисляет его " +
            "рекурсивно снизу вверх. Новая операция (умножение, скобки) = новый класс выражения.");
        r.setCode(GOOD_CODE);

        GoodInterpreter.Expr ast = new GoodInterpreter.Sub(
            new GoodInterpreter.Add(new GoodInterpreter.Num(5), new GoodInterpreter.Num(3)),
            new GoodInterpreter.Num(2));
        int result = ast.interpret();

        long t = 0;
        addNode(r, "Num(5)", "5");
        r.getSteps().add(new PatternDemoResponse.Step(t++, "Num(5)", "interpret()", "= 5", true));
        addNode(r, "Num(3)", "3");
        r.getSteps().add(new PatternDemoResponse.Step(t++, "Num(3)", "interpret()", "= 3", true));
        addNode(r, "Add", "5 + 3 = 8");
        r.getSteps().add(new PatternDemoResponse.Step(t++, "Add", "5 + 3", "= 8", true));
        addNode(r, "Num(2)", "2");
        r.getSteps().add(new PatternDemoResponse.Step(t++, "Num(2)", "interpret()", "= 2", true));
        addNode(r, "Sub", "8 - 2 = " + result);
        r.getSteps().add(new PatternDemoResponse.Step(t, "Sub", "8 - 2", "= " + result, true));

        r.setVerdict("PASS — " + ast.show() + " = " + result);
        r.setExplanation(
            "Грамматика выражена классами Num/Add/Sub, а дерево вычисляется рекурсивно. Приоритеты и " +
            "вложенность задаются структурой AST; новая операция — это новый класс Expr.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Interpreter — антипаттерн: ad-hoc разбор строки");
        r.setDescription(
            "Та же сумма считается split'ом по пробелам слева направо. На «5 + 3 - 2» совпадает (6), но " +
            "нет приоритетов и вложенности: умножение или скобки парсер не осилит без переписывания.");
        r.setCode(BAD_CODE);

        int result = BadInterpreter.eval("5 + 3 - 2");
        boolean breaksOnMul = brokenOnMultiply();

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("\"5 + 3 - 2\"", "split & loop"));

        long t = 0;
        r.getSteps().add(new PatternDemoResponse.Step(t++, "\"5 + 3 - 2\"", "split(\" \")", "[5, +, 3, -, 2]", false));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "loop", "acc слева направо", "= " + result, false));
        r.getSteps().add(new PatternDemoResponse.Step(t, "\"2 + 3 * 4\"", "eval(*)", breaksOnMul ? "падает: оператор не поддержан ✗" : "—", false));

        r.setVerdict("FAIL — ad-hoc парсинг: нет приоритетов и вложенности");
        r.setExplanation(
            "Линейный проход по токенам не знает приоритетов и скобок: добавить умножение = переписать " +
            "разбор. Interpreter выносит грамматику в классы и строит расширяемое дерево.");
        return r;
    }

    private boolean brokenOnMultiply() {
        try {
            BadInterpreter.eval("2 + 3 * 4");
            return false;
        } catch (RuntimeException e) {
            return true; // не поддерживает умножение
        }
    }

    private void addNode(PatternDemoResponse r, String label, String detail) {
        r.getInstances().add(new PatternDemoResponse.InstanceInfo(label, detail));
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Interpreter");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            interface Expr { int interpret(); }

            record Num(int v) implements Expr { public int interpret() { return v; } }
            record Add(Expr l, Expr r) implements Expr {
                public int interpret() { return l.interpret() + r.interpret(); }
            }
            record Sub(Expr l, Expr r) implements Expr {
                public int interpret() { return l.interpret() - r.interpret(); }
            }

            Expr ast = new Sub(new Add(new Num(5), new Num(3)), new Num(2));
            ast.interpret();   // 6, рекурсия по дереву
            """;

    private static final String BAD_CODE = """
            // Ad-hoc разбор: split и проход слева направо
            int eval(String expr) {
                String[] tok = expr.split(" ");
                int acc = parseInt(tok[0]);
                for (int i = 1; i + 1 < tok.length; i += 2) {
                    if (tok[i].equals("+")) acc += parseInt(tok[i+1]);
                    else if (tok[i].equals("-")) acc -= parseInt(tok[i+1]);
                }
                return acc;   // нет приоритетов, нет скобок, "*" не осилит
            }
            """;
}
