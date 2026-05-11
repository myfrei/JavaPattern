package com.patterns.demo.patterns.creational.singleton;

/**
 * Хороший Singleton: thread-safe через "double-checked locking"
 * с volatile-полем. Гарантирует ровно один экземпляр даже в многопоточной среде.
 *
 * Альтернативно правильные варианты — enum-singleton или статический holder.
 * Здесь оставлен DCL как самый наглядный.
 */
public final class GoodSingleton {

    /** volatile важен: без него JIT может опубликовать недоинициализированную ссылку. */
    private static volatile GoodSingleton INSTANCE;

    private final long createdAt = System.nanoTime();

    private GoodSingleton() {
        // имитация дорогой инициализации
        try { Thread.sleep(20); } catch (InterruptedException ignored) {}
    }

    public static GoodSingleton getInstance() {
        GoodSingleton local = INSTANCE;
        if (local == null) {                       // 1-я проверка — без блокировки (быстро)
            synchronized (GoodSingleton.class) {
                local = INSTANCE;
                if (local == null) {               // 2-я проверка — под блокировкой
                    local = new GoodSingleton();
                    INSTANCE = local;
                }
            }
        }
        return local;
    }

    /** Только для демо: сбросить состояние между запусками. */
    static void resetForDemo() {
        synchronized (GoodSingleton.class) {
            INSTANCE = null;
        }
    }

    public long getCreatedAt() { return createdAt; }
}
