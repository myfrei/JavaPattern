package com.patterns.demo.patterns.microservices.data.cqrs;

import java.util.ArrayList;
import java.util.List;

/**
 * Антипаттерн вместо CQRS.
 *
 * Одна модель и для записи, и для чтения. Запрос каждый раз пересчитывает результат
 * по всему write-логу — чтение конкурирует с записью и деградирует с ростом данных.
 */
public final class BadCqrs {

    private final List<Integer> deposits = new ArrayList<>();

    public void deposit(int amount) { deposits.add(amount); }

    public int query() {
        int sum = 0;
        for (int d : deposits) sum += d; // пересчёт по всему логу на каждый запрос
        return sum;
    }

    public int logSize() { return deposits.size(); }
}
