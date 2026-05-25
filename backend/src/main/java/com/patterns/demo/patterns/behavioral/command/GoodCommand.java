package com.patterns.demo.patterns.behavioral.command;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Хорошая реализация Command.
 *
 * Действие упаковано в объект-команду с методами execute() и undo(). Invoker
 * хранит историю выполненных команд и умеет откатывать последнюю. Запрос стал
 * объектом — его можно складывать в очередь, логировать и отменять.
 */
public final class GoodCommand {

    public static final class Document {
        private final StringBuilder sb = new StringBuilder();
        public void append(String s) { sb.append(s); }
        public void deleteLast(int n) { sb.delete(Math.max(0, sb.length() - n), sb.length()); }
        public String text() { return sb.toString(); }
    }

    public interface Command {
        void execute();
        void undo();
        String label();
    }

    public static final class AppendCommand implements Command {
        private final Document doc;
        private final String text;
        public AppendCommand(Document doc, String text) { this.doc = doc; this.text = text; }
        public void execute() { doc.append(text); }
        public void undo() { doc.deleteLast(text.length()); }
        public String label() { return "append(\"" + text + "\")"; }
    }

    public static final class Invoker {
        private final Deque<Command> history = new ArrayDeque<>();
        public void run(Command c) { c.execute(); history.push(c); }
        public boolean undo() {
            if (history.isEmpty()) return false;
            history.pop().undo();
            return true;
        }
        public int historySize() { return history.size(); }
    }

    private GoodCommand() {}
}
