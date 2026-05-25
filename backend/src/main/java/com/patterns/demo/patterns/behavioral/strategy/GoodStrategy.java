package com.patterns.demo.patterns.behavioral.strategy;

/**
 * Хорошая реализация Strategy.
 *
 * Алгоритм расчёта скидки вынесен в отдельный объект-стратегию за общим
 * интерфейсом. Cart хранит стратегию и подменяет её в рантайме. Новый вид скидки
 * = новый класс, корзина не меняется.
 */
public final class GoodStrategy {

    public interface Discount {
        int apply(int total);
        String name();
    }

    public static final class NoDiscount implements Discount {
        public int apply(int total) { return total; }
        public String name() { return "NoDiscount"; }
    }

    public static final class Percent implements Discount {
        private final int pct;
        public Percent(int pct) { this.pct = pct; }
        public int apply(int total) { return total - total * pct / 100; }
        public String name() { return "Percent(" + pct + "%)"; }
    }

    public static final class Coupon implements Discount {
        private final int amount;
        public Coupon(int amount) { this.amount = amount; }
        public int apply(int total) { return Math.max(0, total - amount); }
        public String name() { return "Coupon(-" + amount + ")"; }
    }

    public static final class Cart {
        private Discount discount = new NoDiscount();
        public void setDiscount(Discount d) { this.discount = d; } // подмена в рантайме
        public int checkout(int total) { return discount.apply(total); }
        public String strategy() { return discount.name(); }
    }

    private GoodStrategy() {}
}
