package com.patterns.demo.patterns.microservices.decomposition.apigateway;

import java.util.List;

/**
 * Хорошая реализация API Gateway (внутрипроцессная симуляция).
 *
 * Клиент делает ОДИН запрос к шлюзу, а шлюз сам ходит в нужные сервисы
 * (Users / Orders / Inventory) и агрегирует ответ. Клиент развязан с топологией
 * бэкенда и числом сервисов.
 */
public final class GoodApiGateway {

    public record Dashboard(String user, List<String> orders, int stock) {}

    static final class UserService { String get(String id) { return "User(" + id + ")"; } }
    static final class OrderService { List<String> list(String id) { return List.of("order#1", "order#2"); } }
    static final class InventoryService { int stock(String sku) { return 42; } }

    public static final class Gateway {
        private final UserService users = new UserService();
        private final OrderService orders = new OrderService();
        private final InventoryService inventory = new InventoryService();

        /** Один вызов клиента → агрегированный ответ из трёх сервисов. */
        public Dashboard getDashboard(String userId) {
            return new Dashboard(
                users.get(userId),
                orders.list(userId),
                inventory.stock("SKU-1"));
        }
    }

    private GoodApiGateway() {}
}
