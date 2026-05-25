package com.patterns.demo.patterns.creational.prototype;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Клонирует документ-шаблон и правит клон: при глубоком копировании (good)
 * оригинал цел, при поверхностном (bad) — оригинал портится через общий список.
 * Собирает пошаговый отчёт для визуализации «оригинал ↔ клон».
 */
@Service
public class PrototypeDemoService {

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Prototype — глубокое копирование, клон независим");
        r.setDescription(
            "Документ-шаблон с тегами [draft] клонируется через copy(). copy() создаёт новый " +
            "список тегов, поэтому правки клона не трогают оригинал.");
        r.setCode(GOOD_CODE);

        GoodPrototype.Document original = new GoodPrototype.Document("Шаблон", List.of("draft"));
        GoodPrototype.Document clone = original.copy();
        clone.setTitle("Копия");
        clone.addTag("v2");

        long t = 0;
        r.getSteps().add(new PatternDemoResponse.Step(t++, "Original", "new Document(tags=[draft])", "создан", true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "Clone", "original.copy()", "новый список тегов", true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "Clone", "addTag(\"v2\")", "tags=" + clone.tags(), true));
        boolean independent = !original.tags().contains("v2");
        r.getSteps().add(new PatternDemoResponse.Step(t, "Original", "проверка", "tags=" + original.tags()
                + (independent ? " — не задет ✓" : " — испорчен ✗"), independent));

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Original", "tags=" + original.tags()));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Clone", "tags=" + clone.tags()));

        r.setVerdict(independent ? "PASS — клон независим" : "FAIL — оригинал задет");
        r.setExplanation(
            "copy() скопировал вложенный список, поэтому оригинал и клон не делят состояние. " +
            "Добавление тега в клон не повлияло на оригинал.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Prototype — антипаттерн: поверхностная копия");
        r.setDescription(
            "Тот же сценарий, но copy() переиспользует ту же ссылку на список тегов. " +
            "Оригинал и клон делят один список — правка клона тихо портит оригинал.");
        r.setCode(BAD_CODE);

        BadPrototype.Document original = new BadPrototype.Document("Шаблон", new ArrayList<>(List.of("draft")));
        BadPrototype.Document clone = original.copy();
        clone.setTitle("Копия");
        clone.addTag("v2");

        boolean shared = original.tags() == clone.tags();
        boolean corrupted = original.tags().contains("v2");

        long t = 0;
        r.getSteps().add(new PatternDemoResponse.Step(t++, "Original", "new Document(tags=[draft])", "создан", true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "Clone", "original.copy()", "общий список (shallow)", false));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "Clone", "addTag(\"v2\")", "tags=" + clone.tags(), false));
        r.getSteps().add(new PatternDemoResponse.Step(t, "Original", "проверка", "tags=" + original.tags()
                + (corrupted ? " — испорчен ✗" : " — не задет"), !corrupted));

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Original", "tags=" + original.tags()));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Clone", "tags=" + clone.tags()));

        r.setVerdict(corrupted ? "FAIL — оригинал испорчен общим списком" : "PASS");
        r.setExplanation(
            "copy() вернул объект с той же ссылкой на список (shared=" + shared + "). Мутация клона " +
            "изменила и оригинал. Prototype требует именно глубокого копирования вложенного состояния.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Prototype");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            class Document {
                private String title;
                private final List<String> tags;

                Document copy() {                       // глубокая копия
                    return new Document(title, new ArrayList<>(tags));
                }                                       // ^ новый список, не общая ссылка
            }

            Document clone = original.copy();
            clone.addTag("v2");                         // оригинал не задет
            """;

    private static final String BAD_CODE = """
            class Document {
                private String title;
                private List<String> tags;

                Document copy() {                       // поверхностная копия
                    return new Document(title, this.tags);
                }                                       // ^ ОБЩАЯ ссылка на список — баг
            }

            Document clone = original.copy();
            clone.addTag("v2");                         // оригинал тоже получил "v2"!
            """;
}
