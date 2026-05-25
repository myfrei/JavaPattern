package com.patterns.demo.patterns.microservices.communication.servicediscovery;

/**
 * Антипаттерн вместо Service Discovery.
 *
 * Клиент хардкодит адрес сервиса (host:port). Пока инстанс жив по этому адресу —
 * всё работает, но при пересоздании/масштабировании (новый адрес) вызовы по
 * старому адресу падают: клиент жёстко привязан к расположению инстанса.
 */
public final class BadServiceDiscovery {

    public static final String HARDCODED = "10.0.0.1:8080";

    /** Вызов проходит только если адрес совпадает с текущим живым. */
    public static boolean callSucceeds(String address, String liveAddress) {
        return address.equals(liveAddress);
    }

    private BadServiceDiscovery() {}
}
