package com.patterns.demo.patterns.microservices.data.outbox;

import java.util.ArrayList;
import java.util.List;

/**
 * Хорошая реализация Transactional Outbox (внутрипроцессная симуляция).
 *
 * Бизнес-строка и исходящее сообщение пишутся в одну локальную транзакцию БД
 * (таблица outbox). Отдельный relay позже забирает сообщения из outbox и
 * публикует их. Даже при падении между записью и публикацией сообщение не теряется.
 */
public final class GoodOutbox {

    public static final class Db {
        private final List<String> rows = new ArrayList<>();
        private final List<String> outbox = new ArrayList<>();

        /** Атомарно: и бизнес-строка, и outbox-сообщение в одной транзакции. */
        public void saveOrder(String order, String event) {
            rows.add(order);
            outbox.add(event);
        }

        public List<String> rows() { return rows; }
        public List<String> outbox() { return outbox; }
    }

    public static final class Relay {
        private final List<String> published = new ArrayList<>();

        public void poll(Db db) {
            published.addAll(db.outbox);
            db.outbox.clear(); // помечаем как отправленные
        }

        public List<String> published() { return published; }
    }

    private GoodOutbox() {}
}
