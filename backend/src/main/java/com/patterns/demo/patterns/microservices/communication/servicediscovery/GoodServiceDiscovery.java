package com.patterns.demo.patterns.microservices.communication.servicediscovery;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Хорошая реализация Service Discovery (внутрипроцессная симуляция).
 *
 * Инстансы регистрируются в реестре, клиент резолвит сервис по имени и получает
 * живой адрес (round-robin по пулу). Инстансы можно поднимать и снимать в
 * рантайме — клиент к адресам не привязан, переезд/масштабирование прозрачны.
 */
public final class GoodServiceDiscovery {

    public static final class Registry {
        private final Map<String, List<String>> pool = new LinkedHashMap<>();
        private int rr = 0;

        public void register(String service, String instance) {
            pool.computeIfAbsent(service, k -> new ArrayList<>()).add(instance);
        }

        public boolean deregister(String service, String instance) {
            return pool.getOrDefault(service, new ArrayList<>()).remove(instance);
        }

        public List<String> instances(String service) {
            return pool.getOrDefault(service, List.of());
        }

        /** Резолв живого адреса round-robin; null если инстансов нет. */
        public String resolve(String service) {
            List<String> in = pool.getOrDefault(service, List.of());
            if (in.isEmpty()) return null;
            return in.get(rr++ % in.size());
        }
    }

    private GoodServiceDiscovery() {}
}
