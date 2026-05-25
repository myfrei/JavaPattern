package com.patterns.demo.patterns.structural.adapter;

/**
 * Антипаттерн вместо Adapter.
 *
 * Клиент сам дёргает чужой API в каждом месте оплаты и каждый раз вручную
 * конвертирует центы в доллары. Логика конвертации размазана по кодовой базе —
 * стоит в одном месте забыть поделить на 100, и клиент спишет в 100 раз больше.
 */
public final class BadAdapter {

    /** Первое место вызова — конвертация продублирована здесь. */
    public static String checkoutCorrect(int cents) {
        GoodAdapter.LegacyBank bank = new GoodAdapter.LegacyBank();
        return bank.makeTransfer(cents / 100.0, "USD");
    }

    /** Второе место вызова — забыли поделить на 100: тихий баг. */
    public static String checkoutBuggy(int cents) {
        GoodAdapter.LegacyBank bank = new GoodAdapter.LegacyBank();
        return bank.makeTransfer(cents, "USD"); // BUG: центы переданы как доллары
    }

    private BadAdapter() {}
}
