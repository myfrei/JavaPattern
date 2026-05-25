package com.patterns.demo.patterns.structural.bridge;

/**
 * Хорошая реализация Bridge.
 *
 * Две независимые оси — абстракция (Shape: Circle/Square) и реализация
 * (Renderer: Vector/Raster) — связаны мостом через композицию. Любая фигура
 * работает с любым рендерером. Классов растёт «фигуры + рендереры», а не
 * «фигуры × рендереры».
 */
public final class GoodBridge {

    /** Ось реализации. */
    public interface Renderer {
        String render(String shape);
    }

    public static final class VectorRenderer implements Renderer {
        public String render(String shape) { return "vector[" + shape + "]"; }
    }

    public static final class RasterRenderer implements Renderer {
        public String render(String shape) { return "raster(" + shape + ")"; }
    }

    /** Ось абстракции держит ссылку на реализацию — это и есть «мост». */
    public abstract static class Shape {
        protected final Renderer renderer;
        protected Shape(Renderer renderer) { this.renderer = renderer; }
        public abstract String draw();
    }

    public static final class Circle extends Shape {
        public Circle(Renderer renderer) { super(renderer); }
        public String draw() { return renderer.render("circle"); }
    }

    public static final class Square extends Shape {
        public Square(Renderer renderer) { super(renderer); }
        public String draw() { return renderer.render("square"); }
    }

    private GoodBridge() {}
}
