package com.patterns.demo.patterns.microservices.resilience.bulkhead;

/**
 * Хорошая реализация Bulkhead (внутрипроцессная симуляция).
 *
 * У каждой зависимости — свой пул ограниченной ёмкости («отсек»). Если одна
 * зависимость насыщается (медленный report исчерпал свой пул), это не затрагивает
 * другие: вызовы payment идут через отдельный пул и не голодают.
 */
public final class GoodBulkhead {

    public static final class Pool {
        private final String name;
        private final int capacity;
        private int inUse = 0;

        public Pool(String name, int capacity) {
            this.name = name;
            this.capacity = capacity;
        }

        public boolean acquire() {
            if (inUse < capacity) { inUse++; return true; }
            return false; // отсек заполнен
        }

        public void release() { if (inUse > 0) inUse--; }
        public int inUse() { return inUse; }
        public int capacity() { return capacity; }
        public String name() { return name; }
    }

    private GoodBulkhead() {}
}
