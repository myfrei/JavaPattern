package com.patterns.demo.patterns.structural.facade;

/**
 * Хорошая реализация Facade.
 *
 * Подсистемы оформления заказа (склад, оплата, доставка) спрятаны за единым
 * фасадом OrderFacade. Клиент делает один вызов placeOrder() и не знает ни
 * состава подсистем, ни правильного порядка их вызова.
 */
public final class GoodFacade {

    public static final class Inventory {
        public boolean reserve(String sku) { return sku != null && sku.startsWith("SKU"); }
    }

    public static final class Payment {
        public String charge(int cents) { return "charged $" + cents / 100.0; }
    }

    public static final class Shipping {
        public String ship(String sku) { return "shipped " + sku; }
    }

    /** Фасад оркеструет подсистемы в правильном порядке. */
    public static final class OrderFacade {
        private final Inventory inventory = new Inventory();
        private final Payment payment = new Payment();
        private final Shipping shipping = new Shipping();

        public String placeOrder(String sku, int cents) {
            if (!inventory.reserve(sku)) return "out of stock";
            payment.charge(cents);
            return shipping.ship(sku);
        }
    }

    private GoodFacade() {}
}
