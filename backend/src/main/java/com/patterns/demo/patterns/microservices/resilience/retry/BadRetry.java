package com.patterns.demo.patterns.microservices.resilience.retry;

/**
 * Антипаттерн вместо Retry.
 *
 * Одна попытка без повторов: любой транзиентный сбой (моргнувшая сеть, рестарт
 * пода) мгновенно роняет запрос, хотя следующая попытка прошла бы. Обратная
 * крайность — повтор без backoff, который добивает восстанавливающийся сервис.
 */
public final class BadRetry {

    public static boolean once(GoodRetry.FlakyService svc) {
        return svc.call(); // одна попытка, без повторов
    }

    private BadRetry() {}
}
