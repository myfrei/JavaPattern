package com.patterns.demo.patterns.microservices.resilience.ratelimiter;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

/**
 * Прогоняет 5 запросов при лимите 3/окно: через token bucket (good) и без лимита
 * (bad). Отдаёт отчёт для визуализации «ведро токенов + поток запросов».
 */
@Service
public class RateLimiterDemoService {

    private static final int LIMIT = 3;
    private static final int REQUESTS = 5;

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Rate Limiter — token bucket защищает сервис");
        r.setDescription(
            "Лимит 3 запроса на окно. Первые 3 запроса берут токены и проходят, оставшиеся 2 получают 429. " +
            "Сервис принимает только посильную нагрузку.");
        r.setCode(GOOD_CODE);

        GoodRateLimiter.TokenBucket bucket = new GoodRateLimiter.TokenBucket(LIMIT);
        long t = 0;
        int allowed = 0;
        for (int i = 1; i <= REQUESTS; i++) {
            boolean ok = bucket.tryAcquire();
            if (ok) allowed++;
            r.getInstances().add(new PatternDemoResponse.InstanceInfo("req " + i, ok ? "allowed" : "429"));
            r.getSteps().add(new PatternDemoResponse.Step(
                t++, "req " + i, ok ? "tryAcquire" : "tryAcquire", ok ? "✓ allowed (tokens=" + bucket.tokens() + ")" : "✗ 429 Too Many Requests", ok));
        }

        r.setVerdict("PASS — лимит " + LIMIT + "/окно: " + allowed + " пропущено, " + (REQUESTS - allowed) + " → 429");
        r.setExplanation(
            "Ведро токенов ограничивает входящую нагрузку: лишние запросы быстро отклоняются с 429, и сервис " +
            "не уходит в перегрузку. По таймеру токены пополняются.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Rate Limiter — антипаттерн: без лимита");
        r.setDescription(
            "Лимита нет: сервис принимает все 5 запросов подряд. Нагрузка превышает посильные " + LIMIT +
            " и сервис уходит в перегрузку.");
        r.setCode(BAD_CODE);

        BadRateLimiter.Service svc = new BadRateLimiter.Service();
        long t = 0;
        for (int i = 1; i <= REQUESTS; i++) {
            svc.handle();
            boolean overloaded = svc.load() > LIMIT;
            r.getInstances().add(new PatternDemoResponse.InstanceInfo("req " + i, "passed"));
            r.getSteps().add(new PatternDemoResponse.Step(
                t++, "req " + i, "handle", "load=" + svc.load() + (overloaded ? " · перегрузка" : ""), !overloaded));
        }

        r.setVerdict("FAIL — без лимита: все " + svc.load() + " прошли, сервис перегружен (>" + LIMIT + ")");
        r.setExplanation(
            "Любой всплеск трафика проходит целиком и превышает ёмкость — деградация или падение. Rate Limiter " +
            "режет нагрузку до посильной, отклоняя лишнее с 429.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Rate Limiter");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            class TokenBucket {
                private int tokens = capacity;          // 3
                boolean tryAcquire() {
                    if (tokens > 0) { tokens--; return true; }
                    return false;                        // 429 Too Many Requests
                }
            }
            // 5 запросов при лимите 3 → 3 проходят, 2 получают 429
            """;

    private static final String BAD_CODE = """
            // Без лимита: принимаем всё подряд
            for (int i = 0; i < 5; i++) service.handle();   // load растёт без границ
            // всплеск проходит целиком → перегрузка/падение
            """;
}
