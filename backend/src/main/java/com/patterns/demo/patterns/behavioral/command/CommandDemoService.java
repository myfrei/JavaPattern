package com.patterns.demo.patterns.behavioral.command;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

/**
 * Печатает текст и откатывает последнее действие: через команды с историей
 * (good) и прямыми вызовами без отмены (bad). Отдаёт отчёт для визуализации
 * «стек команд + undo».
 */
@Service
public class CommandDemoService {

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Command — действие как объект, с историей и undo");
        r.setDescription(
            "Каждое редактирование — команда с execute()/undo(). Invoker складывает их в историю и " +
            "умеет откатить последнюю: после двух append undo возвращает текст к предыдущему состоянию.");
        r.setCode(GOOD_CODE);

        GoodCommand.Document doc = new GoodCommand.Document();
        GoodCommand.Invoker inv = new GoodCommand.Invoker();
        GoodCommand.AppendCommand c1 = new GoodCommand.AppendCommand(doc, "Hello ");
        GoodCommand.AppendCommand c2 = new GoodCommand.AppendCommand(doc, "World");

        r.getInstances().add(new PatternDemoResponse.InstanceInfo(c1.label(), "cmd #1"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo(c2.label(), "cmd #2"));

        long t = 0;
        inv.run(c1);
        r.getSteps().add(new PatternDemoResponse.Step(t++, c1.label(), "execute()", "text=\"" + doc.text() + "\"", true));
        inv.run(c2);
        r.getSteps().add(new PatternDemoResponse.Step(t++, c2.label(), "execute()", "text=\"" + doc.text() + "\"", true));
        inv.undo();
        r.getSteps().add(new PatternDemoResponse.Step(t, c2.label(), "undo()", "text=\"" + doc.text() + "\"", true));

        r.setVerdict("PASS — undo восстановил состояние: \"" + doc.text() + "\"");
        r.setExplanation(
            "Команда хранит, как себя отменить, а Invoker — историю. Поэтому появляются undo/redo, очередь " +
            "задач и журнал — всё потому, что запрос стал объектом, а не разовым вызовом.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Command — антипаттерн: прямые вызовы без отмены");
        r.setDescription(
            "Клиент зовёт doc.append() напрямую. Действия нигде не сохраняются, поэтому отменить " +
            "последнее редактирование нечем — нет истории, очереди и лога.");
        r.setCode(BAD_CODE);

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Document", "no history"));

        BadCommand.Document doc = new BadCommand.Document();
        long t = 0;
        doc.append("Hello ");
        r.getSteps().add(new PatternDemoResponse.Step(t++, "Document", "append(\"Hello \")", "text=\"" + doc.text() + "\"", false));
        doc.append("World");
        r.getSteps().add(new PatternDemoResponse.Step(t++, "Document", "append(\"World\")", "text=\"" + doc.text() + "\"", false));
        r.getSteps().add(new PatternDemoResponse.Step(t, "Document", "undo()?", "нечем отменить — истории нет", false));

        r.setVerdict("FAIL — нет undo: действия не сохранены");
        r.setExplanation(
            "Раз действие не овеществлено, его нельзя отменить, поставить в очередь или залогировать. " +
            "Command решает это, превращая вызов в объект с execute()/undo().");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Command");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            interface Command { void execute(); void undo(); }

            class AppendCommand implements Command {
                public void execute() { doc.append(text); }
                public void undo()    { doc.deleteLast(text.length()); }
            }

            class Invoker {
                private final Deque<Command> history = new ArrayDeque<>();
                void run(Command c) { c.execute(); history.push(c); }
                boolean undo() { history.pop().undo(); return true; }
            }

            inv.run(new AppendCommand(doc, "Hello "));
            inv.run(new AppendCommand(doc, "World"));
            inv.undo();                       // → "Hello "
            """;

    private static final String BAD_CODE = """
            // Прямые вызовы: действие нигде не сохраняется
            doc.append("Hello ");
            doc.append("World");
            // undo? нечем — нет истории, очереди и лога
            """;
}
