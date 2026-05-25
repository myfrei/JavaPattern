package com.patterns.demo.patterns.microservices.data.cqrs;

/**
 * Хорошая реализация CQRS (внутрипроцессная симуляция).
 *
 * Команды (запись) и запросы (чтение) разнесены по разным моделям. Команда меняет
 * write-модель и обновляет денормализованную read-модель (проекцию). Запросы
 * читают готовую проекцию — быстро и без конкуренции с записью.
 */
public final class GoodCqrs {

    public static final class WriteSide {
        private int balance = 0;
        public void deposit(int amount) { balance += amount; } // обработка команды
        public int balance() { return balance; }
    }

    public static final class ReadSide {
        private String view = "balance: 0";
        public void project(int balance) { view = "balance: " + balance; } // денормализованная проекция
        public String query() { return view; }                              // быстрый read
    }

    private GoodCqrs() {}
}
