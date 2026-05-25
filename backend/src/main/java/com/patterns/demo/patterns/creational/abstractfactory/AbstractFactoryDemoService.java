package com.patterns.demo.patterns.creational.abstractfactory;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

/**
 * Строит панель из кнопки и чекбокса: через согласованную фабрику (good) и
 * через ручной выбор классов с перемешиванием платформ (bad). Собирает
 * пошаговый отчёт для визуализации «семейства продуктов» на фронте.
 */
@Service
public class AbstractFactoryDemoService {

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Abstract Factory — согласованное семейство продуктов");
        r.setDescription(
            "Клиент берёт одну фабрику (Windows) и создаёт через неё и кнопку, и чекбокс. " +
            "Оба виджета гарантированно из одного семейства. Сменить платформу — подставить " +
            "другую фабрику, клиентский код не меняется.");
        r.setCode(GOOD_CODE);

        GoodAbstractFactory.GUIFactory f = GoodAbstractFactory.forOs("windows");
        String creator = f.getClass().getSimpleName();
        long t = 0;

        GoodAbstractFactory.Button button = f.createButton();
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("WinButton", f.family()));
        r.getSteps().add(new PatternDemoResponse.Step(
            t++, "WinButton", creator + ".createButton()", button.render(), true));

        GoodAbstractFactory.Checkbox checkbox = f.createCheckbox();
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("WinCheckbox", f.family()));
        r.getSteps().add(new PatternDemoResponse.Step(
            t++, "WinCheckbox", creator + ".createCheckbox()", checkbox.render(), true));

        r.setVerdict("PASS — одно семейство: " + f.family());
        r.setExplanation(
            "Все продукты создаются одной фабрикой, поэтому семейство целостно. Смена фабрики " +
            "меняет сразу всё семейство, а смешать стили платформ клиент физически не может.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Abstract Factory — антипаттерн: смешанные семейства");
        r.setDescription(
            "Клиент сам выбирает класс под каждый виджет и нечаянно берёт кнопку Windows и " +
            "чекбокс macOS. Компилируется, но UI рассинхронизирован — целостность семейства потеряна.");
        r.setCode(BAD_CODE);

        long t = 0;
        for (BadAbstractFactory.Widget w : BadAbstractFactory.buildToolbar()) {
            r.getInstances().add(new PatternDemoResponse.InstanceInfo(w.name(), w.family()));
            r.getSteps().add(new PatternDemoResponse.Step(
                t++, w.name(), "new " + w.name() + "()", w.render() + " · " + w.family(), true));
        }

        r.setVerdict("FAIL — смешаны семейства: Windows + macOS");
        r.setExplanation(
            "Выбор классов размазан по клиенту: ничто не мешает соединить виджеты разных платформ. " +
            "Abstract Factory как раз убирает эту дыру, отдавая создание целостной фабрике семейства.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Abstract Factory");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            interface Button { String render(); }
            interface Checkbox { String render(); }

            interface GUIFactory {                 // абстрактная фабрика семейства
                Button createButton();
                Checkbox createCheckbox();
            }

            class WinFactory implements GUIFactory {
                public Button createButton()    { return new WinButton(); }
                public Checkbox createCheckbox(){ return new WinCheckbox(); }
            }
            // MacFactory — аналогично, всё семейство macOS

            GUIFactory f = osIsMac ? new MacFactory() : new WinFactory();
            Button b = f.createButton();           // оба виджета — гарантированно
            Checkbox c = f.createCheckbox();       // из одного семейства
            """;

    private static final String BAD_CODE = """
            // Клиент сам выбирает классы под каждый виджет
            Button b   = new WinButton();          // Windows
            Checkbox c = new MacCheckbox();        // macOS — другое семейство!

            // компилируется, но UI рассинхронизирован:
            // ничто не гарантирует, что виджеты из одной платформы
            """;
}
