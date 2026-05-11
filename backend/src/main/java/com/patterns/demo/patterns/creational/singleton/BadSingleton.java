package com.patterns.demo.patterns.creational.singleton;

/**
 * Плохой Singleton: классический "ленивый" вариант БЕЗ синхронизации.
 *
 * Под нагрузкой несколько потоков одновременно проходят проверку null
 * и каждый создаёт собственный экземпляр. Получаем не singleton,
 * а multi-ton, при этом без единого предупреждения от компилятора.
 *
 * Бонусом: поле не volatile, поэтому даже опубликованная ссылка
 * может быть невидима другим потокам.
 */
public final class BadSingleton {

    private static BadSingleton instance;          // нет volatile, нет synchronized

    private final long createdAt = System.nanoTime();

    private BadSingleton() {
        // дорогая инициализация — расширяем "окно гонки"
        try { Thread.sleep(20); } catch (InterruptedException ignored) {}
    }

    public static BadSingleton getInstance() {
        if (instance == null) {                    // race condition: T1 и T2 оба видят null
            instance = new BadSingleton();         // и оба создают новый объект
        }
        return instance;
    }

    static void resetForDemo() { instance = null; }

    public long getCreatedAt() { return createdAt; }
}
