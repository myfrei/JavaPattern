package com.patterns.demo.patterns.structural.flyweight;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

/**
 * Сажает лес из 5 деревьев двух видов: с общим пулом типов (good) и с копией
 * тяжёлых данных в каждом дереве (bad). Отдаёт отчёт для визуализации «пул
 * общих объектов против множества тяжёлых».
 */
@Service
public class FlyweightDemoService {

    private static final String[][] FOREST = {
        {"T1", "oak", "green"},
        {"T2", "oak", "green"},
        {"T3", "pine", "dark"},
        {"T4", "oak", "green"},
        {"T5", "pine", "dark"},
    };

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Flyweight — общий пул тяжёлых типов");
        r.setDescription(
            "Фабрика раздаёт общий TreeType: одинаковые виды деревьев получают один и тот же объект. " +
            "5 деревьев двух видов используют всего 2 общих flyweight'а.");
        r.setCode(GOOD_CODE);

        GoodFlyweight.TreeFactory factory = new GoodFlyweight.TreeFactory();
        java.util.Map<String, Integer> typeIndex = new java.util.LinkedHashMap<>();
        long t = 0;
        for (String[] node : FOREST) {
            int before = factory.distinctTypes();
            GoodFlyweight.TreeType type = factory.get(node[1], node[2]);
            String key = type.name() + "/" + type.color();
            boolean created = factory.distinctTypes() > before;
            if (created) typeIndex.put(key, typeIndex.size() + 1);
            int idx = typeIndex.get(key);             // стабильный номер конкретного flyweight'а
            r.getInstances().add(new PatternDemoResponse.InstanceInfo(node[0], key));
            r.getSteps().add(new PatternDemoResponse.Step(
                t++, node[0], "factory.get(" + node[1] + ")",
                (created ? "new flyweight #" : "reused #") + idx, true));
        }

        r.setVerdict("PASS — 5 деревьев, " + factory.distinctTypes() + " общих объекта-типа");
        r.setExplanation(
            "Тяжёлое состояние вынесено в общий TreeType и переиспользуется через пул. Память не зависит " +
            "от числа деревьев — только от числа различных видов. Координаты остаются у каждого дерева.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Flyweight — антипаттерн: тяжёлые данные в каждом объекте");
        r.setDescription(
            "Без пула каждое дерево создаёт собственную копию тяжёлого типа. 5 деревьев = 5 копий " +
            "одних и тех же данных; на тысячах деревьев память течёт линейно.");
        r.setCode(BAD_CODE);

        long t = 0;
        for (String[] node : FOREST) {
            new BadFlyweight.Tree(0, 0, node[1], node[2]); // своя копия типа
            r.getInstances().add(new PatternDemoResponse.InstanceInfo(node[0], node[1] + "/" + node[2]));
            r.getSteps().add(new PatternDemoResponse.Step(
                t++, node[0], "new HeavyType(" + node[1] + ")", "своя копия тяжёлых данных", false));
        }

        r.setVerdict("FAIL — 5 деревьев = 5 копий тяжёлого типа");
        r.setExplanation(
            "Внутреннее состояние не делится: одинаковые дубы держат одинаковые данные по отдельной копии. " +
            "Flyweight выносит это в общий объект и раздаёт через пул.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Flyweight");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            class TreeType { final String name, color; }   // тяжёлое общее состояние

            class TreeFactory {
                private final Map<String, TreeType> pool = new HashMap<>();
                TreeType get(String name, String color) {
                    return pool.computeIfAbsent(name + "/" + color,
                                                k -> new TreeType(name, color)); // общий объект
                }
            }

            record Tree(int x, int y, TreeType type) {}      // только координаты + ссылка
            // 5 деревьев двух видов → 2 общих flyweight'а
            """;

    private static final String BAD_CODE = """
            // Каждое дерево хранит СВОЮ копию тяжёлого состояния
            class Tree {
                final int x, y;
                final HeavyType type;                        // texture-blob-256kb на КАЖДОЕ
                Tree(int x, int y, String name, String color) {
                    this.type = new HeavyType(name, color);  // новая копия всегда
                }
            }
            // 1000 одинаковых дубов → 1000 копий одних и тех же данных
            """;
}
