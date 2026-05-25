package com.patterns.demo.patterns.microservices.resilience.bulkhead;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

/**
 * Медленный report исчерпывает свою ёмкость: с отдельными пулами (good) и с одним
 * общим пулом (bad). Отдаёт отчёт для визуализации «изолированные отсеки».
 */
@Service
public class BulkheadDemoService {

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Bulkhead — изолированные пулы на зависимость");
        r.setDescription(
            "У payment и report — отдельные пулы по 2 слота. Медленный report забивает свой пул (2/2), но " +
            "пул payment свободен, и вызовы payment продолжают проходить — сбой изолирован.");
        r.setCode(GOOD_CODE);

        GoodBulkhead.Pool reportPool = new GoodBulkhead.Pool("reportPool", 2);
        GoodBulkhead.Pool paymentPool = new GoodBulkhead.Pool("paymentPool", 2);

        long t = 0;
        reportPool.acquire();
        r.getSteps().add(new PatternDemoResponse.Step(t++, "reportPool", "report acquire", reportPool.inUse() + "/" + reportPool.capacity(), true));
        reportPool.acquire();
        r.getSteps().add(new PatternDemoResponse.Step(t++, "reportPool", "report acquire", reportPool.inUse() + "/" + reportPool.capacity() + " · full", true));
        boolean reportRejected = !reportPool.acquire();
        r.getSteps().add(new PatternDemoResponse.Step(t++, "reportPool", "report acquire", reportRejected ? "reject (свой пул полон)" : "ok", true));
        boolean paymentOk = paymentPool.acquire();
        r.getSteps().add(new PatternDemoResponse.Step(t, "paymentPool", "payment acquire", paymentOk ? "ok (изолирован)" : "reject", paymentOk));

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("paymentPool", paymentPool.inUse() + "/" + paymentPool.capacity() + " · ok"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("reportPool", reportPool.inUse() + "/" + reportPool.capacity() + " · full"));

        r.setVerdict("PASS — изоляция: report полон (2/2), а payment проходит");
        r.setExplanation(
            "Отдельный пул на зависимость локализует насыщение: медленный report тратит только свой бюджет " +
            "ресурсов. Остальные вызовы идут через свои отсеки и не голодают.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Bulkhead — антипаттерн: один общий пул");
        r.setDescription(
            "Все зависимости делят один пул на 4 слота. Медленный report занимает все 4, и вызов payment " +
            "получает отказ — частичный сбой расползся на не связанную функциональность.");
        r.setCode(BAD_CODE);

        BadBulkhead.SharedPool shared = new BadBulkhead.SharedPool(4);

        long t = 0;
        for (int i = 1; i <= 4; i++) {
            shared.acquire();
            r.getSteps().add(new PatternDemoResponse.Step(t++, "sharedPool", "report acquire #" + i, shared.inUse() + "/" + shared.capacity(), true));
        }
        boolean paymentOk = shared.acquire();
        r.getSteps().add(new PatternDemoResponse.Step(t, "sharedPool", "payment acquire", paymentOk ? "ok" : "reject (пул исчерпан report)", paymentOk));

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("sharedPool", shared.inUse() + "/" + shared.capacity() + " · full → payment rejected"));

        r.setVerdict("FAIL — общий пул исчерпан report, payment голодает");
        r.setExplanation(
            "Без отсеков одна медленная зависимость забирает все ресурсы и роняет не связанные с ней вызовы. " +
            "Bulkhead режет ресурсы на изолированные пулы, ограничивая радиус поражения.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Bulkhead");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            Pool reportPool  = new Pool("reportPool", 2);   // отдельные отсеки
            Pool paymentPool = new Pool("paymentPool", 2);

            reportPool.acquire(); reportPool.acquire();      // report: 2/2 (full)
            reportPool.acquire();                            // reject — но только report
            paymentPool.acquire();                           // ok — payment изолирован
            """;

    private static final String BAD_CODE = """
            // Один общий пул на всё
            SharedPool pool = new SharedPool(4);
            for (int i = 0; i < 4; i++) pool.acquire();   // report занял все 4 слота
            pool.acquire();                                // payment → reject (голодает)
            """;
}
