package com.patterns.demo.patterns.behavioral.visitor;

/**
 * Хорошая реализация Visitor.
 *
 * Операция над иерархией фигур вынесена в отдельный объект-посетитель. Элементы
 * через accept() выбирают нужный метод посетителя (двойная диспетчеризация).
 * Новая операция = новый Visitor без правки классов фигур и без instanceof.
 */
public final class GoodVisitor {

    public interface Shape {
        <R> R accept(Visitor<R> v);
    }

    public interface Visitor<R> {
        R visitCircle(Circle c);
        R visitSquare(Square s);
    }

    public static final class Circle implements Shape {
        public final int radius;
        public Circle(int radius) { this.radius = radius; }
        public <R> R accept(Visitor<R> v) { return v.visitCircle(this); }
    }

    public static final class Square implements Shape {
        public final int side;
        public Square(int side) { this.side = side; }
        public <R> R accept(Visitor<R> v) { return v.visitSquare(this); }
    }

    /** Операция №1: площадь. */
    public static final class AreaVisitor implements Visitor<Integer> {
        public Integer visitCircle(Circle c) { return (int) Math.round(Math.PI * c.radius * c.radius); }
        public Integer visitSquare(Square s) { return s.side * s.side; }
    }

    /** Операция №2: описание — добавлена без правки фигур. */
    public static final class NameVisitor implements Visitor<String> {
        public String visitCircle(Circle c) { return "Circle(r=" + c.radius + ")"; }
        public String visitSquare(Square s) { return "Square(side=" + s.side + ")"; }
    }

    private GoodVisitor() {}
}
