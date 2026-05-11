package com.patterns.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Метаданные о доступных паттернах — фронт строит по ним меню.
 */
@RestController
public class IndexController {

    @GetMapping("/api/patterns")
    public Map<String, Object> index() {
        return Map.of(
            "sections", List.of(
                Map.of(
                    "id", "design",
                    "title", "Паттерны проектирования",
                    "groups", List.of(
                        Map.of("id", "behavioral", "title", "Поведенческие",
                               "patterns", List.of()),
                        Map.of("id", "structural", "title", "Структурные",
                               "patterns", List.of()),
                        Map.of("id", "creational", "title", "Порождающие",
                               "patterns", List.of(
                                   Map.of("id", "singleton", "title", "Singleton",
                                          "endpoint", "/api/patterns/creational/singleton",
                                          "ready", true)
                               ))
                    )
                ),
                Map.of(
                    "id", "microservices",
                    "title", "Паттерны микросервисов",
                    "groups", List.of()
                )
            )
        );
    }
}
