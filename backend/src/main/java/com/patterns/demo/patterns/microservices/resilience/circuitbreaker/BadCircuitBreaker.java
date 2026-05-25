package com.patterns.demo.patterns.microservices.resilience.circuitbreaker;

/**
 * Антипаттерн вместо Circuit Breaker.
 *
 * Брейкера нет: каждый вызов идёт прямо в упавший downstream и ждёт таймаут.
 * Под нагрузкой это копит зависшие потоки и каскадом валит вызывающий сервис.
 */
public final class BadCircuitBreaker {

    public static final class Downstream {
        private int calls = 0;
        public boolean call() { calls++; return false; } // всегда падает (таймаут)
        public int calls() { return calls; }
    }

    private BadCircuitBreaker() {}
}
