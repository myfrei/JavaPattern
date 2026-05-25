package com.patterns.demo.patterns.behavioral.memento;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Хорошая реализация Memento.
 *
 * Editor (originator) сам создаёт снимок своего состояния (Memento) и умеет
 * восстановиться из него. History (caretaker) хранит снимки, не зная их
 * внутренностей. Инкапсуляция цела: снимок — это копия значения, а не общая ссылка.
 */
public final class GoodMemento {

    /** Снимок: непрозрачен для внешнего мира, состояние читает только Editor. */
    public static final class Memento {
        private final String state;
        private Memento(String state) { this.state = state; }
        private String state() { return state; }
    }

    public static final class Editor {
        private String content = "";
        public void type(String s) { content += s; }
        public String content() { return content; }
        public Memento save() { return new Memento(content); }      // копия значения
        public void restore(Memento m) { this.content = m.state(); }
    }

    public static final class History {
        private final Deque<Memento> stack = new ArrayDeque<>();
        public void push(Memento m) { stack.push(m); }
        public Memento pop() { return stack.pop(); }
        public int size() { return stack.size(); }
    }

    private GoodMemento() {}
}
