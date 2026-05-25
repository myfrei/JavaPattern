package com.patterns.demo.patterns.structural.proxy;

/**
 * Антипаттерн вместо Proxy.
 *
 * Клиент дёргает дорогой сервис напрямую при каждом запросе. Нет ни кэша, ни
 * ленивой загрузки, ни контроля доступа — повторный запрос за теми же данными
 * снова оплачивает полную стоимость.
 */
public final class BadProxy {

    public static final class DirectService {
        private int calls = 0;
        public int calls() { return calls; }
        public String fetch(int id) {
            calls++;                 // каждый вызов — дорого, всегда
            return "data#" + id;
        }
    }

    private BadProxy() {}
}
