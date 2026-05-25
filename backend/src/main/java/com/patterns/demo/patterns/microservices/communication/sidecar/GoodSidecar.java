package com.patterns.demo.patterns.microservices.communication.sidecar;

import java.util.ArrayList;
import java.util.List;

/**
 * Хорошая реализация Sidecar (внутрипроцессная симуляция).
 *
 * Приложение содержит только бизнес-логику, а сквозные задачи (TLS, rate-limit,
 * метрики) вынесены в сайдкар, который оборачивает запрос вокруг приложения. Тот
 * же сайдкар переиспользуется любым сервисом без правки его кода.
 */
public final class GoodSidecar {

    public interface App {
        String handle(String request);
    }

    public static final class OrderApp implements App {
        public String handle(String request) { return "order:" + request; } // только бизнес
    }

    public static final class Sidecar {
        private final App app;
        public Sidecar(App app) { this.app = app; }

        public List<String> process(String request) {
            List<String> trace = new ArrayList<>();
            trace.add("TLS terminate");        // сквозное — в сайдкаре
            trace.add("rate-limit check");
            trace.add("biz: " + app.handle(request)); // бизнес-логика приложения
            trace.add("metrics emit");
            return trace;
        }
    }

    private GoodSidecar() {}
}
