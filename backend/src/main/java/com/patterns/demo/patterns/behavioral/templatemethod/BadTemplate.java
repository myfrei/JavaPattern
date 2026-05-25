package com.patterns.demo.patterns.behavioral.templatemethod;

import java.util.List;

/**
 * Антипаттерн вместо Template Method.
 *
 * Каждый напиток переписывает весь алгоритм целиком. Скелет дублируется, и при
 * копировании легко потерять шаг: Coffee здесь забыл вскипятить воду. Инвариант
 * «общая последовательность» нигде не зафиксирован.
 */
public final class BadTemplate {

    public static final class Tea {
        public List<String> prepare() {
            return List.of("boil water", "steep tea", "pour into cup", "add lemon");
        }
    }

    public static final class Coffee {
        public List<String> prepare() {
            // забыли "boil water" — при копипасте шаг потерялся
            return List.of("brew coffee", "pour into cup", "add sugar");
        }
    }

    private BadTemplate() {}
}
