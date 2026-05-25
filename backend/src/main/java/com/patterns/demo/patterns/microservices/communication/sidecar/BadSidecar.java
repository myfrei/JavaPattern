package com.patterns.demo.patterns.microservices.communication.sidecar;

import java.util.ArrayList;
import java.util.List;

/**
 * Антипаттерн вместо Sidecar.
 *
 * Каждый сервис сам тащит инфраструктурный код (TLS, rate-limit, метрики) вперемешку
 * с бизнес-логикой. Сквозные задачи дублируются по всем сервисам, а их обновление —
 * это правка каждого сервиса.
 */
public final class BadSidecar {

    public static final class OrderApp {
        public List<String> handle(String request) {
            List<String> trace = new ArrayList<>();
            trace.add("TLS terminate (inline)");      // инфра-код в каждом сервисе
            trace.add("rate-limit check (inline)");
            trace.add("biz: order:" + request);       // бизнес перемешан с инфрой
            trace.add("metrics emit (inline)");
            return trace;
        }
    }

    private BadSidecar() {}
}
