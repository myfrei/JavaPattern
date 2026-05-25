package com.patterns.demo.patterns.microservices.resilience.ratelimiter;

/**
 * Антипаттерн вместо Rate Limiter.
 *
 * Лимита нет: сервис принимает все запросы подряд. Всплеск трафика проходит
 * целиком, нагрузка превышает ёмкость — деградация или падение под нагрузкой.
 */
public final class BadRateLimiter {

    public static final class Service {
        private int load = 0;
        public void handle() { load++; } // принимаем всё без ограничений
        public int load() { return load; }
    }

    private BadRateLimiter() {}
}
