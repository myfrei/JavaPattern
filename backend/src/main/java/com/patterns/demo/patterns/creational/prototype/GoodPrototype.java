package com.patterns.demo.patterns.creational.prototype;

import java.util.ArrayList;
import java.util.List;

/**
 * Хорошая реализация Prototype.
 *
 * Объект умеет копировать себя через copy(), делая ПОЛНУЮ (deep) копию: вложенный
 * изменяемый список тегов копируется в новый ArrayList. Клон полностью независим —
 * правки клона не затрагивают оригинал.
 */
public final class GoodPrototype {

    public static final class Document {
        private String title;
        private final List<String> tags;

        public Document(String title, List<String> tags) {
            this.title = title;
            this.tags = new ArrayList<>(tags);   // копируем входной список
        }

        /** Глубокое копирование: новый список, а не общая ссылка. */
        public Document copy() {
            return new Document(this.title, new ArrayList<>(this.tags));
        }

        public void setTitle(String t) { this.title = t; }
        public void addTag(String t) { this.tags.add(t); }
        public String title() { return title; }
        public List<String> tags() { return tags; }
    }

    private GoodPrototype() {}
}
