package com.patterns.demo.patterns.creational.builder;

/**
 * Антипаттерн вместо Builder — телескопический конструктор.
 *
 * Пять позиционных параметров, половина из них — булевы флаги. Вызов
 * new Pizza("L", "тонкое", true, false, true) нечитаем: что есть что, видно
 * только по сигнатуре. Перепутать порядок флагов — поймать тихий баг, который
 * компилятор не заметит.
 */
public final class BadBuilder {

    public static final class Pizza {
        private final String size;
        private final String crust;
        private final boolean cheese;
        private final boolean ham;
        private final boolean mushroom;

        public Pizza(String size, String crust, boolean cheese, boolean ham, boolean mushroom) {
            this.size = size;
            this.crust = crust;
            this.cheese = cheese;
            this.ham = ham;
            this.mushroom = mushroom;
        }

        public boolean ham() { return ham; }
        public boolean mushroom() { return mushroom; }

        public String describe() {
            return size + " / " + crust + (cheese ? " / cheese" : "")
                    + (ham ? " + ветчина" : "") + (mushroom ? " + грибы" : "");
        }
    }

    private BadBuilder() {}
}
