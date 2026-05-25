package com.patterns.demo.patterns.behavioral.strategy;

/**
 * Антипаттерн вместо Strategy.
 *
 * Все варианты расчёта зашиты в один метод с switch по типу скидки. Новый вид
 * скидки = ещё одна ветка в этом методе (и во всех его копиях по коду).
 */
public final class BadStrategy {

    public static int checkout(int total, String type, int param) {
        switch (type) {                                  // выбор алгоритма зашит
            case "none":    return total;
            case "percent": return total - total * param / 100;
            case "coupon":  return Math.max(0, total - param);
            default: throw new IllegalArgumentException("неизвестная скидка: " + type);
        }
    }

    private BadStrategy() {}
}
