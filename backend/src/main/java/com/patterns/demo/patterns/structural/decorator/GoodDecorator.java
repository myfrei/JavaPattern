package com.patterns.demo.patterns.structural.decorator;

/**
 * Хорошая реализация Decorator.
 *
 * Базовый напиток оборачивается слоями-декораторами. Каждый декоратор реализует
 * тот же интерфейс Beverage и добавляет к результату вложенного объекта свою
 * цену и подпись. Любая комбинация добавок собирается во время выполнения без
 * новых классов.
 */
public final class GoodDecorator {

    public interface Beverage {
        int cost();
        String describe();
    }

    public static final class Espresso implements Beverage {
        public int cost() { return 80; }
        public String describe() { return "espresso"; }
    }

    /** Базовый декоратор хранит вложенный напиток. */
    public abstract static class Decorator implements Beverage {
        protected final Beverage inner;
        protected Decorator(Beverage inner) { this.inner = inner; }
    }

    public static final class Milk extends Decorator {
        public Milk(Beverage inner) { super(inner); }
        public int cost() { return inner.cost() + 20; }
        public String describe() { return inner.describe() + ", milk"; }
    }

    public static final class Sugar extends Decorator {
        public Sugar(Beverage inner) { super(inner); }
        public int cost() { return inner.cost() + 10; }
        public String describe() { return inner.describe() + ", sugar"; }
    }

    private GoodDecorator() {}
}
