package com.patterns.demo.patterns.creational.builder;

import java.util.ArrayList;
import java.util.List;

/**
 * Хорошая реализация Builder.
 *
 * Сложный объект (пицца) собирается пошагово через текучий интерфейс. Каждый
 * шаг назван — порядок и смысл аргументов очевидны. build() валидирует
 * обязательные поля и отдаёт иммутабельный объект. Опциональные части не
 * требуют десятка перегруженных конструкторов.
 */
public final class GoodBuilder {

    public static final class Pizza {
        private final String size;
        private final String crust;
        private final boolean cheese;
        private final List<String> toppings;

        private Pizza(Builder b) {
            this.size = b.size;
            this.crust = b.crust;
            this.cheese = b.cheese;
            this.toppings = List.copyOf(b.toppings); // иммутабельно
        }

        public String size() { return size; }
        public List<String> toppings() { return toppings; }

        public String describe() {
            return size + " / " + crust + (cheese ? " / cheese" : "")
                    + (toppings.isEmpty() ? "" : " + " + String.join(", ", toppings));
        }

        public static Builder builder() { return new Builder(); }

        public static final class Builder {
            private String size;                 // обязательное поле
            private String crust = "classic";
            private boolean cheese = false;
            private final List<String> toppings = new ArrayList<>();

            public Builder size(String s)    { this.size = s; return this; }
            public Builder crust(String c)   { this.crust = c; return this; }
            public Builder cheese(boolean v) { this.cheese = v; return this; }
            public Builder topping(String t) { this.toppings.add(t); return this; }

            public Pizza build() {
                if (size == null || size.isBlank()) {
                    throw new IllegalStateException("size обязателен");
                }
                return new Pizza(this);
            }
        }
    }

    private GoodBuilder() {}
}
