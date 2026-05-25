package com.patterns.demo.patterns.microservices.decomposition.apigateway;

import java.util.ArrayList;
import java.util.List;

/**
 * Антипаттерн вместо API Gateway.
 *
 * Без шлюза клиент сам обращается к каждому сервису. Получается «болтливый»
 * клиент: несколько round-trip по сети на один экран, и он жёстко знает адреса и
 * число всех сервисов.
 */
public final class BadApiGateway {

    /** Возвращает список round-trip'ов, которые клиент делает сам. */
    public static List<String> clientFetchesEverything(String userId) {
        List<String> roundTrips = new ArrayList<>();
        roundTrips.add("GET user-service/users/" + userId);
        roundTrips.add("GET order-service/orders?user=" + userId);
        roundTrips.add("GET inventory-service/stock/SKU-1");
        return roundTrips; // 3 запроса от клиента
    }

    private BadApiGateway() {}
}
