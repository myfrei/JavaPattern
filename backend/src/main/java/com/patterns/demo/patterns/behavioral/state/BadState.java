package com.patterns.demo.patterns.behavioral.state;

/**
 * Антипаттерн вместо State.
 *
 * Состояние — строка, а все переходы зашиты в один switch. Добавить состояние
 * или поменять переход — править этот метод и рисковать сломать соседние ветки.
 * Поведение размазано, инвариант «какие переходы вообще возможны» нигде не виден.
 */
public final class BadState {

    public static final class Document {
        private String state = "Draft";

        public void publish() {
            switch (state) {                 // все переходы в одном месте
                case "Draft":     state = "Review"; break;
                case "Review":    state = "Published"; break;
                case "Published": break;     // no-op
                default: throw new IllegalStateException("неизвестное состояние: " + state);
            }
        }

        public String state() { return state; }
    }

    private BadState() {}
}
