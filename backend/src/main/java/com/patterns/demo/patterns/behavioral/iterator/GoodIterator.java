package com.patterns.demo.patterns.behavioral.iterator;

import java.util.Iterator;

/**
 * Хорошая реализация Iterator.
 *
 * Коллекция отдаёт итератор и реализует Iterable, скрывая внутреннюю структуру.
 * Клиент обходит элементы через hasNext()/next() (или for-each) и ничего не знает
 * о том, массив там, список или дерево — сменить хранилище можно без правки клиента.
 */
public final class GoodIterator {

    public static final class Repo implements Iterable<String> {
        private final String[] items;
        public Repo(String... items) { this.items = items; }

        @Override
        public Iterator<String> iterator() {
            return new Iterator<>() {
                private int i = 0;
                public boolean hasNext() { return i < items.length; }
                public String next() { return items[i++]; }
            };
        }
    }

    private GoodIterator() {}
}
