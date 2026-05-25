package com.patterns.demo.patterns.microservices.decomposition.bff;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Антипаттерн вместо BFF.
 *
 * Один общий API отдаёт всем клиентам полный объект. Мобильный клиент скачивает
 * 5 полей, а использует 2 — overfetch по трафику и батарее, плюс контракт
 * становится компромиссом между всеми клиентами.
 */
public final class BadBff {

    public static Map<String, Object> generalProduct() {
        GoodBff.Product p = GoodBff.backend();
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("name", p.name());
        dto.put("price", p.price());
        dto.put("description", p.description());
        dto.put("reviews", p.reviews());
        dto.put("specs", p.specs());
        return dto; // всем одно и то же — мобильный качает лишнее
    }

    private BadBff() {}
}
