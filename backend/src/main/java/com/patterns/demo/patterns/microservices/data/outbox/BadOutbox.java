package com.patterns.demo.patterns.microservices.data.outbox;

import java.util.ArrayList;
import java.util.List;

/**
 * Антипаттерн вместо Transactional Outbox — dual write.
 *
 * Сначала коммитим строку в БД, затем отдельным шагом публикуем сообщение в брокер.
 * Если процесс падает между этими двумя действиями, в БД заказ есть, а сообщение
 * не отправлено — downstream о заказе никогда не узнает.
 */
public final class BadOutbox {

    public static final class Db {
        private final List<String> rows = new ArrayList<>();
        public void save(String order) { rows.add(order); }
        public List<String> rows() { return rows; }
    }

    public static final class Broker {
        private final List<String> published = new ArrayList<>();
        public void publish(String event) { published.add(event); }
        public List<String> published() { return published; }
    }

    private BadOutbox() {}
}
