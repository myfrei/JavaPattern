package com.patterns.demo.patterns.behavioral.interpreter;

/**
 * Антипаттерн вместо Interpreter.
 *
 * Выражение разбирается «на коленке»: split по пробелам и проход слева направо.
 * Нет ни приоритетов операций, ни вложенности (скобок), ни расширяемости —
 * добавить умножение или скобки можно только переписав весь парсер.
 */
public final class BadInterpreter {

    public static int eval(String expr) {
        String[] tok = expr.trim().split("\\s+");
        int acc = Integer.parseInt(tok[0]);
        for (int i = 1; i + 1 < tok.length; i += 2) {
            String op = tok[i];
            int n = Integer.parseInt(tok[i + 1]);
            switch (op) {
                case "+": acc += n; break;
                case "-": acc -= n; break;
                default: throw new IllegalArgumentException("оператор не поддержан: " + op);
            }
        }
        return acc; // нет приоритетов и вложенности
    }

    private BadInterpreter() {}
}
