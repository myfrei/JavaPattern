package com.patterns.demo.patterns.microservices.resilience.ratelimiter;

/**
 * Хорошая реализация Rate Limiter (token bucket, внутрипроцессная симуляция).
 *
 * Ведро выдаёт ограниченное число токенов на окно. Запрос берёт токен; когда
 * токены кончились, лишние запросы отклоняются (429), защищая сервис от
 * перегрузки. По таймеру ведро пополняется (refill).
 */
public final class GoodRateLimiter {

    public static final class TokenBucket {
        private final int capacity;
        private int tokens;

        public TokenBucket(int capacity) {
            this.capacity = capacity;
            this.tokens = capacity;
        }

        public boolean tryAcquire() {
            if (tokens > 0) { tokens--; return true; }
            return false; // 429 Too Many Requests
        }

        public void refill() { tokens = capacity; }
        public int tokens() { return tokens; }
        public int capacity() { return capacity; }
    }

    private GoodRateLimiter() {}
}
