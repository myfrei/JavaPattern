package com.patterns.demo.patterns.structural.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * Антипаттерн вместо Composite.
 *
 * У файла и папки нет общего интерфейса, дети хранятся как Object. Клиент сам
 * перебирает разнотипные узлы через instanceof — и легко забывает спуститься во
 * вложенные папки. Размер считается неверно: рекурсии нет.
 */
public final class BadComposite {

    public static final class FileItem {
        public final String name;
        public final int size;
        public FileItem(String name, int size) { this.name = name; this.size = size; }
    }

    public static final class FolderItem {
        public final String name;
        public final List<Object> items = new ArrayList<>();
        public FolderItem(String name) { this.name = name; }
        public FolderItem add(Object item) { items.add(item); return this; }
    }

    /** Клиент обходит вручную и забывает про вложенные папки — недосчёт. */
    public static int sizeShallow(FolderItem folder) {
        int total = 0;
        for (Object item : folder.items) {
            if (item instanceof FileItem file) {
                total += file.size;
            }
            // забыли: else if (item instanceof FolderItem) спуститься рекурсивно
        }
        return total;
    }

    private BadComposite() {}
}
