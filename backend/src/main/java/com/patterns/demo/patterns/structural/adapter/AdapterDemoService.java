package com.patterns.demo.patterns.structural.adapter;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

/**
 * Оплачивает заказ на $25.99 (2599 центов): через адаптер (good) и напрямую
 * чужим API с дублированием конвертации (bad). Собирает пошаговый отчёт для
 * визуализации «несовместимые интерфейсы + переходник».
 */
@Service
public class AdapterDemoService {

    private static final int CENTS = 2599;

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Adapter — переходник к несовместимому API");
        r.setDescription(
            "Клиент платит через наш интерфейс PaymentGateway.pay(cents). BankAdapter " +
            "реализует его и внутри транслирует вызов к чужому LegacyBank.makeTransfer(dollars). " +
            "Конвертация центов в доллары спрятана в адаптере.");
        r.setCode(GOOD_CODE);

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("PaymentGateway", "наш интерфейс"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("BankAdapter", "адаптер"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("LegacyBank", "чужой API"));

        GoodAdapter.PaymentGateway gateway = new GoodAdapter.BankAdapter(new GoodAdapter.LegacyBank());
        String out = gateway.pay(CENTS);

        long t = 0;
        r.getSteps().add(new PatternDemoResponse.Step(t++, "PaymentGateway", "pay(2599)", "клиент зовёт наш интерфейс", true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "BankAdapter", "2599¢ → $25.99", "makeTransfer(25.99, USD)", true));
        r.getSteps().add(new PatternDemoResponse.Step(t, "LegacyBank", "makeTransfer()", out, true));

        r.setVerdict("PASS — оплата через единый интерфейс: " + out);
        r.setExplanation(
            "Несовместимость спрятана в адаптере: клиент знает только PaymentGateway, конвертация " +
            "единиц живёт в одном месте. Заменить банк = новый адаптер, клиентский код не меняется.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Adapter — антипаттерн: прямые вызовы чужого API");
        r.setDescription(
            "Без адаптера клиент сам зовёт LegacyBank в каждом месте оплаты и каждый раз " +
            "вручную конвертирует центы в доллары. В одном из мест забыли поделить на 100.");
        r.setCode(BAD_CODE);

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("LegacyBank", "прямые вызовы"));

        String ok = BadAdapter.checkoutCorrect(CENTS);
        String buggy = BadAdapter.checkoutBuggy(CENTS);

        long t = 0;
        r.getSteps().add(new PatternDemoResponse.Step(t++, "LegacyBank", "checkoutA: 2599/100", ok, true));
        r.getSteps().add(new PatternDemoResponse.Step(t, "LegacyBank", "checkoutB: забыли /100", buggy + "  ← списали в 100× больше", false));

        r.setVerdict("FAIL — конвертация дублируется, есть баг: " + buggy);
        r.setExplanation(
            "Каждое место оплаты повторяет конвертацию единиц — рано или поздно где-то ошибаются. " +
            "Adapter убирает дублирование, оставляя трансляцию интерфейса в одной точке.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Adapter");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            interface PaymentGateway { String pay(int cents); }   // наш интерфейс

            class LegacyBank {                                     // чужой API, менять нельзя
                String makeTransfer(double dollars, String ccy) { ... }
            }

            class BankAdapter implements PaymentGateway {         // переходник
                private final LegacyBank bank;
                public String pay(int cents) {
                    return bank.makeTransfer(cents / 100.0, "USD"); // конвертация в одном месте
                }
            }

            PaymentGateway gateway = new BankAdapter(new LegacyBank());
            gateway.pay(2599);                                    // клиент не знает о LegacyBank
            """;

    private static final String BAD_CODE = """
            // Клиент сам зовёт чужой API и дублирует конвертацию повсюду
            new LegacyBank().makeTransfer(2599 / 100.0, "USD");   // место A — ок
            new LegacyBank().makeTransfer(2599,         "USD");   // место B — забыли /100!
            //                            ^ списали $2599 вместо $25.99
            """;
}
