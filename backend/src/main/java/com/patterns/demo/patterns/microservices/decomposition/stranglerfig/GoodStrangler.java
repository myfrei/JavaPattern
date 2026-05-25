package com.patterns.demo.patterns.microservices.decomposition.stranglerfig;

import java.util.HashSet;
import java.util.Set;

/**
 * Хорошая реализация Strangler Fig (внутрипроцессная симуляция).
 *
 * Фасад-роутер постепенно переводит маршруты со старой системы на новую: пока
 * маршрут не мигрирован — идёт в legacy, после миграции — в новый сервис. Система
 * всё время живая, риск размазан по маленьким шагам.
 */
public final class GoodStrangler {

    public static final class Router {
        private final Set<String> migrated = new HashSet<>();

        public void migrate(String route) { migrated.add(route); }

        /** Не мигрированный маршрут обслуживает legacy, мигрированный — new. */
        public String handle(String route) {
            return migrated.contains(route) ? "new" : "legacy";
        }

        public int migratedCount() { return migrated.size(); }
    }

    private GoodStrangler() {}
}
