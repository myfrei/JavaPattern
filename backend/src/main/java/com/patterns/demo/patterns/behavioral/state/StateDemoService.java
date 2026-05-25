package com.patterns.demo.patterns.behavioral.state;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

/**
 * Прогоняет документ по workflow Draft → Review → Published: через классы
 * состояний (good) и через монолитный switch (bad). Отдаёт отчёт для
 * визуализации графа состояний.
 */
@Service
public class StateDemoService {

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("State — поведение в классах состояний");
        r.setDescription(
            "Document делегирует publish() текущему состоянию, а каждое состояние знает свой переход. " +
            "Draft → Review → Published, повторный publish в Published — no-op.");
        r.setCode(GOOD_CODE);

        states(r);

        GoodState.Document doc = new GoodState.Document();
        long t = 0;
        String from = doc.state();
        doc.publish();
        r.getSteps().add(new PatternDemoResponse.Step(t++, doc.state(), "publish() [" + from + "]", from + " → " + doc.state(), true));
        from = doc.state();
        doc.publish();
        r.getSteps().add(new PatternDemoResponse.Step(t++, doc.state(), "publish() [" + from + "]", from + " → " + doc.state(), true));
        from = doc.state();
        doc.publish();
        r.getSteps().add(new PatternDemoResponse.Step(t, doc.state(), "publish() [" + from + "]", "уже " + doc.state() + " (no-op)", true));

        r.setVerdict("PASS — переходы инкапсулированы в состояниях, итог: " + doc.state());
        r.setExplanation(
            "Каждое состояние — класс, знающий только свои переходы. Добавить состояние = новый класс без " +
            "правки остальных; невозможные переходы просто не описаны, а не теряются среди веток switch.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("State — антипаттерн: монолитный switch по строке");
        r.setDescription(
            "Состояние — строка, переходы зашиты в один switch. Тот же workflow работает, но добавить " +
            "состояние или изменить переход = править общий метод с риском задеть соседние ветки.");
        r.setCode(BAD_CODE);

        states(r);

        BadState.Document doc = new BadState.Document();
        long t = 0;
        String from = doc.state();
        doc.publish();
        r.getSteps().add(new PatternDemoResponse.Step(t++, doc.state(), "switch(\"" + from + "\")", from + " → " + doc.state(), false));
        from = doc.state();
        doc.publish();
        r.getSteps().add(new PatternDemoResponse.Step(t++, doc.state(), "switch(\"" + from + "\")", from + " → " + doc.state(), false));
        from = doc.state();
        doc.publish();
        r.getSteps().add(new PatternDemoResponse.Step(t, doc.state(), "switch(\"" + from + "\")", "уже " + doc.state(), false));

        r.setVerdict("FAIL — один switch знает все переходы");
        r.setExplanation(
            "Все переходы и поведение свалены в один метод. Новое состояние = ещё одна ветка во всех " +
            "switch'ах по коду. State выносит каждое состояние в отдельный класс с собственными переходами.");
        return r;
    }

    private void states(PatternDemoResponse r) {
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Draft", "state"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Review", "state"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Published", "state"));
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("State");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            interface State { State onPublish(); }

            class Draft implements State {
                public State onPublish() { return new Review(); }
            }
            class Review implements State {
                public State onPublish() { return new Published(); }
            }

            class Document {
                private State state = new Draft();
                void publish() { state = state.onPublish(); } // делегирует состоянию
            }
            """;

    private static final String BAD_CODE = """
            // Состояние — строка, переходы в одном switch
            class Document {
                private String state = "Draft";
                void publish() {
                    switch (state) {
                        case "Draft":  state = "Review";    break;
                        case "Review": state = "Published"; break;
                        // новое состояние? правим switch (и все остальные)
                    }
                }
            }
            """;
}
