package com.patterns.demo.patterns.structural.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * Хорошая реализация Composite.
 *
 * Лист (FileNode) и контейнер (Directory) реализуют один интерфейс Node.
 * Клиент вызывает size() единообразно — рекурсия по дереву спрятана внутри
 * Directory. Не нужно знать тип узла и вручную обходить уровни.
 */
public final class GoodComposite {

    public interface Node {
        String name();
        int size();
    }

    /** Лист. */
    public static final class FileNode implements Node {
        private final String name;
        private final int size;
        public FileNode(String name, int size) { this.name = name; this.size = size; }
        public String name() { return name; }
        public int size() { return size; }
    }

    /** Композит: содержит детей и считает размер рекурсивно. */
    public static final class Directory implements Node {
        private final String name;
        private final List<Node> children = new ArrayList<>();
        public Directory(String name) { this.name = name; }

        public Directory add(Node child) { children.add(child); return this; }
        public List<Node> children() { return children; }
        public String name() { return name; }

        public int size() {
            int total = 0;
            for (Node child : children) total += child.size(); // единообразно, рекурсивно
            return total;
        }
    }

    private GoodComposite() {}
}
