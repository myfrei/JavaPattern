package com.patterns.demo.patterns.behavioral.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Хорошая реализация Observer.
 *
 * Субъект (NewsFeed) знает только про абстрактный Observer и держит список
 * подписчиков. Подписчики добавляются и убираются в рантайме, а публикация
 * уведомляет всех, кто подписан в данный момент. Слабая связанность: добавить
 * новый канал = реализовать Observer, субъект не меняется.
 */
public final class GoodObserver {

    public interface Observer {
        void update(String news);
        String name();
        String last();
    }

    public interface Subject {
        void attach(Observer o);
        void detach(Observer o);
        void publish(String news);
    }

    public static final class NewsFeed implements Subject {
        private final List<Observer> subscribers = new ArrayList<>();
        public void attach(Observer o) { subscribers.add(o); }
        public void detach(Observer o) { subscribers.remove(o); }
        public void publish(String news) {
            for (Observer o : List.copyOf(subscribers)) o.update(news);
        }
        public int subscriberCount() { return subscribers.size(); }
    }

    public static class BaseObserver implements Observer {
        private final String name;
        private String last = "";
        protected BaseObserver(String name) { this.name = name; }
        public void update(String news) { this.last = news; }
        public String name() { return name; }
        public String last() { return last; }
    }

    public static final class EmailObserver extends BaseObserver { public EmailObserver() { super("EmailObserver"); } }
    public static final class PushObserver extends BaseObserver { public PushObserver() { super("PushObserver"); } }
    public static final class SmsObserver extends BaseObserver { public SmsObserver() { super("SmsObserver"); } }

    private GoodObserver() {}
}
