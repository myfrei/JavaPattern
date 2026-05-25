package com.patterns.demo.patterns.microservices.data.eventsourcing;

/**
 * Антипаттерн вместо Event Sourcing.
 *
 * Хранится только текущее значение, которое мутируется на месте. История теряется:
 * нельзя ни провести аудит «как пришли к этому балансу», ни восстановить состояние
 * на момент в прошлом, ни перепроиграть события.
 */
public final class BadEventSourcing {

    public static final class Account {
        private int balance = 0;
        public void deposit(int amount) { balance += amount; }   // мутируем на месте
        public void withdraw(int amount) { balance -= amount; }
        public int balance() { return balance; }
        // истории нет — прошлое стёрто
    }

    private BadEventSourcing() {}
}
