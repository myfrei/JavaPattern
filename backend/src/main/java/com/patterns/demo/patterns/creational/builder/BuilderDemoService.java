package com.patterns.demo.patterns.creational.builder;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

/**
 * Собирает одну и ту же пиццу: пошагово через Builder (good) и через
 * телескопический конструктор с перепутанными флагами (bad). Отдаёт
 * пошаговый отчёт для визуализации «сборки объекта».
 */
@Service
public class BuilderDemoService {

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Builder — пошаговая сборка через текучий интерфейс");
        r.setDescription(
            "Пицца собирается именованными шагами: size → crust → cheese → topping. " +
            "Порядок и смысл очевидны, build() валидирует обязательное поле и отдаёт " +
            "иммутабельный объект.");
        r.setCode(GOOD_CODE);

        // реально собираем объект текучим интерфейсом
        GoodBuilder.Pizza pizza = GoodBuilder.Pizza.builder()
                .size("L")
                .crust("тонкое")
                .cheese(true)
                .topping("ветчина")
                .topping("грибы")
                .build();

        String[][] parts = {
            {".size(\"L\")",        "size = L"},
            {".crust(\"тонкое\")",  "crust = тонкое"},
            {".cheese(true)",       "cheese = да"},
            {".topping(\"ветчина\")", "+ ветчина"},
            {".topping(\"грибы\")",   "+ грибы"},
        };
        long t = 0;
        for (String[] p : parts) {
            r.getInstances().add(new PatternDemoResponse.InstanceInfo(p[1], p[0]));
            r.getSteps().add(new PatternDemoResponse.Step(t++, p[1], p[0], "ok", true));
        }
        r.getSteps().add(new PatternDemoResponse.Step(
            t, "Pizza", "build()", pizza.describe(), true));

        r.setVerdict("PASS — объект собран по шагам: " + pizza.describe());
        r.setExplanation(
            "Каждый шаг назван, поэтому код читается как описание. build() гарантирует " +
            "валидность и отдаёт иммутабельный объект. Опциональные начинки не плодят " +
            "перегруженных конструкторов.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Builder — антипаттерн: телескопический конструктор");
        r.setDescription(
            "Та же пицца, но через new Pizza(size, crust, cheese, ham, mushroom). Клиент " +
            "хотел ветчину, но перепутал порядок двух булевых флагов — и получил грибы. " +
            "Компилятор молчит.");
        r.setCode(BAD_CODE);

        // клиент хотел ham=true, mushroom=false, но перепутал местами флаги
        BadBuilder.Pizza wrong = new BadBuilder.Pizza("L", "тонкое", true, false, true);

        String[][] args = {
            {"arg1 · size",     "\"L\""},
            {"arg2 · crust",    "\"тонкое\""},
            {"arg3 · cheese",   "true"},
            {"arg4 · ham",      "false  ← хотели true"},
            {"arg5 · mushroom", "true   ← хотели false"},
        };
        long t = 0;
        for (String[] a : args) {
            r.getInstances().add(new PatternDemoResponse.InstanceInfo(a[1], a[0]));
            r.getSteps().add(new PatternDemoResponse.Step(t++, a[1], a[0], "позиционный аргумент", true));
        }
        r.getSteps().add(new PatternDemoResponse.Step(
            t, "Pizza", "new Pizza(...)", wrong.describe() + "  (грибы вместо ветчины)", false));

        r.setVerdict("FAIL — перепутан порядок флагов: " + wrong.describe());
        r.setExplanation(
            "Позиционные аргументы (особенно булевы) нечитаемы и легко переставляются местами. " +
            "Builder убирает проблему: шаги названы, опциональные поля задаются явно.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Builder");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            Pizza pizza = Pizza.builder()
                .size("L")
                .crust("тонкое")
                .cheese(true)
                .topping("ветчина")
                .topping("грибы")
                .build();              // валидирует size и отдаёт иммутабельный объект

            // в Builder:
            public Pizza build() {
                if (size == null) throw new IllegalStateException("size обязателен");
                return new Pizza(this);
            }
            """;

    private static final String BAD_CODE = """
            // Телескопический конструктор: что есть что — видно только по сигнатуре
            Pizza pizza = new Pizza("L", "тонкое", true, false, true);
            //                                      ^cheese ^ham  ^mushroom
            // хотели ham=true, mushroom=false — но переставили флаги местами.
            // Компилируется, тихо отдаёт не ту пиццу.
            """;
}
