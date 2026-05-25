package com.patterns.demo.patterns.behavioral.command;

/**
 * Антипаттерн вместо Command.
 *
 * Клиент дёргает получателя напрямую. Действие не овеществлено, поэтому нет ни
 * истории, ни undo, ни очереди, ни логирования — отменить выполненное нечем.
 */
public final class BadCommand {

    public static final class Document {
        private final StringBuilder sb = new StringBuilder();
        public void append(String s) { sb.append(s); } // прямой вызов, ничего не записывается
        public String text() { return sb.toString(); }
    }

    private BadCommand() {}
}
