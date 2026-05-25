package com.patterns.demo.patterns.microservices.data.eventsourcing;

import java.util.ArrayList;
import java.util.List;

/**
 * Хорошая реализация Event Sourcing (внутрипроцессная симуляция).
 *
 * Состояние не хранится напрямую — хранится append-only лог событий. Текущий
 * баланс получается свёрткой (fold) событий. Лог даёт аудит и replay: можно
 * восстановить состояние на любой момент (версию).
 */
public final class GoodEventSourcing {

    public static final class Account {
        private final List<String> events = new ArrayList<>();

        public void apply(String event) { events.add(event); } // append-only

        public List<String> history() { return List.copyOf(events); }

        public int balance() { return balanceAt(events.size()); }

        /** Свёртка первых `version` событий — состояние на момент в прошлом. */
        public int balanceAt(int version) {
            int b = 0;
            for (int i = 0; i < Math.min(version, events.size()); i++) {
                String[] p = events.get(i).split(" ");
                int amt = Integer.parseInt(p[1]);
                b += p[0].equals("deposit") ? amt : -amt;
            }
            return b;
        }
    }

    private GoodEventSourcing() {}
}
