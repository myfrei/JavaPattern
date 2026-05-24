package com.patterns.demo.patterns.behavioral.chainofresponsibility;

import java.util.ArrayList;
import java.util.List;

/**
 * Хорошая реализация Chain of Responsibility.
 *
 * Запрос на согласование расходов идёт по цепочке обработчиков. Каждое звено
 * либо одобряет сумму в пределах своего лимита, либо передаёт запрос дальше,
 * ничего не зная о следующем получателе. Цепочка собирается в рантайме —
 * новый уровень согласования добавляется новым звеном без правки остальных.
 */
public final class GoodChain {

    public record Request(int amount) {}

    /** Запись хода выполнения для пошаговой визуализации на фронте. */
    public record Trace(String actor, String action, String result, boolean ok) {}

    /** Звено цепочки: знает только свой лимит и ссылку на следующего. */
    static final class Approver {
        private final String name;
        private final int limit;            // Integer.MAX_VALUE = без ограничений
        private Approver next;

        Approver(String name, int limit) {
            this.name = name;
            this.limit = limit;
        }

        Approver linkTo(Approver next) {
            this.next = next;
            return next;
        }

        void handle(Request req, List<Trace> trace) {
            String cap = limit == Integer.MAX_VALUE ? "∞" : "$" + money(limit);
            if (req.amount() <= limit) {
                trace.add(new Trace(name, "лимит " + cap, "APPROVED ✓", true));
            } else if (next != null) {
                trace.add(new Trace(name, "лимит " + cap, "не тянет → дальше", true));
                next.handle(req, trace);
            } else {
                trace.add(new Trace(name, "лимит " + cap, "REJECTED ✗", false));
            }
        }
    }

    /** Уровни согласования по возрастанию полномочий — для отрисовки цепи. */
    public static final String[][] HANDLERS = {
        {"TeamLead", "≤ $1 000"},
        {"Manager",  "≤ $5 000"},
        {"Director",  "≤ $20 000"},
        {"CEO",       "∞"},
    };

    public static List<Trace> run(Request req) {
        List<Trace> trace = new ArrayList<>();
        Approver lead = new Approver("TeamLead", 1_000);
        lead.linkTo(new Approver("Manager", 5_000))
            .linkTo(new Approver("Director", 20_000))
            .linkTo(new Approver("CEO", Integer.MAX_VALUE));
        lead.handle(req, trace);
        return trace;
    }

    static String money(int v) {
        return String.format("%,d", v).replace(',', ' ');
    }

    private GoodChain() {}
}
