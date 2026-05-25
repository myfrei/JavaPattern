package com.patterns.demo.patterns.behavioral.iterator;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

/**
 * Обходит коллекцию [alpha, beta, gamma]: через итератор, скрывающий структуру
 * (good), и через прямой доступ к массиву по индексам (bad). Отдаёт отчёт для
 * визуализации «курсор бежит по коллекции».
 */
@Service
public class IteratorDemoService {

    private static final String[] ITEMS = {"alpha", "beta", "gamma"};

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Iterator — обход без знания структуры");
        r.setDescription(
            "Repo реализует Iterable и отдаёт итератор. Клиент идёт по hasNext()/next() и не знает, " +
            "что внутри массив. Поменять хранилище можно, не трогая код обхода.");
        r.setCode(GOOD_CODE);

        for (String it : ITEMS) {
            r.getInstances().add(new PatternDemoResponse.InstanceInfo(it, "element"));
        }

        GoodIterator.Repo repo = new GoodIterator.Repo(ITEMS);
        long t = 0;
        for (String value : repo) {
            r.getSteps().add(new PatternDemoResponse.Step(t++, value, "next()", "→ " + value, true));
        }

        r.setVerdict("PASS — обойдено " + ITEMS.length + " элемента без доступа к структуре");
        r.setExplanation(
            "Итератор инкапсулирует позицию обхода. Клиент работает с hasNext()/next(), поэтому смена " +
            "внутреннего хранилища (массив → список/дерево) не ломает обходящий код.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Iterator — антипаттерн: обход по индексам массива");
        r.setDescription(
            "Repo обнажает массив data, клиент ходит по индексам data[i]. Код завязан на структуру: " +
            "сменить массив на другую коллекцию — переписывать все циклы, плюс риск off-by-one.");
        r.setCode(BAD_CODE);

        for (String it : ITEMS) {
            r.getInstances().add(new PatternDemoResponse.InstanceInfo(it, "data[]"));
        }

        BadIterator.Repo repo = new BadIterator.Repo(ITEMS);
        long t = 0;
        for (int i = 0; i < repo.size(); i++) {
            r.getSteps().add(new PatternDemoResponse.Step(t++, repo.data[i], "data[" + i + "]", "→ " + repo.data[i], false));
        }

        r.setVerdict("FAIL — клиент завязан на массив и индексы");
        r.setExplanation(
            "Прямой доступ к data[i] жёстко связывает клиента со структурой. Любая смена хранилища ломает " +
            "обход. Iterator прячет это за единым интерфейсом перебора.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Iterator");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            class Repo implements Iterable<String> {
                private final String[] items;
                public Iterator<String> iterator() {
                    return new Iterator<>() {
                        int i = 0;
                        public boolean hasNext() { return i < items.length; }
                        public String next()     { return items[i++]; }
                    };
                }
            }

            for (String s : repo) { ... }   // не знает, что внутри массив
            """;

    private static final String BAD_CODE = """
            // Коллекция обнажает массив, клиент ходит по индексам
            class Repo { public final String[] data; public int size(){ return data.length; } }

            for (int i = 0; i < repo.size(); i++) {
                use(repo.data[i]);          // завязан на массив; смена структуры всё ломает
            }
            """;
}
