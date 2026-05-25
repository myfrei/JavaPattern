package com.patterns.demo.patterns.microservices.communication.publishsubscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Антипаттерн вместо Publish-Subscribe.
 *
 * Издатель синхронно вызывает каждого получателя по очереди и сам их знает. Если
 * один получатель падает, цепочка обрывается — следующие не получают событие
 * (каскад). Связность высокая, добавить получателя = править издателя.
 */
public final class BadPubSub {

    public static final class Publisher {
        /** Синхронная цепочка: AnalyticsConsumer падает и обрывает доставку дальше. */
        public List<String> publish(String event) {
            List<String> trace = new ArrayList<>();
            trace.add("EmailConsumer → ok");
            trace.add("AnalyticsConsumer → FAIL");
            // InventoryConsumer не вызван: синхронный сбой оборвал цепочку
            return trace;
        }
    }

    private BadPubSub() {}
}
