package com.patterns.demo.patterns.microservices.resilience.circuitbreaker;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

/**
 * Бьёт по упавшему downstream: через брейкер с fast-fail и восстановлением (good)
 * и без брейкера (bad). Отдаёт отчёт для визуализации конечного автомата
 * CLOSED → OPEN → HALF_OPEN.
 */
@Service
public class CircuitBreakerDemoService {

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Circuit Breaker — размыкание и fast-fail");
        r.setDescription(
            "После 3 ошибок брейкер размыкается (OPEN) и мгновенно отклоняет вызовы, не трогая упавший " +
            "downstream. Через паузу пробует один вызов (HALF_OPEN) — успех закрывает брейкер.");
        r.setCode(GOOD_CODE);

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("CLOSED", "state"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("OPEN", "state"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("HALF_OPEN", "state"));

        GoodCircuitBreaker.Breaker breaker = new GoodCircuitBreaker.Breaker(3);
        GoodCircuitBreaker.Downstream ds = new GoodCircuitBreaker.Downstream();

        long t = 0;
        for (int i = 1; i <= 3; i++) {
            breaker.call(ds);
            r.getSteps().add(new PatternDemoResponse.Step(t++, breaker.state().name(), "call → downstream", "fail (" + i + "/3)", false));
        }
        // OPEN: вызовы fast-fail, downstream не трогаем
        breaker.call(ds);
        r.getSteps().add(new PatternDemoResponse.Step(t++, breaker.state().name(), "call", "fast-fail (downstream не вызван)", true));
        breaker.call(ds);
        r.getSteps().add(new PatternDemoResponse.Step(t++, breaker.state().name(), "call", "fast-fail (сэкономлен вызов)", true));
        // half-open + recovery
        breaker.halfOpen();
        ds.setHealthy(true);
        breaker.call(ds);
        r.getSteps().add(new PatternDemoResponse.Step(t, breaker.state().name(), "trial → ok", "восстановлен → CLOSED", true));

        r.setVerdict("PASS — брейкер открылся после 3 ошибок; downstream вызван " + ds.calls() + " раз вместо 6");
        r.setExplanation(
            "В OPEN брейкер не трогает упавший сервис и мгновенно отвечает вызывающему — это гасит каскад " +
            "таймасов. HALF_OPEN аккуратно проверяет восстановление и закрывает брейкер при успехе.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Circuit Breaker — антипаттерн: без брейкера");
        r.setDescription(
            "Брейкера нет: каждый из вызовов идёт прямо в упавший downstream и ждёт таймаут. Зависшие " +
            "вызовы копятся и каскадом валят вызывающий сервис.");
        r.setCode(BAD_CODE);

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Downstream", "failing"));

        BadCircuitBreaker.Downstream ds = new BadCircuitBreaker.Downstream();
        long t = 0;
        for (int i = 1; i <= 5; i++) {
            ds.call();
            r.getSteps().add(new PatternDemoResponse.Step(t++, "Downstream", "call #" + i, "fail (таймаут, downstream hit)", false));
        }

        r.setVerdict("FAIL — нет брейкера: " + ds.calls() + "/5 вызовов бьют по упавшему сервису");
        r.setExplanation(
            "Без размыкания каждый вызов платит полный таймаут и держит поток. Под нагрузкой это исчерпывает " +
            "пулы и роняет вызывающий сервис каскадом. Circuit Breaker размыкает цепь и отвечает мгновенно.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Circuit Breaker");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            String call(Downstream ds) {
                if (state == OPEN) return "fast-fail";   // не трогаем упавший сервис
                if (ds.call()) { failures = 0; state = CLOSED; return "ok"; }
                if (++failures >= threshold) state = OPEN; // разомкнуть после N ошибок
                return "fail";
            }
            // OPEN → (пауза) → HALF_OPEN → пробный вызов → CLOSED при успехе
            """;

    private static final String BAD_CODE = """
            // Без брейкера: каждый вызов ждёт таймаут упавшего сервиса
            for (int i = 0; i < 5; i++) {
                downstream.call();   // fail (таймаут) — поток висит
            }
            // зависшие вызовы копятся → каскадный отказ вызывающего сервиса
            """;
}
