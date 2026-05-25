package com.patterns.demo.patterns.behavioral.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Антипаттерн вместо Observer.
 *
 * NewsFeed сам создаёт и дёргает конкретные сервисы. Связь жёсткая: добавить или
 * убрать канал = править publish(); подписаться/отписаться в рантайме нельзя.
 */
public final class BadObserver {

    public static final class EmailSvc { public String send(String n) { return "email:" + n; } }
    public static final class PushSvc  { public String send(String n) { return "push:" + n; } }
    public static final class SmsSvc   { public String send(String n) { return "sms:" + n; } }

    public static final class NewsFeed {
        public List<String> publish(String news) {
            List<String> out = new ArrayList<>();
            out.add(new EmailSvc().send(news)); // конкретные классы зашиты прямо здесь
            out.add(new PushSvc().send(news));
            out.add(new SmsSvc().send(news));
            return out;
        }
    }

    private BadObserver() {}
}
