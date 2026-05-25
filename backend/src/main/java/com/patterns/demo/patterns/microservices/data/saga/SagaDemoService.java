package com.patterns.demo.patterns.microservices.data.saga;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Прогоняет распределённую транзакцию оформления заказа, где шаг резервирования
 * товара падает: с компенсациями (good) и без них (bad). Отдаёт отчёт для
 * визуализации «шаги саги + откат».
 */
@Service
public class SagaDemoService {

    private static List<GoodSaga.Step> scenario() {
        return List.of(
            new GoodSaga.Step("createOrder", true, "cancel order"),
            new GoodSaga.Step("chargePayment", true, "refund"),
            new GoodSaga.Step("reserveInventory", false, "restock"));
    }

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Saga — компенсации сохраняют согласованность");
        r.setDescription(
            "createOrder и chargePayment проходят, а reserveInventory падает (нет товара). Сага откатывает " +
            "завершённые шаги в обратном порядке: refund, затем cancel order — система остаётся согласованной.");
        r.setCode(GOOD_CODE);

        List<String> trace = GoodSaga.run(scenario());
        boolean compensated = trace.stream().anyMatch(x -> x.startsWith("↩"));

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("createOrder", "compensated"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("chargePayment", "compensated"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("reserveInventory", "failed"));

        long t = 0;
        r.getSteps().add(new PatternDemoResponse.Step(t++, "createOrder", "execute", "✓ выполнен", true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "chargePayment", "execute", "✓ деньги списаны", true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "reserveInventory", "execute", "✗ нет товара → откат", false));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "chargePayment", "compensate", "↩ refund (деньги возвращены)", true));
        r.getSteps().add(new PatternDemoResponse.Step(t, "createOrder", "compensate", "↩ cancel order", true));

        r.setVerdict(compensated ? "PASS — согласованность сохранена компенсациями" : "PASS");
        r.setExplanation(
            "Сага не использует распределённую блокировку: при сбое шага она вызывает компенсации завершённых " +
            "шагов в обратном порядке. Итог — согласованное состояние без распределённой транзакции (2PC).");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Saga — антипаттерн: шаги без компенсаций");
        r.setDescription(
            "Те же шаги, но без компенсаций. reserveInventory падает, а createOrder и chargePayment уже " +
            "закоммичены — деньги списаны и заказ создан, но товара нет: система в рассинхроне.");
        r.setCode(BAD_CODE);

        BadSaga.run(scenario());

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("createOrder", "committed"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("chargePayment", "committed"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("reserveInventory", "failed"));

        long t = 0;
        r.getSteps().add(new PatternDemoResponse.Step(t++, "createOrder", "commit", "✓ закоммичен", true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "chargePayment", "commit", "✓ деньги списаны", true));
        r.getSteps().add(new PatternDemoResponse.Step(t, "reserveInventory", "commit", "✗ нет товара — стоп, отката нет", false));

        r.setVerdict("FAIL — частичный коммит: деньги списаны, заказ есть, товара нет");
        r.setExplanation(
            "Без компенсаций ранние шаги остаются закоммиченными при сбое позднего — это рассинхрон. Saga " +
            "вводит компенсирующие действия, чтобы откатить уже сделанное.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Saga");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            List<String> run(List<Step> steps) {
                Deque<Step> done = new ArrayDeque<>();
                for (Step s : steps) {
                    if (s.ok()) { done.push(s); }              // шаг прошёл
                    else {
                        while (!done.isEmpty())
                            compensate(done.pop());            // откат завершённых в обратном порядке
                        break;
                    }
                }
            }
            // createOrder ✓, chargePayment ✓, reserveInventory ✗ → refund, cancel order
            """;

    private static final String BAD_CODE = """
            // Шаги без компенсаций
            createOrder();        // ✓ закоммичен
            chargePayment();      // ✓ деньги списаны
            reserveInventory();   // ✗ нет товара → исключение
            // откатить createOrder и chargePayment нечем → рассинхрон
            """;
}
