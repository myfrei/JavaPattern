package com.patterns.demo.patterns.structural.decorator;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

/**
 * Собирает «espresso + milk + sugar»: слоями-декораторами (good) и отдельным
 * классом-комбинацией (bad). Отдаёт отчёт для визуализации «обёртки вокруг ядра».
 */
@Service
public class DecoratorDemoService {

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Decorator — слои-обёртки вокруг базового объекта");
        r.setDescription(
            "Espresso оборачивается в Milk, затем в Sugar. Каждый слой добавляет цену и подпись " +
            "к результату вложенного объекта. Комбинация собирается в рантайме без новых классов.");
        r.setCode(GOOD_CODE);

        GoodDecorator.Beverage drink = new GoodDecorator.Sugar(new GoodDecorator.Milk(new GoodDecorator.Espresso()));

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Espresso", "80"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("+ Milk", "+20"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("+ Sugar", "+10"));

        long t = 0;
        r.getSteps().add(new PatternDemoResponse.Step(t++, "Espresso", "base", "cost = 80", true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "+ Milk", "wrap", "cost = 100", true));
        r.getSteps().add(new PatternDemoResponse.Step(t, "+ Sugar", "wrap", "cost = " + drink.cost() + " · " + drink.describe(), true));

        r.setVerdict("PASS — " + drink.describe() + " = " + drink.cost());
        r.setExplanation(
            "Каждый декоратор реализует Beverage и делегирует вложенному объекту, добавляя своё. " +
            "Любой набор и порядок добавок собирается обёртками — без класса на каждую комбинацию.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Decorator — антипаттерн: класс на каждую комбинацию");
        r.setDescription(
            "Без декораторов заводят EspressoWithMilk, EspressoWithSugar, EspressoWithMilkAndSugar… " +
            "Каждая новая добавка удваивает число классов (рост как 2^n).");
        r.setCode(BAD_CODE);

        BadDecorator.EspressoWithMilkAndSugar drink = new BadDecorator.EspressoWithMilkAndSugar();

        String[] classes = {"Espresso", "EspressoWithMilk", "EspressoWithSugar", "EspressoWithMilkAndSugar"};
        long t = 0;
        for (String c : classes) {
            r.getInstances().add(new PatternDemoResponse.InstanceInfo(c, "класс-комбинация"));
            r.getSteps().add(new PatternDemoResponse.Step(t++, c, "отдельный класс", "ручная комбинация", false));
        }

        r.setVerdict("FAIL — " + BadDecorator.classCount() + " класса на 2 добавки (рост 2^n): " + drink.describe());
        r.setExplanation(
            "Комбинации добавок не переиспользуются: каждая — свой класс. Третья добавка превратит " +
            "4 класса в 8. Decorator заменяет это композицией обёрток в рантайме.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Decorator");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            interface Beverage { int cost(); String describe(); }

            class Espresso implements Beverage {
                public int cost() { return 80; }
            }

            abstract class Decorator implements Beverage {
                protected final Beverage inner;          // вложенный объект
            }
            class Milk extends Decorator {
                public int cost() { return inner.cost() + 20; }   // добавляет к вложенному
            }

            Beverage drink = new Sugar(new Milk(new Espresso()));
            drink.cost();      // 110, собрано обёртками в рантайме
            """;

    private static final String BAD_CODE = """
            // Класс на каждую комбинацию добавок — рост как 2^n
            class Espresso { ... }
            class EspressoWithMilk { ... }
            class EspressoWithSugar { ... }
            class EspressoWithMilkAndSugar { ... }
            // добавили сироп? удваиваем число классов
            """;
}
