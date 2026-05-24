package com.patterns.demo.patterns.behavioral.chainofresponsibility;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Гоняет один и тот же сценарий (согласование расхода на $15 000) через
 * правильную цепочку и через монолитный if-else, собирая пошаговый отчёт
 * для пиксельной визуализации на фронте.
 */
@Service
public class ChainDemoService {

    private static final int AMOUNT = 15_000;

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Chain of Responsibility — корректная цепочка обработчиков");
        r.setDescription(
            "Запрос на согласование $15 000 проходит по цепочке " +
            "TeamLead → Manager → Director → CEO. Каждое звено одобряет в пределах " +
            "своего лимита либо передаёт дальше, не зная конечного получателя.");
        r.setCode(GOOD_CODE);

        // звенья цепи — для отрисовки боксов на фронте
        for (String[] h : GoodChain.HANDLERS) {
            r.getInstances().add(new PatternDemoResponse.InstanceInfo(h[0], h[1]));
        }

        long t = 0;
        r.getSteps().add(new PatternDemoResponse.Step(
            t++, "Request", "approve($15 000)", "входит в цепь", true));

        List<GoodChain.Trace> trace = GoodChain.run(new GoodChain.Request(AMOUNT));
        for (GoodChain.Trace tr : trace) {
            r.getSteps().add(new PatternDemoResponse.Step(
                t++, tr.actor(), tr.action(), tr.result(), tr.ok()));
        }

        String who = trace.stream()
                .filter(x -> x.result().contains("APPROVED"))
                .map(GoodChain.Trace::actor)
                .findFirst()
                .orElse(null);
        r.setVerdict(who != null ? "PASS — одобрено: " + who : "FAIL — запрос не обработан");
        r.setExplanation(
            (who != null ? who : "Никто") + " одобрил $15 000 в пределах своего лимита; " +
            "предыдущие звенья передали запрос дальше, не зная конечного получателя. " +
            "Новый уровень согласования = новое звено-класс, существующие не меняются (Open/Closed).");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Chain of Responsibility — антипаттерн: монолитный if-else");
        r.setDescription(
            "Тот же сценарий, но один класс ApprovalService знает все правила и их " +
            "порядок. Работает, но не расширяется: новый уровень — правка общего метода.");
        r.setCode(BAD_CODE);

        r.getInstances().add(new PatternDemoResponse.InstanceInfo(
            "ApprovalService", "знает все правила"));

        BadChain.Result res = BadChain.approve(AMOUNT);
        r.getSteps().add(new PatternDemoResponse.Step(
            0, "Request", "approve($15 000)", "входит в сервис", true));
        r.getSteps().add(new PatternDemoResponse.Step(
            1, "ApprovalService", "if/else if (" + res.branch() + ")",
            "ветка " + res.approver() + " → APPROVED ✓", true));

        r.setVerdict("FAIL — нет цепочки: один класс знает все правила");
        r.setExplanation(
            "Вся логика согласования зашита в один if-else. Добавить уровень или сменить " +
            "порядок = править этот метод и рисковать сломать соседние ветки. Высокая " +
            "связанность, плохая тестируемость, нулевое переиспользование звеньев.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Chain of Responsibility");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            abstract class Approver {
                protected Approver next;
                Approver linkTo(Approver next) { this.next = next; return next; }

                void handle(Request req) {
                    if (canApprove(req)) {
                        approve(req);
                    } else if (next != null) {
                        next.handle(req);            // передаём дальше по цепочке
                    } else {
                        System.out.println("Никто не одобрил: " + req);
                    }
                }
                abstract boolean canApprove(Request req);
                abstract void approve(Request req);
            }

            class Manager extends Approver {
                boolean canApprove(Request r) { return r.amount() <= 5_000; }
                void approve(Request r) { System.out.println("Manager одобрил " + r); }
            }
            // TeamLead, Director, CEO — аналогично, каждый со своим лимитом

            // сборка цепочки в рантайме
            Approver chain = new TeamLead();
            chain.linkTo(new Manager())
                 .linkTo(new Director())
                 .linkTo(new CEO());
            chain.handle(new Request(15_000));       // → одобрит Director
            """;

    private static final String BAD_CODE = """
            // Один класс знает ВСЕ правила и их порядок — не расширяется
            class ApprovalService {
                void approve(Request r) {
                    if (r.amount() <= 1_000) {
                        System.out.println("TeamLead одобрил " + r);
                    } else if (r.amount() <= 5_000) {
                        System.out.println("Manager одобрил " + r);
                    } else if (r.amount() <= 20_000) {
                        System.out.println("Director одобрил " + r);
                    } else {
                        System.out.println("CEO одобрил " + r);
                    }
                    // новый уровень? правим этот метод и рискуем сломать ветки
                }
            }
            """;
}
