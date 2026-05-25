package com.patterns.demo.patterns.behavioral.mediator;

import java.util.ArrayList;
import java.util.List;

/**
 * Антипаттерн вместо Mediator.
 *
 * Каждый участник держит ссылки на всех остальных и шлёт им напрямую. Связи
 * образуют «паутину»: N участников = N×(N−1) направленных связей, добавить
 * одного — соединить его со всеми.
 */
public final class BadMediator {

    public static final class User {
        public final String name;
        private final List<User> peers = new ArrayList<>();

        public User(String name) { this.name = name; }
        public void connect(User u) { peers.add(u); } // знает всех остальных

        public List<String> send(String msg) {
            List<String> delivered = new ArrayList<>();
            for (User p : peers) delivered.add(p.name);
            return delivered;
        }

        public int peerCount() { return peers.size(); }
    }

    /** Полносвязная сеть: N×(N−1) направленных связей. */
    public static int meshConnections(int n) { return n * (n - 1); }

    private BadMediator() {}
}
