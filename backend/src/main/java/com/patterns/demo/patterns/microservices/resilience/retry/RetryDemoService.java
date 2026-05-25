package com.patterns.demo.patterns.microservices.resilience.retry;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Зовёт сервис, который падает 2 раза и потом отвечает: с повтором и backoff
 * (good) и одной попыткой без повторов (bad). Отдаёт отчёт для визуализации
 * «попытки с растущими паузами».
 */
@Service
public class RetryDemoService {

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Retry — повтор транзиентных сбоев с backoff");
        r.setDescription(
            "Сервис падает первые 2 раза (моргнула сеть) и отвечает на 3-й. Retry повторяет с " +
            "экспоненциальным backoff (0 → 100 → 200 мс) и доводит запрос до успеха.");
        r.setCode(GOOD_CODE);

        GoodRetry.FlakyService svc = new GoodRetry.FlakyService(2);
        List<GoodRetry.Attempt> log = GoodRetry.run(svc, 5);

        long t = 0;
        for (GoodRetry.Attempt a : log) {
            String sub = a.backoffMs() + "ms · " + (a.ok() ? "ok" : "fail");
            r.getInstances().add(new PatternDemoResponse.InstanceInfo("attempt " + a.n(), sub));
            r.getSteps().add(new PatternDemoResponse.Step(
                t++, "attempt " + a.n(), "backoff " + a.backoffMs() + "ms → call", a.ok() ? "✓ ok" : "✗ fail", a.ok()));
        }

        boolean ok = log.get(log.size() - 1).ok();
        r.setVerdict(ok ? "PASS — успех на попытке " + log.size() + " (backoff 0→100→200ms)" : "FAIL");
        r.setExplanation(
            "Транзиентный сбой переживается повтором, а экспоненциальный backoff (плюс jitter в проде) " +
            "разводит повторы во времени и не добивает восстанавливающийся сервис.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Retry — антипаттерн: без повторов");
        r.setDescription(
            "Тот же сервис, но повторов нет. Первая же транзиентная ошибка роняет запрос — хотя 3-я попытка " +
            "прошла бы. (Обратная крайность — повтор без backoff, который добивает сервис штормом.)");
        r.setCode(BAD_CODE);

        GoodRetry.FlakyService svc = new GoodRetry.FlakyService(2);
        boolean ok = BadRetry.once(svc);

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("attempt 1", "no retry · fail"));
        r.getSteps().add(new PatternDemoResponse.Step(0, "attempt 1", "call (без повторов)", ok ? "✓ ok" : "✗ fail → запрос потерян", ok));

        r.setVerdict("FAIL — без повторов транзиентный сбой роняет запрос");
        r.setExplanation(
            "Сеть и поды иногда моргают; отсутствие повтора превращает временный сбой в отказ. Retry с backoff " +
            "переживает транзиентные ошибки, не создавая шторма повторов.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Retry");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            for (int n = 1; n <= maxAttempts; n++) {
                long wait = (n == 1) ? 0 : 100L * (1L << (n - 2)); // 0,100,200,400…
                sleep(wait);
                if (service.call()) return SUCCESS;                // успех — выходим
            }
            // падает 2 раза, успех на 3-й попытке
            """;

    private static final String BAD_CODE = """
            // Одна попытка, без повторов
            boolean ok = service.call();   // ✗ транзиентный сбой → запрос потерян
            // а 3-я попытка прошла бы.
            // обратная крайность: while(!ok) call();  // шторм без backoff
            """;
}
