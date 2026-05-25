package com.patterns.demo.patterns.microservices.data.eventsourcing;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

/**
 * Применяет три операции к счёту: через append-only лог событий с replay (good)
 * и через одно мутируемое значение без истории (bad). Отдаёт отчёт для
 * визуализации «лог событий → свёртка».
 */
@Service
public class EventSourcingDemoService {

    private static final String[] EVENTS = {"deposit 100", "withdraw 30", "deposit 50"};
    private static final String[] SUB = {"+100", "-30", "+50"};

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Event Sourcing — состояние из лога событий");
        r.setDescription(
            "Счёт хранит append-only лог: deposit 100, withdraw 30, deposit 50. Баланс получается свёрткой " +
            "(= 120), а по логу можно восстановить состояние на любую версию — например, баланс после 2 событий = 70.");
        r.setCode(GOOD_CODE);

        GoodEventSourcing.Account acc = new GoodEventSourcing.Account();
        long t = 0;
        for (int i = 0; i < EVENTS.length; i++) {
            acc.apply(EVENTS[i]);
            r.getInstances().add(new PatternDemoResponse.InstanceInfo(EVENTS[i], SUB[i]));
            r.getSteps().add(new PatternDemoResponse.Step(t++, EVENTS[i], "append", "Σ = " + acc.balance(), true));
        }
        int v2 = acc.balanceAt(2);
        r.getSteps().add(new PatternDemoResponse.Step(t, "replay", "balanceAt(2)", "→ Σ = " + v2 + " (audit/replay)", true));

        r.setVerdict("PASS — баланс " + acc.balance() + " из лога; replay к версии 2 = " + v2);
        r.setExplanation(
            "Лог событий — источник истины. Текущее состояние — свёртка событий, а история даёт аудит, " +
            "восстановление на любой момент и проекции под разные нужды.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Event Sourcing — антипаттерн: только текущее значение");
        r.setDescription(
            "Хранится лишь текущий баланс, который мутируется на месте. Итог тот же (120), но история стёрта: " +
            "ни аудита, ни восстановления состояния на прошлый момент, ни replay.");
        r.setCode(BAD_CODE);

        BadEventSourcing.Account acc = new BadEventSourcing.Account();
        long t = 0;
        acc.deposit(100);
        r.getSteps().add(new PatternDemoResponse.Step(t++, "balance", "deposit(100)", "balance = " + acc.balance(), true));
        acc.withdraw(30);
        r.getSteps().add(new PatternDemoResponse.Step(t++, "balance", "withdraw(30)", "balance = " + acc.balance(), true));
        acc.deposit(50);
        r.getSteps().add(new PatternDemoResponse.Step(t++, "balance", "deposit(50)", "balance = " + acc.balance(), true));
        r.getSteps().add(new PatternDemoResponse.Step(t, "balance", "balanceAt(2)?", "истории нет — восстановить нельзя ✗", false));

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("balance = " + acc.balance(), "mutable"));

        r.setVerdict("FAIL — только текущее значение, история потеряна");
        r.setExplanation(
            "Мутация значения на месте стирает прошлое: нельзя понять, как пришли к балансу, и восстановить " +
            "момент. Event Sourcing хранит события, а состояние выводит из них.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Event Sourcing");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            class Account {
                private final List<String> events = new ArrayList<>();   // append-only лог
                void apply(String event) { events.add(event); }
                int balanceAt(int version) {                              // свёртка событий
                    int b = 0;
                    for (int i = 0; i < version; i++) b += delta(events.get(i));
                    return b;
                }
            }

            acc.apply("deposit 100"); acc.apply("withdraw 30"); acc.apply("deposit 50");
            acc.balance();        // 120 (свёртка)
            acc.balanceAt(2);     // 70  (replay в прошлое)
            """;

    private static final String BAD_CODE = """
            // Хранится только текущее значение
            class Account {
                private int balance = 0;
                void deposit(int a)  { balance += a; }   // мутация на месте
                void withdraw(int a) { balance -= a; }
            }
            // balance = 120, но "как пришли?" и "сколько было после 2 операций?" — не ответить
            """;
}
