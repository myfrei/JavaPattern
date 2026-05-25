package com.patterns.demo.patterns.creational.factorymethod;

import java.util.List;

/**
 * Хорошая реализация Factory Method.
 *
 * Клиент работает только с абстрактным Creator ({@link NotifierFactory}) и
 * абстрактным продуктом ({@link Notifier}). Какой конкретный продукт создать —
 * решает подкласс через переопределённый фабричный метод {@code create()}.
 * Новый канал уведомления добавляется новым подклассом фабрики без правки
 * клиента и существующих фабрик (Open/Closed).
 */
public final class GoodFactory {

    /** Абстрактный продукт. */
    public interface Notifier {
        String name();
        String send(String message);
    }

    /** Абстрактный создатель с фабричным методом. */
    public abstract static class NotifierFactory {
        public abstract String channel();
        /** Фабричный метод — единственная точка, которую переопределяют подклассы. */
        public abstract Notifier create();

        /** Бизнес-логика создателя опирается на абстрактный продукт. */
        public final String notify(String message) {
            return create().send(message);
        }
    }

    // ─── Конкретные продукты ───
    static final class EmailNotifier implements Notifier {
        public String name() { return "EmailNotifier"; }
        public String send(String m) { return "✉ e-mail: " + m; }
    }

    static final class SmsNotifier implements Notifier {
        public String name() { return "SmsNotifier"; }
        public String send(String m) { return "✆ sms: " + m; }
    }

    static final class PushNotifier implements Notifier {
        public String name() { return "PushNotifier"; }
        public String send(String m) { return "▲ push: " + m; }
    }

    // ─── Конкретные создатели ───
    public static final class EmailFactory extends NotifierFactory {
        public String channel() { return "EMAIL"; }
        public Notifier create() { return new EmailNotifier(); }
    }

    public static final class SmsFactory extends NotifierFactory {
        public String channel() { return "SMS"; }
        public Notifier create() { return new SmsNotifier(); }
    }

    public static final class PushFactory extends NotifierFactory {
        public String channel() { return "PUSH"; }
        public Notifier create() { return new PushNotifier(); }
    }

    /** Набор фабрик, который перебирает демо. */
    public static List<NotifierFactory> factories() {
        return List.of(new EmailFactory(), new SmsFactory(), new PushFactory());
    }

    private GoodFactory() {}
}
