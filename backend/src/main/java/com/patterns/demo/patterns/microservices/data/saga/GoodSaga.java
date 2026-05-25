package com.patterns.demo.patterns.microservices.data.saga;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Хорошая реализация Saga (внутрипроцессная симуляция).
 *
 * Распределённая транзакция выполняется как последовательность локальных шагов.
 * Если какой-то шаг падает, сага откатывает уже завершённые шаги их
 * компенсациями в обратном порядке — система остаётся согласованной без 2PC.
 */
public final class GoodSaga {

    public record Step(String name, boolean ok, String compensation) {}

    public static List<String> run(List<Step> steps) {
        List<String> trace = new ArrayList<>();
        Deque<Step> done = new ArrayDeque<>();
        for (Step s : steps) {
            if (s.ok()) {
                trace.add("✓ " + s.name());
                done.push(s);
            } else {
                trace.add("✗ " + s.name() + " FAILED → compensating");
                while (!done.isEmpty()) {
                    trace.add("↩ " + done.pop().compensation()); // откат завершённых в обратном порядке
                }
                return trace;
            }
        }
        return trace;
    }

    private GoodSaga() {}
}
