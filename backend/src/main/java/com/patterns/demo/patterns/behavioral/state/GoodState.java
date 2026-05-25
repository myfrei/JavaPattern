package com.patterns.demo.patterns.behavioral.state;

/**
 * Хорошая реализация State.
 *
 * Поведение зависит от состояния, и каждое состояние — отдельный класс, который
 * сам знает, куда перейти. Контекст (Document) делегирует действие текущему
 * состоянию. Новое состояние = новый класс, существующие переходы не трогаются.
 */
public final class GoodState {

    public interface State {
        String name();
        State onPublish();
    }

    public static final class Draft implements State {
        public String name() { return "Draft"; }
        public State onPublish() { return new Review(); }
    }

    public static final class Review implements State {
        public String name() { return "Review"; }
        public State onPublish() { return new Published(); }
    }

    public static final class Published implements State {
        public String name() { return "Published"; }
        public State onPublish() { return this; } // финальное состояние
    }

    public static final class Document {
        private State state = new Draft();
        public void publish() { state = state.onPublish(); }
        public String state() { return state.name(); }
    }

    private GoodState() {}
}
