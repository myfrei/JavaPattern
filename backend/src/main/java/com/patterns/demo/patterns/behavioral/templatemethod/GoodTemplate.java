package com.patterns.demo.patterns.behavioral.templatemethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Хорошая реализация Template Method.
 *
 * Базовый класс задаёт скелет алгоритма в final-методе prepare() и оставляет
 * подклассам только переопределяемые шаги-хуки (brew, addCondiments). Инвариантные
 * шаги (вскипятить воду, налить) описаны один раз и не могут «потеряться».
 */
public final class GoodTemplate {

    public abstract static class Beverage {
        /** Шаблонный метод — скелет фиксирован, менять его подклассы не могут. */
        public final List<String> prepare() {
            List<String> log = new ArrayList<>();
            log.add("boil water");        // фиксированный шаг
            log.add(brew());              // хук
            log.add("pour into cup");     // фиксированный шаг
            log.add(addCondiments());     // хук
            return log;
        }

        protected abstract String brew();
        protected abstract String addCondiments();
    }

    public static final class Tea extends Beverage {
        protected String brew() { return "steep tea"; }
        protected String addCondiments() { return "add lemon"; }
    }

    public static final class Coffee extends Beverage {
        protected String brew() { return "brew coffee"; }
        protected String addCondiments() { return "add sugar"; }
    }

    private GoodTemplate() {}
}
