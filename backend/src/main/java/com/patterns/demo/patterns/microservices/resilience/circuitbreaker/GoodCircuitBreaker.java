package com.patterns.demo.patterns.microservices.resilience.circuitbreaker;

/**
 * Хорошая реализация Circuit Breaker (внутрипроцессная симуляция).
 *
 * После N подряд ошибок брейкер «размыкается» (OPEN) и начинает мгновенно
 * отклонять вызовы (fast-fail), не трогая упавший downstream. Через паузу он
 * пробует один вызов (HALF_OPEN); успех — закрывается, защищая и downstream, и
 * вызывающего от каскада таймаутов.
 */
public final class GoodCircuitBreaker {

    public enum State { CLOSED, OPEN, HALF_OPEN }

    public static final class Downstream {
        private int calls = 0;
        private boolean healthy = false;
        public void setHealthy(boolean healthy) { this.healthy = healthy; }
        public boolean call() { calls++; return healthy; }
        public int calls() { return calls; }
    }

    public static final class Breaker {
        private final int threshold;
        private int failures = 0;
        private State state = State.CLOSED;

        public Breaker(int threshold) { this.threshold = threshold; }
        public State state() { return state; }

        public String call(Downstream ds) {
            if (state == State.OPEN) return "fast-fail"; // downstream не вызывается
            boolean ok = ds.call();
            if (ok) {
                failures = 0;
                state = State.CLOSED;
                return "ok";
            }
            failures++;
            if (failures >= threshold) state = State.OPEN;
            return "fail";
        }

        public void halfOpen() {
            if (state == State.OPEN) state = State.HALF_OPEN;
        }
    }

    private GoodCircuitBreaker() {}
}
