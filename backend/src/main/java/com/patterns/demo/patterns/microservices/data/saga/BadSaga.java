package com.patterns.demo.patterns.microservices.data.saga;

import java.util.ArrayList;
import java.util.List;

/**
 * Антипаттерн вместо Saga.
 *
 * Шаги распределённой операции просто выполняются по очереди без компенсаций.
 * Если поздний шаг падает, ранние уже закоммичены и остаются — система в
 * рассинхроне (деньги списаны, заказ создан, но товара нет).
 */
public final class BadSaga {

    public static List<String> run(List<GoodSaga.Step> steps) {
        List<String> trace = new ArrayList<>();
        for (GoodSaga.Step s : steps) {
            if (s.ok()) {
                trace.add("✓ " + s.name());
            } else {
                trace.add("✗ " + s.name() + " FAILED — без компенсаций");
                return trace; // ранние шаги остаются закоммиченными
            }
        }
        return trace;
    }

    private BadSaga() {}
}
