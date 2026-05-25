package com.patterns.demo.patterns.behavioral.interpreter;

/**
 * Хорошая реализация Interpreter.
 *
 * Каждое грамматическое правило — отдельный класс выражения с методом interpret().
 * Из них собирается дерево разбора (AST), которое вычисляется рекурсией. Грамматика
 * расширяется новыми классами (умножение, скобки) без переписывания парсера.
 */
public final class GoodInterpreter {

    public interface Expr {
        int interpret();
        String show();
    }

    public static final class Num implements Expr {
        private final int value;
        public Num(int value) { this.value = value; }
        public int interpret() { return value; }
        public String show() { return String.valueOf(value); }
    }

    public static final class Add implements Expr {
        private final Expr left, right;
        public Add(Expr left, Expr right) { this.left = left; this.right = right; }
        public int interpret() { return left.interpret() + right.interpret(); }
        public String show() { return "(" + left.show() + " + " + right.show() + ")"; }
    }

    public static final class Sub implements Expr {
        private final Expr left, right;
        public Sub(Expr left, Expr right) { this.left = left; this.right = right; }
        public int interpret() { return left.interpret() - right.interpret(); }
        public String show() { return "(" + left.show() + " - " + right.show() + ")"; }
    }

    private GoodInterpreter() {}
}
