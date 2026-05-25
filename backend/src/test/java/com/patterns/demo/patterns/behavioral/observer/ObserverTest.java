package com.patterns.demo.patterns.behavioral.observer;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ObserverTest {

    // ─── good: подписка/уведомление/отписка в рантайме ───
    @Test
    void notifiesAttachedAndRespectsDetach() {
        GoodObserver.NewsFeed feed = new GoodObserver.NewsFeed();
        GoodObserver.EmailObserver email = new GoodObserver.EmailObserver();
        GoodObserver.PushObserver push = new GoodObserver.PushObserver();

        feed.attach(email);
        feed.attach(push);
        feed.publish("hi");
        assertThat(email.last()).isEqualTo("hi");
        assertThat(push.last()).isEqualTo("hi");

        feed.detach(push);
        feed.publish("bye");
        assertThat(email.last()).isEqualTo("bye");
        assertThat(push.last()).isEqualTo("hi");        // после отписки не получает
        assertThat(feed.subscriberCount()).isEqualTo(1);
    }

    // ─── bad: получатели зашиты в publish() ───
    @Test
    void hardcodedFeedAlwaysHitsSameThree() {
        var out = new BadObserver.NewsFeed().publish("hi");
        assertThat(out).hasSize(3);
        assertThat(out.get(0)).contains("email");
        assertThat(out.get(2)).contains("sms");
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunShowsDynamicSubscription() {
        PatternDemoResponse r = new ObserverDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(3);
        assertThat(r.getVerdict()).startsWith("PASS");
    }

    @Test
    void badRunIsTightlyCoupled() {
        PatternDemoResponse r = new ObserverDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getVerdict()).startsWith("FAIL");
    }
}
