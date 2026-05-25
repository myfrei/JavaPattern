package com.patterns.demo.patterns.behavioral.memento;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

/**
 * Печатает текст, делает снимки и откатывается: через честный Memento с копией
 * значения (good) и через «снимок-ссылку» на изменяемое состояние (bad). Отдаёт
 * отчёт для визуализации ленты снимков.
 */
@Service
public class MementoDemoService {

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Memento — снимок состояния и точный откат");
        r.setDescription(
            "Editor печатает A, B, C и по пути делает снимки. Снимок хранит копию текста, поэтому " +
            "restore(S2) возвращает ровно то состояние, что было на момент S2 — \"AB\".");
        r.setCode(GOOD_CODE);

        GoodMemento.Editor editor = new GoodMemento.Editor();
        GoodMemento.History history = new GoodMemento.History();

        long t = 0;
        editor.type("A");
        r.getSteps().add(new PatternDemoResponse.Step(t++, "Editor", "type(\"A\")", "content=\"" + editor.content() + "\"", true));
        history.push(editor.save());
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("S1", "\"" + editor.content() + "\""));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "S1", "save()", "снимок \"" + editor.content() + "\"", true));

        editor.type("B");
        r.getSteps().add(new PatternDemoResponse.Step(t++, "Editor", "type(\"B\")", "content=\"" + editor.content() + "\"", true));
        GoodMemento.Memento s2 = editor.save();
        history.push(s2);
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("S2", "\"" + editor.content() + "\""));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "S2", "save()", "снимок \"" + editor.content() + "\"", true));

        editor.type("C");
        r.getSteps().add(new PatternDemoResponse.Step(t++, "Editor", "type(\"C\")", "content=\"" + editor.content() + "\"", true));

        editor.restore(s2);
        r.getSteps().add(new PatternDemoResponse.Step(t, "S2", "restore(S2)", "content=\"" + editor.content() + "\"", true));

        boolean ok = editor.content().equals("AB");
        r.setVerdict(ok ? "PASS — restore вернул \"AB\"" : "FAIL — состояние не восстановилось");
        r.setExplanation(
            "Снимок хранит копию значения, поэтому последующие правки на него не влияют. Caretaker (History) " +
            "управляет снимками, не зная их внутренностей — инкапсуляция Editor сохранена.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Memento — антипаттерн: снимок как ссылка на состояние");
        r.setDescription(
            "«Снимок» — это ссылка на изменяемый список строк редактора. После новых правок снимок " +
            "меняется вместе с оригиналом, и restore возвращает не прошлое, а текущее состояние.");
        r.setCode(BAD_CODE);

        BadMemento.Editor editor = new BadMemento.Editor();
        long t = 0;
        editor.type("A");
        r.getSteps().add(new PatternDemoResponse.Step(t++, "Editor", "type(\"A\")", "lines=" + editor.lines, true));
        var snapshot = editor.lines; // «снимок» = ссылка на тот же список
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("snapshot", "ссылка на lines"));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "snapshot", "save = editor.lines", "взяли ссылку (не копию)", false));

        editor.type("B");
        r.getSteps().add(new PatternDemoResponse.Step(t++, "Editor", "type(\"B\")", "lines=" + editor.lines, false));

        String restored = String.join("", snapshot); // снимок уже содержит "A","B"
        r.getSteps().add(new PatternDemoResponse.Step(t, "snapshot", "restore", "получили \"" + restored + "\" вместо \"A\"", false));

        r.setVerdict("FAIL — снимок делил ссылку: восстановилось \"" + restored + "\"");
        r.setExplanation(
            "Снимок и оригинал делят один список, поэтому снимок «портится» вместе с состоянием. Memento " +
            "требует хранить именно копию значения и прятать её за непрозрачным объектом.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Memento");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            class Editor {
                private String content = "";
                Memento save()            { return new Memento(content); } // копия значения
                void restore(Memento m)   { this.content = m.state(); }
            }

            history.push(editor.save());   // S1 = "A"
            editor.type("B");
            Memento s2 = editor.save();    // S2 = "AB"
            editor.type("C");
            editor.restore(s2);            // → "AB"
            """;

    private static final String BAD_CODE = """
            // «Снимок» — это ссылка на изменяемое состояние
            List<String> snapshot = editor.lines;   // не копия!
            editor.type("B");                        // snapshot тоже стал ["A","B"]
            restore(snapshot);                       // вернули "AB" вместо "A"
            """;
}
