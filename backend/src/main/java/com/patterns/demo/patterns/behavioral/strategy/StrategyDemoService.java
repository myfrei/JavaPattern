package com.patterns.demo.patterns.behavioral.strategy;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

/**
 * Считает итог корзины 1000 с тремя видами скидки: через взаимозаменяемые
 * стратегии (good) и через switch по типу (bad). Отдаёт отчёт для визуализации
 * «слот стратегии в контексте».
 */
@Service
public class StrategyDemoService {

    private static final int TOTAL = 1000;

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Strategy — взаимозаменяемые алгоритмы");
        r.setDescription(
            "Cart хранит стратегию скидки и подменяет её в рантайме. На сумме 1000 подставляем " +
            "NoDiscount, Percent(10%) и Coupon(-150) — корзина не меняется, меняется только стратегия.");
        r.setCode(GOOD_CODE);

        GoodStrategy.Cart cart = new GoodStrategy.Cart();
        GoodStrategy.Discount[] strategies = {
            new GoodStrategy.NoDiscount(),
            new GoodStrategy.Percent(10),
            new GoodStrategy.Coupon(150),
        };

        long t = 0;
        for (GoodStrategy.Discount s : strategies) {
            cart.setDiscount(s);
            int result = cart.checkout(TOTAL);
            r.getInstances().add(new PatternDemoResponse.InstanceInfo(s.name(), "strategy"));
            r.getSteps().add(new PatternDemoResponse.Step(t++, s.name(), "checkout(1000)", "= " + result, true));
        }

        r.setVerdict("PASS — 3 стратегии скидки взаимозаменяемы");
        r.setExplanation(
            "Алгоритм инкапсулирован в стратегии и выбирается композицией. Добавить вид скидки = новый класс " +
            "Discount, без правки Cart и без единого if по типу.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Strategy — антипаттерн: switch по типу скидки");
        r.setDescription(
            "Все формулы скидок свалены в один метод checkout(total, type, param) со switch. Тот же " +
            "результат, но новый вид скидки = править этот метод (и каждую его копию).");
        r.setCode(BAD_CODE);

        String[][] cases = {{"none", "0"}, {"percent", "10"}, {"coupon", "150"}};
        long t = 0;
        for (String[] c : cases) {
            int result = BadStrategy.checkout(TOTAL, c[0], Integer.parseInt(c[1]));
            r.getInstances().add(new PatternDemoResponse.InstanceInfo(c[0], "switch case"));
            r.getSteps().add(new PatternDemoResponse.Step(t++, c[0], "switch(\"" + c[0] + "\")", "= " + result, false));
        }

        r.setVerdict("FAIL — выбор алгоритма зашит в switch");
        r.setExplanation(
            "Все варианты живут в одном методе: ветки растут, тестировать в изоляции нечего, дублирование " +
            "switch расползается по коду. Strategy выносит каждый алгоритм в свой класс.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Strategy");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            interface Discount { int apply(int total); }

            class Percent implements Discount {
                public int apply(int total) { return total - total * pct / 100; }
            }
            class Coupon implements Discount {
                public int apply(int total) { return Math.max(0, total - amount); }
            }

            cart.setDiscount(new Percent(10));   // подмена в рантайме
            cart.checkout(1000);                 // 900
            """;

    private static final String BAD_CODE = """
            // Выбор алгоритма зашит в switch по типу
            int checkout(int total, String type, int param) {
                switch (type) {
                    case "none":    return total;
                    case "percent": return total - total * param / 100;
                    case "coupon":  return Math.max(0, total - param);
                    // новая скидка? ещё одна ветка здесь
                }
            }
            """;
}
