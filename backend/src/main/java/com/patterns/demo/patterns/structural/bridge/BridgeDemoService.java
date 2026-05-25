package com.patterns.demo.patterns.structural.bridge;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

/**
 * Рисует все 4 комбинации «фигура × рендерер»: через мост из 2+2 классов (good)
 * и через 4 класса-комбинации (bad). Собирает отчёт для визуализации двух осей.
 */
@Service
public class BridgeDemoService {

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Bridge — две оси через композицию (2 + 2 класса)");
        r.setDescription(
            "Фигура держит ссылку на рендерер. Circle и Square комбинируются с Vector и Raster " +
            "без отдельных классов на каждую пару. Рост классов — сумма осей, а не произведение.");
        r.setCode(GOOD_CODE);

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Circle", "abstraction"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Square", "abstraction"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("VectorRenderer", "implementation"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("RasterRenderer", "implementation"));

        GoodBridge.Renderer vector = new GoodBridge.VectorRenderer();
        GoodBridge.Renderer raster = new GoodBridge.RasterRenderer();
        GoodBridge.Shape[] shapes = {
            new GoodBridge.Circle(vector), new GoodBridge.Circle(raster),
            new GoodBridge.Square(vector), new GoodBridge.Square(raster),
        };
        long t = 0;
        for (GoodBridge.Shape s : shapes) {
            r.getSteps().add(new PatternDemoResponse.Step(t++, "Shape", "draw()", s.draw(), true));
        }

        r.setVerdict("PASS — 4 комбинации из 2 + 2 = 4 классов");
        r.setExplanation(
            "Каждая ось расширяется независимо: новый рендерер = +1 класс на все фигуры сразу, " +
            "новая фигура = +1 класс на все рендереры. При 3×3 это 6 классов против 9 у наследования.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Bridge — антипаттерн: класс на каждую комбинацию");
        r.setDescription(
            "Без моста каждая пара «фигура × рендерер» — отдельный класс: VectorCircle, RasterCircle, " +
            "VectorSquare, RasterSquare. Иерархия растёт как произведение измерений.");
        r.setCode(BAD_CODE);

        String[][] combos = {
            {"VectorCircle", "vector[circle]"}, {"RasterCircle", "raster(circle)"},
            {"VectorSquare", "vector[square]"}, {"RasterSquare", "raster(square)"},
        };
        long t = 0;
        for (String[] c : combos) {
            r.getInstances().add(new PatternDemoResponse.InstanceInfo(c[0], "класс-комбинация"));
            r.getSteps().add(new PatternDemoResponse.Step(t++, c[0], "draw()", c[1], false));
        }

        r.setVerdict("FAIL — " + BadBridge.classCount() + " класса на 2×2 (рост как произведение)");
        r.setExplanation(
            "Каждое новое измерение умножает число классов. Добавить рендерер = завести класс под " +
            "каждую фигуру. Bridge разрывает связь, оставляя сумму вместо произведения.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Bridge");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            interface Renderer { String render(String shape); }   // ось реализации

            abstract class Shape {                                 // ось абстракции
                protected final Renderer renderer;                // ← мост
                Shape(Renderer r) { this.renderer = r; }
                abstract String draw();
            }
            class Circle extends Shape {
                String draw() { return renderer.render("circle"); }
            }

            new Circle(new VectorRenderer()).draw();              // любая пара
            new Square(new RasterRenderer()).draw();              // без новых классов
            """;

    private static final String BAD_CODE = """
            // Класс на каждую КОМБИНАЦИЮ — рост как произведение
            class VectorCircle { String draw() { return "vector[circle]"; } }
            class RasterCircle { String draw() { return "raster(circle)"; } }
            class VectorSquare { String draw() { return "vector[square]"; } }
            class RasterSquare { String draw() { return "raster(square)"; } }
            // добавили рендерер? +1 класс на КАЖДУЮ фигуру
            """;
}
