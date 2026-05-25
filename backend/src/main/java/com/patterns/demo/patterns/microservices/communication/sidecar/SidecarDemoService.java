package com.patterns.demo.patterns.microservices.communication.sidecar;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Обрабатывает запрос #42 со сквозными задачами (TLS, rate-limit, метрики):
 * через сайдкар при чистой бизнес-логике (good) и с инфра-кодом внутри сервиса
 * (bad). Отдаёт отчёт для визуализации «app + sidecar».
 */
@Service
public class SidecarDemoService {

    private static final String REQ = "#42";

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Sidecar — сквозные задачи рядом с приложением");
        r.setDescription(
            "OrderApp содержит только бизнес-логику. Сайдкар оборачивает запрос TLS, rate-limit и метриками. " +
            "Тот же сайдкар переиспользуется любым сервисом без правки его кода.");
        r.setCode(GOOD_CODE);

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("OrderApp", "business"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Sidecar", "cross-cutting"));

        List<String> trace = new GoodSidecar.Sidecar(new GoodSidecar.OrderApp()).process(REQ);
        long t = 0;
        for (String line : trace) {
            boolean biz = line.startsWith("biz:");
            r.getSteps().add(new PatternDemoResponse.Step(
                t++, biz ? "OrderApp" : "Sidecar", biz ? "handle(" + REQ + ")" : line, biz ? line + " (чистая логика)" : "cross-cutting", true));
        }

        r.setVerdict("PASS — бизнес-логика чистая, сквозное в сайдкаре");
        r.setExplanation(
            "Сайдкар берёт на себя TLS/лимиты/метрики, поэтому приложение остаётся только про бизнес. Обновить " +
            "сквозную политику = обновить сайдкар, а не каждый сервис.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Sidecar — антипаттерн: инфра-код внутри сервиса");
        r.setDescription(
            "OrderApp сам делает TLS, rate-limit и метрики вперемешку с бизнес-логикой. Этот инфра-код " +
            "дублируется в каждом сервисе, и его обновление = правка всех сервисов.");
        r.setCode(BAD_CODE);

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("OrderApp", "business + infra"));

        List<String> trace = new BadSidecar.OrderApp().handle(REQ);
        long t = 0;
        for (String line : trace) {
            boolean biz = line.startsWith("biz:");
            r.getSteps().add(new PatternDemoResponse.Step(
                t++, "OrderApp", line, biz ? "бизнес" : "инфра в коде сервиса", false));
        }

        r.setVerdict("FAIL — каждый сервис тащит инфра-код (дублирование)");
        r.setExplanation(
            "Сквозные задачи зашиты в сервис и перемешаны с бизнес-логикой: дублирование и риск рассинхрона " +
            "политик. Sidecar выносит инфраструктуру в отдельный процесс рядом с приложением.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Sidecar");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            class OrderApp implements App {
                public String handle(String req) { return "order:" + req; } // только бизнес
            }

            class Sidecar {                                  // оборачивает сквозным
                List<String> process(String req) {
                    tls(); rateLimit();
                    String out = app.handle(req);            // бизнес-логика чистая
                    metrics();
                    return out;
                }
            }
            """;

    private static final String BAD_CODE = """
            // Инфра-код вперемешку с бизнесом в каждом сервисе
            class OrderApp {
                String handle(String req) {
                    tlsTerminate();        // инфра
                    rateLimitCheck();      // инфра
                    var out = "order:" + req; // бизнес
                    metricsEmit();         // инфра
                    return out;
                }
            }
            """;
}
