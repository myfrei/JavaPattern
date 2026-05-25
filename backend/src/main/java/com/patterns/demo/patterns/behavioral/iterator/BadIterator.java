package com.patterns.demo.patterns.behavioral.iterator;

/**
 * Антипаттерн вместо Iterator.
 *
 * Коллекция обнажает внутреннее хранилище (массив), и клиент сам ходит по
 * индексам. Код клиента жёстко завязан на структуру: сменить массив на список
 * или дерево — переписывать все циклы обхода, легко словить off-by-one.
 */
public final class BadIterator {

    public static final class Repo {
        public final String[] data; // внутренности наружу
        public Repo(String... data) { this.data = data; }
        public int size() { return data.length; }
    }

    private BadIterator() {}
}
