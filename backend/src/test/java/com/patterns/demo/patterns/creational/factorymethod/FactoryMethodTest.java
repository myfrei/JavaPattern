package com.patterns.demo.patterns.creational.factorymethod;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FactoryMethodTest {

    // ─── good: фабричный метод ───
    @Test
    void eachFactoryCreatesItsOwnProduct() {
        assertThat(new GoodFactory.EmailFactory().create().name()).isEqualTo("EmailNotifier");
        assertThat(new GoodFactory.SmsFactory().create().name()).isEqualTo("SmsNotifier");
        assertThat(new GoodFactory.PushFactory().create().name()).isEqualTo("PushNotifier");
    }

    @Test
    void creatorLogicRoutesThroughProduct() {
        assertThat(new GoodFactory.EmailFactory().notify("hi")).contains("e-mail").contains("hi");
        assertThat(GoodFactory.factories()).hasSize(3);
    }

    // ─── bad: монолитный switch ───
    @Test
    void badSwitchHandlesKnownChannels() {
        assertThat(BadFactory.send("SMS", "hi")).contains("sms").contains("hi");
    }

    @Test
    void badSwitchRejectsUnknownChannel() {
        assertThatThrownBy(() -> BadFactory.send("FAX", "hi"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunBuildsThreeProducts() {
        PatternDemoResponse r = new FactoryMethodDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(3);
        assertThat(r.getVerdict()).startsWith("PASS");
        assertThat(r.getSteps()).isNotEmpty();
    }

    @Test
    void badRunIsMonolithic() {
        PatternDemoResponse r = new FactoryMethodDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getInstances()).hasSize(1);
        assertThat(r.getVerdict()).startsWith("FAIL");
        assertThat(r.getSteps()).isNotEmpty();
    }
}
