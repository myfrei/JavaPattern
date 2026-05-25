package com.patterns.demo.patterns.microservices.resilience.retry;

import java.util.ArrayList;
import java.util.List;

/**
 * Хорошая реализация Retry (внутрипроцессная симуляция).
 *
 * Транзиентные сбои повторяются с экспоненциальным backoff (паузы 0 → 100 → 200
 * мс), пока не выйдет успех или не исчерпаются попытки. Растущие паузы не дают
 * «забить» восстанавливающийся сервис (no retry storm).
 */
public final class GoodRetry {

    /** Падает первые failBefore раз, затем отвечает успехом. */
    public static final class FlakyService {
        private int calls = 0;
        private final int failBefore;
        public FlakyService(int failBefore) { this.failBefore = failBefore; }
        public boolean call() { calls++; return calls > failBefore; }
        public int calls() { return calls; }
    }

    public record Attempt(int n, long backoffMs, boolean ok) {}

    public static List<Attempt> run(FlakyService svc, int maxAttempts) {
        List<Attempt> log = new ArrayList<>();
        for (int n = 1; n <= maxAttempts; n++) {
            long wait = (n == 1) ? 0 : 100L * (1L << (n - 2)); // 0,100,200,400…
            boolean ok = svc.call();
            log.add(new Attempt(n, wait, ok));
            if (ok) break;
        }
        return log;
    }

    private GoodRetry() {}
}
