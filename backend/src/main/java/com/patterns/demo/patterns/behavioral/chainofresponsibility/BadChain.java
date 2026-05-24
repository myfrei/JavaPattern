package com.patterns.demo.patterns.behavioral.chainofresponsibility;

/**
 * Плохая реализация: один класс ApprovalService знает ВСЕ правила и их порядок.
 *
 * Функционально работает, но это антипаттерн: любое изменение (новый уровень
 * согласования, другой порядок проверок) — правка одного общего метода с риском
 * сломать соседние ветки. Нет переиспользования звеньев и расширяемости.
 */
public final class BadChain {

    public record Result(String approver, String branch) {}

    public static Result approve(int amount) {
        if (amount <= 1_000) {
            return new Result("TeamLead", "amount <= 1 000");
        } else if (amount <= 5_000) {
            return new Result("Manager", "amount <= 5 000");
        } else if (amount <= 20_000) {
            return new Result("Director", "amount <= 20 000");
        } else {
            return new Result("CEO", "else");
        }
    }

    private BadChain() {}
}
