package com.patterns.demo.patterns.behavioral.observer;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

/**
 * Публикует новости подписчикам: через слабо связанный Subject/Observer с
 * динамической подпиской (good) и через NewsFeed с жёстко зашитыми сервисами
 * (bad). Отдаёт отчёт для визуализации «субъект → подписчики».
 */
@Service
public class ObserverDemoService {

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Observer — слабая связь и динамическая подписка");
        r.setDescription(
            "NewsFeed знает только интерфейс Observer и список подписчиков. После публикации " +
            "уведомляются все подписанные; Push отписывается в рантайме и второе сообщение уже не получает.");
        r.setCode(GOOD_CODE);

        GoodObserver.NewsFeed feed = new GoodObserver.NewsFeed();
        GoodObserver.EmailObserver email = new GoodObserver.EmailObserver();
        GoodObserver.PushObserver push = new GoodObserver.PushObserver();
        GoodObserver.SmsObserver sms = new GoodObserver.SmsObserver();

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("EmailObserver", "subscriber"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("PushObserver", "subscriber"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("SmsObserver", "subscriber"));

        long t = 0;
        feed.attach(email);
        r.getSteps().add(new PatternDemoResponse.Step(t++, "EmailObserver", "attach()", "подписан", true));
        feed.attach(push);
        r.getSteps().add(new PatternDemoResponse.Step(t++, "PushObserver", "attach()", "подписан", true));
        feed.attach(sms);
        r.getSteps().add(new PatternDemoResponse.Step(t++, "SmsObserver", "attach()", "подписан", true));

        feed.publish("breaking news");
        r.getSteps().add(new PatternDemoResponse.Step(t++, "NewsFeed", "publish(\"breaking news\")", "уведомляю " + feed.subscriberCount(), true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "EmailObserver", "update()", email.last(), true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "PushObserver", "update()", push.last(), true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "SmsObserver", "update()", sms.last(), true));

        feed.detach(push);
        r.getSteps().add(new PatternDemoResponse.Step(t++, "PushObserver", "detach()", "отписан", true));
        feed.publish("update #2");
        r.getSteps().add(new PatternDemoResponse.Step(t++, "NewsFeed", "publish(\"update #2\")", "уведомляю " + feed.subscriberCount(), true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "EmailObserver", "update()", email.last(), true));
        boolean pushStale = push.last().equals("breaking news");
        r.getSteps().add(new PatternDemoResponse.Step(t, "PushObserver", "не уведомлён", "last=" + push.last() + (pushStale ? " ✓ отписан" : ""), true));

        r.setVerdict("PASS — отписка работает: Push не получил второе сообщение");
        r.setExplanation(
            "Субъект развязан с конкретными подписчиками: они добавляются и убираются в рантайме, " +
            "а новый канал — это новый Observer без правки NewsFeed.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Observer — антипаттерн: жёстко зашитые получатели");
        r.setDescription(
            "NewsFeed сам создаёт EmailSvc, PushSvc, SmsSvc и дёргает их в publish(). Подписаться или " +
            "отписаться в рантайме нельзя, добавить канал = править publish().");
        r.setCode(BAD_CODE);

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("EmailSvc", "зашит в publish()"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("PushSvc", "зашит в publish()"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("SmsSvc", "зашит в publish()"));

        var out = new BadObserver.NewsFeed().publish("breaking news");
        long t = 0;
        r.getSteps().add(new PatternDemoResponse.Step(t++, "NewsFeed", "publish()", "дёргаю зашитые сервисы", false));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "EmailSvc", "send()", out.get(0), false));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "PushSvc", "send()", out.get(1), false));
        r.getSteps().add(new PatternDemoResponse.Step(t, "SmsSvc", "send()", out.get(2), false));

        r.setVerdict("FAIL — жёсткая связь, нет динамической подписки");
        r.setExplanation(
            "Получатели зашиты в publish(): нельзя подписать/отписать канал в рантайме, любое изменение — " +
            "правка субъекта. Observer развязывает субъект и подписчиков через общий интерфейс.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Observer");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            interface Observer { void update(String news); }

            class NewsFeed {                              // subject
                private final List<Observer> subs = new ArrayList<>();
                public void attach(Observer o) { subs.add(o); }
                public void detach(Observer o) { subs.remove(o); }
                public void publish(String news) {
                    for (Observer o : subs) o.update(news); // знает только интерфейс
                }
            }

            feed.attach(email); feed.attach(push);
            feed.publish("breaking news");               // оба уведомлены
            feed.detach(push);
            feed.publish("update #2");                    // push уже не получит
            """;

    private static final String BAD_CODE = """
            // NewsFeed создаёт и дёргает конкретные сервисы сам — жёсткая связь
            class NewsFeed {
                public void publish(String news) {
                    new EmailSvc().send(news);
                    new PushSvc().send(news);
                    new SmsSvc().send(news);
                    // добавить канал? править publish(). отписаться в рантайме? никак
                }
            }
            """;
}
