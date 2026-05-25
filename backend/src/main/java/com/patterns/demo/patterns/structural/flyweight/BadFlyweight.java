package com.patterns.demo.patterns.structural.flyweight;

/**
 * Антипаттерн вместо Flyweight.
 *
 * Каждое дерево хранит ПОЛНУЮ копию тяжёлого состояния типа (название, цвет,
 * текстуру-блоб). Тысяча одинаковых дубов = тысяча копий одних и тех же данных,
 * память течёт линейно по числу объектов.
 */
public final class BadFlyweight {

    /** Тяжёлое состояние — в норме его делят, но здесь оно у каждого своё. */
    public static final class HeavyType {
        final String name;
        final String color;
        final String texture;
        HeavyType(String name, String color) {
            this.name = name;
            this.color = color;
            this.texture = "texture-blob-256kb"; // имитация тяжёлых данных
        }
    }

    public static final class Tree {
        final int x;
        final int y;
        private final HeavyType type;

        public Tree(int x, int y, String name, String color) {
            this.x = x;
            this.y = y;
            this.type = new HeavyType(name, color); // своя копия на КАЖДОЕ дерево
        }

        public HeavyType type() { return type; }
    }

    private BadFlyweight() {}
}
