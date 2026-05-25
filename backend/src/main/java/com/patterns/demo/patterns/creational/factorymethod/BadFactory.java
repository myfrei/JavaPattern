package com.patterns.demo.patterns.creational.factorymethod;

/**
 * Антипаттерн вместо Factory Method.
 *
 * Один сервис знает про все каналы сразу и создаёт конкретные объекты через
 * {@code switch}. Добавить канал = править этот метод и рисковать сломать
 * соседние ветки. Высокая связанность, нулевое переиспользование, тяжело
 * тестировать в изоляции.
 */
public final class BadFactory {

    public static String send(String channel, String message) {
        switch (channel) {                       // знает все варианты разом
            case "EMAIL": return "✉ e-mail: " + message;
            case "SMS":   return "✆ sms: " + message;
            case "PUSH":  return "▲ push: " + message;
            default:
                throw new IllegalArgumentException("Неизвестный канал: " + channel);
        }
    }

    private BadFactory() {}
}
