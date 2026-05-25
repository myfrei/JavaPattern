package com.patterns.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Метаданные о доступных паттернах: GET /api/patterns.
 *
 * Внимание: SPA-фронт строит навигацию по СТАТИЧЕСКОМУ каталогу
 * (frontend/src/data/patterns.js) и этот эндпоинт сейчас не использует. Здесь
 * каталог поддерживается как честное «зеркало» реально реализованных эндпоинтов
 * (все 37 паттернов в двух разделах). При добавлении паттерна — добавь строку сюда
 * И в data/patterns.js (см. CLAUDE.md).
 */
@RestController
public class IndexController {

    @GetMapping("/api/patterns")
    public Map<String, Object> index() {
        return Map.of("sections", List.of(
            section("design", "Паттерны проектирования", List.of(
                group("creational", "Порождающие", List.of(
                    pattern("singleton", "Singleton", "/api/patterns/creational/singleton"),
                    pattern("factory-method", "Factory Method", "/api/patterns/creational/factory-method"),
                    pattern("abstract-factory", "Abstract Factory", "/api/patterns/creational/abstract-factory"),
                    pattern("builder", "Builder", "/api/patterns/creational/builder"),
                    pattern("prototype", "Prototype", "/api/patterns/creational/prototype")
                )),
                group("structural", "Структурные", List.of(
                    pattern("adapter", "Adapter", "/api/patterns/structural/adapter"),
                    pattern("bridge", "Bridge", "/api/patterns/structural/bridge"),
                    pattern("composite", "Composite", "/api/patterns/structural/composite"),
                    pattern("decorator", "Decorator", "/api/patterns/structural/decorator"),
                    pattern("facade", "Facade", "/api/patterns/structural/facade"),
                    pattern("flyweight", "Flyweight", "/api/patterns/structural/flyweight"),
                    pattern("proxy", "Proxy", "/api/patterns/structural/proxy")
                )),
                group("behavioral", "Поведенческие", List.of(
                    pattern("chain-of-resp", "Chain of Responsibility", "/api/patterns/behavioral/chain-of-responsibility"),
                    pattern("command", "Command", "/api/patterns/behavioral/command"),
                    pattern("iterator", "Iterator", "/api/patterns/behavioral/iterator"),
                    pattern("mediator", "Mediator", "/api/patterns/behavioral/mediator"),
                    pattern("memento", "Memento", "/api/patterns/behavioral/memento"),
                    pattern("observer", "Observer", "/api/patterns/behavioral/observer"),
                    pattern("state", "State", "/api/patterns/behavioral/state"),
                    pattern("strategy", "Strategy", "/api/patterns/behavioral/strategy"),
                    pattern("template-method", "Template Method", "/api/patterns/behavioral/template-method"),
                    pattern("visitor", "Visitor", "/api/patterns/behavioral/visitor"),
                    pattern("interpreter", "Interpreter", "/api/patterns/behavioral/interpreter")
                ))
            )),
            section("microservices", "Паттерны микросервисов", List.of(
                group("decomposition", "Декомпозиция", List.of(
                    pattern("api-gateway", "API Gateway", "/api/patterns/microservices/decomposition/api-gateway"),
                    pattern("backend-for-frontend", "Backend for Frontend", "/api/patterns/microservices/decomposition/backend-for-frontend"),
                    pattern("strangler-fig", "Strangler Fig", "/api/patterns/microservices/decomposition/strangler-fig")
                )),
                group("communication", "Коммуникация", List.of(
                    pattern("service-discovery", "Service Discovery", "/api/patterns/microservices/communication/service-discovery"),
                    pattern("sidecar", "Sidecar", "/api/patterns/microservices/communication/sidecar"),
                    pattern("publish-subscribe", "Publish-Subscribe", "/api/patterns/microservices/communication/publish-subscribe")
                )),
                group("data", "Данные", List.of(
                    pattern("saga", "Saga", "/api/patterns/microservices/data/saga"),
                    pattern("cqrs", "CQRS", "/api/patterns/microservices/data/cqrs"),
                    pattern("event-sourcing", "Event Sourcing", "/api/patterns/microservices/data/event-sourcing"),
                    pattern("transactional-outbox", "Transactional Outbox", "/api/patterns/microservices/data/transactional-outbox")
                )),
                group("resilience", "Надёжность", List.of(
                    pattern("circuit-breaker", "Circuit Breaker", "/api/patterns/microservices/resilience/circuit-breaker"),
                    pattern("retry", "Retry", "/api/patterns/microservices/resilience/retry"),
                    pattern("bulkhead", "Bulkhead", "/api/patterns/microservices/resilience/bulkhead"),
                    pattern("rate-limiter", "Rate Limiter", "/api/patterns/microservices/resilience/rate-limiter")
                ))
            ))
        ));
    }

    private static Map<String, Object> section(String id, String title, List<Map<String, Object>> groups) {
        return Map.of("id", id, "title", title, "groups", groups);
    }

    private static Map<String, Object> group(String id, String title, List<Map<String, Object>> patterns) {
        return Map.of("id", id, "title", title, "patterns", patterns);
    }

    /** Каждый паттерн реализован (good/bad), поэтому ready=true. endpoint совпадает с @RequestMapping контроллера. */
    private static Map<String, Object> pattern(String id, String title, String endpoint) {
        return Map.of("id", id, "title", title, "endpoint", endpoint, "ready", true);
    }
}
