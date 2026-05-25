package com.patterns.demo.patterns.microservices.decomposition.bff;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Хорошая реализация Backend for Frontend (внутрипроцессная симуляция).
 *
 * У каждого типа клиента — свой шлюз (BFF), который отдаёт ровно тот payload,
 * что нужен этому клиенту. Мобильный BFF возвращает 2 поля, веб-BFF — полный
 * набор. Нет overfetch и нет компромиссного «общего» контракта.
 */
public final class GoodBff {

    public record Product(String name, int price, String description, List<String> reviews, String specs) {}

    static Product backend() {
        return new Product("Phone", 599, "Длинное описание…", List.of("r1", "r2"), "specs…");
    }

    public static final class MobileBff {
        public Map<String, Object> product() {
            Product p = backend();
            Map<String, Object> dto = new LinkedHashMap<>();
            dto.put("name", p.name());
            dto.put("price", p.price());
            return dto; // только то, что нужно мобильному экрану
        }
    }

    public static final class WebBff {
        public Map<String, Object> product() {
            Product p = backend();
            Map<String, Object> dto = new LinkedHashMap<>();
            dto.put("name", p.name());
            dto.put("price", p.price());
            dto.put("description", p.description());
            dto.put("reviews", p.reviews());
            dto.put("specs", p.specs());
            return dto;
        }
    }

    private GoodBff() {}
}
