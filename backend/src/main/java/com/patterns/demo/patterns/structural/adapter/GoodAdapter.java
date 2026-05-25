package com.patterns.demo.patterns.structural.adapter;

import java.util.Locale;

/**
 * Хорошая реализация Adapter.
 *
 * Наш код работает с интерфейсом {@link PaymentGateway}. Чужой банковский API
 * ({@link LegacyBank}) имеет несовместимую сигнатуру и другие единицы (доллары
 * вместо центов). Адаптер реализует наш интерфейс и внутри транслирует вызов к
 * чужому API — конвертация живёт в одном месте, клиент о ней не знает.
 */
public final class GoodAdapter {

    /** Интерфейс, который ожидает наш код. */
    public interface PaymentGateway {
        String pay(int cents);
    }

    /** Чужой API — менять нельзя, сигнатура и единицы другие. */
    public static final class LegacyBank {
        public String makeTransfer(double dollars, String currency) {
            return String.format(Locale.US, "transfer %.2f %s", dollars, currency);
        }
    }

    /** Адаптер: реализует наш интерфейс, внутри зовёт чужой API. */
    public static final class BankAdapter implements PaymentGateway {
        private final LegacyBank bank;

        public BankAdapter(LegacyBank bank) {
            this.bank = bank;
        }

        @Override
        public String pay(int cents) {
            return bank.makeTransfer(cents / 100.0, "USD"); // единственная точка конвертации
        }
    }

    private GoodAdapter() {}
}
