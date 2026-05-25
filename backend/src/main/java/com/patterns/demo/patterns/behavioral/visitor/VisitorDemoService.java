package com.patterns.demo.patterns.behavioral.visitor;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

/**
 * Считает площадь фигур [Circle(2), Square(3)]: через посетителя с двойной
 * диспетчеризацией (good) и через лестницу instanceof (bad). Отдаёт отчёт для
 * визуализации «посетитель обходит элементы».
 */
@Service
public class VisitorDemoService {

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Visitor — двойная диспетчеризация без instanceof");
        r.setDescription(
            "AreaVisitor обходит фигуры: каждая через accept() сама выбирает visitCircle/visitSquare. " +
            "Добавить операцию (например, описание) = новый Visitor, классы фигур не меняются.");
        r.setCode(GOOD_CODE);

        GoodVisitor.Shape[] shapes = { new GoodVisitor.Circle(2), new GoodVisitor.Square(3) };
        GoodVisitor.AreaVisitor area = new GoodVisitor.AreaVisitor();
        GoodVisitor.NameVisitor name = new GoodVisitor.NameVisitor();

        long t = 0;
        int total = 0;
        for (GoodVisitor.Shape s : shapes) {
            String label = s.accept(name);
            int a = s.accept(area);
            total += a;
            r.getInstances().add(new PatternDemoResponse.InstanceInfo(label, "element"));
            r.getSteps().add(new PatternDemoResponse.Step(t++, label, "accept(AreaVisitor)", "area = " + a, true));
        }

        r.setVerdict("PASS — посетитель обошёл все типы, сумма площадей = " + total);
        r.setExplanation(
            "Двойная диспетчеризация направляет вызов в нужный метод посетителя без instanceof. Новая " +
            "операция — это новый Visitor; иерархия фигур остаётся закрытой для изменений.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Visitor — антипаттерн: лестница instanceof");
        r.setDescription(
            "Площадь считается через if (shape instanceof ...). Новая фигура = ветка в каждой такой " +
            "операции, а забытый тип упадёт лишь в рантайме.");
        r.setCode(BAD_CODE);

        Object[] shapes = { new GoodVisitor.Circle(2), new GoodVisitor.Square(3) };
        String[] labels = { "Circle(r=2)", "Square(side=3)" };

        long t = 0;
        int total = 0;
        for (int i = 0; i < shapes.length; i++) {
            int a = BadVisitor.area(shapes[i]);
            total += a;
            r.getInstances().add(new PatternDemoResponse.InstanceInfo(labels[i], "instanceof"));
            r.getSteps().add(new PatternDemoResponse.Step(t++, labels[i], "instanceof → area", "= " + a, false));
        }

        r.setVerdict("FAIL — операция через лестницу instanceof");
        r.setExplanation(
            "instanceof-лестница дублируется для каждой операции и ломается при добавлении фигуры. Visitor " +
            "заменяет её двойной диспетчеризацией: тип выбирается полиморфно через accept().");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Visitor");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            interface Shape { <R> R accept(Visitor<R> v); }
            interface Visitor<R> { R visitCircle(Circle c); R visitSquare(Square s); }

            class Circle implements Shape {
                public <R> R accept(Visitor<R> v) { return v.visitCircle(this); } // двойная диспетчеризация
            }
            class AreaVisitor implements Visitor<Integer> {
                public Integer visitCircle(Circle c) { return (int) Math.round(PI * c.radius * c.radius); }
                public Integer visitSquare(Square s) { return s.side * s.side; }
            }

            shape.accept(new AreaVisitor());     // новая операция = новый Visitor
            """;

    private static final String BAD_CODE = """
            // Операция через лестницу instanceof
            int area(Object shape) {
                if (shape instanceof Circle c) return (int) Math.round(PI * c.radius * c.radius);
                else if (shape instanceof Square s) return s.side * s.side;
                else throw new IllegalArgumentException();
                // новая фигура? правим КАЖДУЮ такую операцию
            }
            """;
}
