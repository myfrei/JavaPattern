package com.patterns.demo.patterns.behavioral.mediator;

import java.util.ArrayList;
import java.util.List;

/**
 * Хорошая реализация Mediator.
 *
 * Участники не знают друг о друге — они общаются через посредника (ChatRoom).
 * Каждый связан только с посредником, поэтому число связей растёт линейно (N), а
 * не как N×N. Добавить участника = зарегистрировать его в комнате.
 */
public final class GoodMediator {

    public static final class ChatRoom {
        private final List<String> users = new ArrayList<>();

        public void register(String user) { users.add(user); }

        /** Доставляет сообщение всем, кроме отправителя; возвращает получателей. */
        public List<String> send(String from, String msg) {
            List<String> delivered = new ArrayList<>();
            for (String u : users) if (!u.equals(from)) delivered.add(u);
            return delivered;
        }

        /** Связей всего N — каждый знает только комнату. */
        public int connections() { return users.size(); }
    }

    private GoodMediator() {}
}
