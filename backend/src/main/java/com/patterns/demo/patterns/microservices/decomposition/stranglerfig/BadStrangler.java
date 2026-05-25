package com.patterns.demo.patterns.microservices.decomposition.stranglerfig;

/**
 * Антипаттерн вместо Strangler Fig — big-bang переписывание.
 *
 * Старую систему замораживают, переписывают целиком и переключают всё разом.
 * Долгая заморозка фич, огромный риск и простой при выкатке: либо всё взлетело,
 * либо всё упало.
 */
public final class BadStrangler {

    public static String bigBang(int routes) {
        return "переключено разом " + routes + " маршрутов (заморозка + риск)";
    }

    private BadStrangler() {}
}
