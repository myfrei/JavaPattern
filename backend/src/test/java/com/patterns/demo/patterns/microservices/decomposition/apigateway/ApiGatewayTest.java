package com.patterns.demo.patterns.microservices.decomposition.apigateway;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApiGatewayTest {

    // ─── good: шлюз агрегирует три сервиса в один ответ ───
    @Test
    void gatewayAggregatesServices() {
        GoodApiGateway.Dashboard dash = new GoodApiGateway.Gateway().getDashboard("u1");
        assertThat(dash.user()).contains("u1");
        assertThat(dash.orders()).hasSize(2);
        assertThat(dash.stock()).isEqualTo(42);
    }

    // ─── bad: клиент делает три round-trip ───
    @Test
    void clientMakesThreeRoundTrips() {
        assertThat(BadApiGateway.clientFetchesEverything("u1")).hasSize(3);
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunHasGatewayAndOneClientHop() {
        PatternDemoResponse r = new ApiGatewayDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).anyMatch(i -> i.getCreatedBy().equals("gateway"));
        assertThat(r.getVerdict()).startsWith("PASS");
    }

    @Test
    void badRunHasNoGateway() {
        PatternDemoResponse r = new ApiGatewayDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getInstances()).noneMatch(i -> i.getCreatedBy().equals("gateway"));
        assertThat(r.getVerdict()).startsWith("FAIL");
    }
}
