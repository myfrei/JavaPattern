package com.patterns.demo.patterns.creational.abstractfactory;

/**
 * Хорошая реализация Abstract Factory.
 *
 * Фабрика создаёт целое СЕМЕЙСТВО согласованных продуктов (Button + Checkbox
 * одной платформы). Клиент выбирает одну фабрику и получает виджеты, которые
 * гарантированно сочетаются. Сменить платформу = подставить другую фабрику,
 * клиентский код не меняется и перемешать стили невозможно.
 */
public final class GoodAbstractFactory {

    // ─── Абстрактные продукты ───
    public interface Button { String render(); }
    public interface Checkbox { String render(); }

    // ─── Абстрактная фабрика ───
    public interface GUIFactory {
        String family();
        Button createButton();
        Checkbox createCheckbox();
    }

    // ─── Семейство Windows ───
    static final class WinButton implements Button { public String render() { return "[Win ▭ button]"; } }
    static final class WinCheckbox implements Checkbox { public String render() { return "[Win ☑ checkbox]"; } }

    // ─── Семейство macOS ───
    static final class MacButton implements Button { public String render() { return "( Mac ▢ button )"; } }
    static final class MacCheckbox implements Checkbox { public String render() { return "( Mac ◉ checkbox )"; } }

    public static final class WinFactory implements GUIFactory {
        public String family() { return "Windows"; }
        public Button createButton() { return new WinButton(); }
        public Checkbox createCheckbox() { return new WinCheckbox(); }
    }

    public static final class MacFactory implements GUIFactory {
        public String family() { return "macOS"; }
        public Button createButton() { return new MacButton(); }
        public Checkbox createCheckbox() { return new MacCheckbox(); }
    }

    /** Выбор семейства в одной точке — дальше клиент работает только с GUIFactory. */
    public static GUIFactory forOs(String os) {
        return "mac".equalsIgnoreCase(os) || "macos".equalsIgnoreCase(os)
                ? new MacFactory()
                : new WinFactory();
    }

    private GoodAbstractFactory() {}
}
