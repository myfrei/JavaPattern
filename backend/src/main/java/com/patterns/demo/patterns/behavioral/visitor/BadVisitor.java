package com.patterns.demo.patterns.behavioral.visitor;

/**
 * Антипаттерн вместо Visitor.
 *
 * Операция реализована лестницей instanceof по типам фигур. Каждая новая фигура
 * требует добавить ветку в КАЖДУЮ такую операцию, а пропуск типа всплывёт только
 * в рантайме.
 */
public final class BadVisitor {

    public static int area(Object shape) {
        if (shape instanceof GoodVisitor.Circle c) {
            return (int) Math.round(Math.PI * c.radius * c.radius);
        } else if (shape instanceof GoodVisitor.Square s) {
            return s.side * s.side;
        } else {
            throw new IllegalArgumentException("неизвестная фигура: " + shape);
        }
    }

    private BadVisitor() {}
}
