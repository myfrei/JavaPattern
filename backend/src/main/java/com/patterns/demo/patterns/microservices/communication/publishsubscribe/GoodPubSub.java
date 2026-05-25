package com.patterns.demo.patterns.microservices.communication.publishsubscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Хорошая реализация Publish-Subscribe (внутрипроцессная симуляция).
 *
 * Издатель публикует событие в брокер, а тот доставляет его всем подписчикам
 * независимо (fan-out). Издатель не знает подписчиков, а сбой одного из них не
 * мешает остальным — добавить подписчика можно без правки издателя.
 */
public final class GoodPubSub {

    public static final class Broker {
        private final List<String> subscribers = new ArrayList<>();

        public void subscribe(String subscriber) { subscribers.add(subscriber); }

        /** Доставляет событие всем подписчикам; возвращает список доставок. */
        public List<String> publish(String event) {
            List<String> delivered = new ArrayList<>();
            for (String s : subscribers) delivered.add(s); // независимый fan-out
            return delivered;
        }

        public int subscriberCount() { return subscribers.size(); }
    }

    private GoodPubSub() {}
}
