package com.patterns.demo.patterns.behavioral.memento;

import java.util.ArrayList;
import java.util.List;

/**
 * Антипаттерн вместо Memento.
 *
 * Состояние редактора (изменяемый список строк) выставлено наружу, и «снимок» —
 * это просто ссылка на тот же список. После новых правок снимок меняется вместе с
 * оригиналом: восстановить прошлое состояние уже нельзя.
 */
public final class BadMemento {

    public static final class Editor {
        public List<String> lines = new ArrayList<>(); // состояние наружу
        public void type(String s) { lines.add(s); }
    }

    private BadMemento() {}
}
