package com.patterns.demo.patterns.structural.facade;

/**
 * Антипаттерн вместо Facade.
 *
 * Без фасада клиент сам создаёт все подсистемы и вызывает их в правильном
 * порядке. Он жёстко связан с тремя классами сразу, и любая ошибка в
 * последовательности (например, отгрузить до оплаты) — на совести клиента.
 */
public final class BadFacade {

    public static String checkout(String sku, int cents) {
        GoodFacade.Inventory inventory = new GoodFacade.Inventory();
        GoodFacade.Payment payment = new GoodFacade.Payment();
        GoodFacade.Shipping shipping = new GoodFacade.Shipping();

        if (!inventory.reserve(sku)) return "out of stock"; // клиент знает шаг 1
        payment.charge(cents);                              // ...шаг 2
        return shipping.ship(sku);                          // ...и шаг 3, и их порядок
    }

    private BadFacade() {}
}
