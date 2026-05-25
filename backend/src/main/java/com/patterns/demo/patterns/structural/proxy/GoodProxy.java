package com.patterns.demo.patterns.structural.proxy;

import java.util.HashMap;
import java.util.Map;

/**
 * Хорошая реализация Proxy (кэширующий заместитель).
 *
 * Прокси реализует тот же интерфейс, что и реальный сервис, и подставляется
 * клиенту вместо него. Повторные запросы он отдаёт из кэша, не дёргая дорогой
 * RealService. Клиент работает с интерфейсом и не знает, что перед ним прокси.
 */
public final class GoodProxy {

    public interface Service {
        String fetch(int id);
    }

    /** Дорогой реальный сервис — считает реальные обращения. */
    public static final class RealService implements Service {
        private int calls = 0;
        public int calls() { return calls; }
        public String fetch(int id) {
            calls++;                 // "дорогая" операция
            return "data#" + id;
        }
    }

    /** Заместитель: тот же интерфейс, плюс кэш. */
    public static final class CachingProxy implements Service {
        private final RealService real;
        private final Map<Integer, String> cache = new HashMap<>();

        public CachingProxy(RealService real) { this.real = real; }

        public String fetch(int id) {
            return cache.computeIfAbsent(id, real::fetch); // дорого только при промахе
        }

        public boolean isCached(int id) { return cache.containsKey(id); }
        public RealService real() { return real; }
    }

    private GoodProxy() {}
}
