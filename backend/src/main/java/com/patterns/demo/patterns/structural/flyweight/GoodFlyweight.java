package com.patterns.demo.patterns.structural.flyweight;

import java.util.HashMap;
import java.util.Map;

/**
 * Хорошая реализация Flyweight.
 *
 * Тяжёлое внутреннее (intrinsic) состояние типа дерева — название, цвет,
 * текстура — выносится в общий объект TreeType, который раздаёт фабрика-пул.
 * Тысячи деревьев хранят лишь координаты (extrinsic) и ссылку на общий тип.
 */
public final class GoodFlyweight {

    /** Flyweight: общее тяжёлое состояние, переиспользуется многими деревьями. */
    public static final class TreeType {
        private final String name;
        private final String color;
        TreeType(String name, String color) { this.name = name; this.color = color; }
        public String name() { return name; }
        public String color() { return color; }
    }

    /** Пул flyweight'ов: одинаковые типы возвращаются как один объект. */
    public static final class TreeFactory {
        private final Map<String, TreeType> pool = new HashMap<>();

        public TreeType get(String name, String color) {
            return pool.computeIfAbsent(name + "/" + color, k -> new TreeType(name, color));
        }

        public int distinctTypes() { return pool.size(); }
    }

    /** Дерево хранит только своё (extrinsic) состояние + ссылку на общий тип. */
    public record Tree(int x, int y, TreeType type) {}

    private GoodFlyweight() {}
}
