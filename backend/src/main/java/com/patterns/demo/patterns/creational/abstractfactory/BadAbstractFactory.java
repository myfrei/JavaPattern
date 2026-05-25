package com.patterns.demo.patterns.creational.abstractfactory;

import java.util.List;

/**
 * Антипаттерн вместо Abstract Factory.
 *
 * Клиент сам выбирает класс под каждый виджет — и легко смешивает несовместимые
 * семейства (кнопка Windows + чекбокс macOS). Компилятор молчит, а UI получается
 * рассинхронизированным: нет гарантии целостности семейства продуктов.
 */
public final class BadAbstractFactory {

    public record Widget(String name, String family, String render) {}

    public static List<Widget> buildToolbar() {
        var win = new GoodAbstractFactory.WinFactory();
        var mac = new GoodAbstractFactory.MacFactory();
        return List.of(
            new Widget("WinButton",   win.family(), win.createButton().render()),
            new Widget("MacCheckbox", mac.family(), mac.createCheckbox().render()) // упс — другое семейство
        );
    }

    private BadAbstractFactory() {}
}
