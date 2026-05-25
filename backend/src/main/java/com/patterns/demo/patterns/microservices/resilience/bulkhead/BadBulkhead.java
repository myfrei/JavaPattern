package com.patterns.demo.patterns.microservices.resilience.bulkhead;

/**
 * Антипаттерн вместо Bulkhead.
 *
 * Один общий пул на все зависимости. Медленная зависимость (report) занимает все
 * слоты, и не связанные с ней вызовы (payment) голодают — частичный сбой
 * расползается на всю систему.
 */
public final class BadBulkhead {

    public static final class SharedPool {
        private final int capacity;
        private int inUse = 0;

        public SharedPool(int capacity) { this.capacity = capacity; }

        public boolean acquire() {
            if (inUse < capacity) { inUse++; return true; }
            return false;
        }

        public void release() { if (inUse > 0) inUse--; }
        public int inUse() { return inUse; }
        public int capacity() { return capacity; }
    }

    private BadBulkhead() {}
}
