package com.patterns.demo.patterns.creational.prototype;

import java.util.List;

/**
 * Антипаттерн вместо Prototype — поверхностная (shallow) копия.
 *
 * copy() переиспользует ту же ссылку на вложенный список тегов. В итоге оригинал
 * и «клон» делят один и тот же список: правка клона тихо портит оригинал. Это
 * классическая ловушка наивного клонирования.
 */
public final class BadPrototype {

    public static final class Document {
        private String title;
        private List<String> tags;

        public Document(String title, List<String> tags) {
            this.title = title;
            this.tags = tags;                    // храним ту же ссылку
        }

        /** Поверхностное копирование: общий вложенный список — баг. */
        public Document copy() {
            return new Document(this.title, this.tags); // делим один список
        }

        public void setTitle(String t) { this.title = t; }
        public void addTag(String t) { this.tags.add(t); }
        public String title() { return title; }
        public List<String> tags() { return tags; }
    }

    private BadPrototype() {}
}
