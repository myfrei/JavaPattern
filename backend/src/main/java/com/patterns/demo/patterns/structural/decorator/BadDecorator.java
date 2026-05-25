package com.patterns.demo.patterns.structural.decorator;

/**
 * Антипаттерн вместо Decorator.
 *
 * Без декораторов под каждую комбинацию добавок заводят отдельный подкласс.
 * Число классов растёт как степень двойки от количества опций — и каждая новая
 * добавка удваивает иерархию.
 */
public final class BadDecorator {

    public static final class Espresso { public int cost() { return 80; } public String describe() { return "espresso"; } }
    public static final class EspressoWithMilk { public int cost() { return 100; } public String describe() { return "espresso, milk"; } }
    public static final class EspressoWithSugar { public int cost() { return 90; } public String describe() { return "espresso, sugar"; } }
    public static final class EspressoWithMilkAndSugar { public int cost() { return 110; } public String describe() { return "espresso, milk, sugar"; } }

    /** 2 добавки → 4 класса; 3 добавки → 8; рост как 2^n. */
    public static int classCount() { return 4; }

    private BadDecorator() {}
}
