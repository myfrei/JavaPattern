package com.patterns.demo.patterns.structural.composite;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

/**
 * Считает суммарный размер дерева файлов root/ {a.txt, sub/{b.txt, c.txt}}:
 * единообразной рекурсией через Composite (good) и ручным обходом с instanceof,
 * который забывает вложенную папку (bad). Отдаёт дерево для визуализации.
 */
@Service
public class CompositeDemoService {

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Composite — единообразная рекурсия по дереву");
        r.setDescription(
            "Файл и папка реализуют один интерфейс Node. Directory.size() рекурсивно " +
            "суммирует детей, не различая их тип. Клиент зовёт root.size() и получает 35.");
        r.setCode(GOOD_CODE);

        GoodComposite.Directory root = new GoodComposite.Directory("root")
            .add(new GoodComposite.FileNode("a.txt", 10))
            .add(new GoodComposite.Directory("sub")
                .add(new GoodComposite.FileNode("b.txt", 20))
                .add(new GoodComposite.FileNode("c.txt", 5)));
        int total = root.size();

        tree(r);
        long t = 0;
        r.getSteps().add(new PatternDemoResponse.Step(t++, "root/", "size()", "обход поддерева", true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "  a.txt", "+10", "сумма = 10", true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "  sub/", "size()", "рекурсия вниз", true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "    b.txt", "+20", "сумма = 30", true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "    c.txt", "+5", "сумма = 35", true));
        r.getSteps().add(new PatternDemoResponse.Step(t, "root/", "итог", "size = " + total, true));

        r.setVerdict("PASS — рекурсивный размер дерева = " + total);
        r.setExplanation(
            "Лист и контейнер за одним интерфейсом, поэтому обход единообразен и вложенность любой " +
            "глубины обрабатывается одинаково. Клиент не пишет ни одного instanceof.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Composite — антипаттерн: ручной обход через instanceof");
        r.setDescription(
            "Без общего интерфейса клиент перебирает узлы через instanceof и забывает спуститься " +
            "во вложенную папку sub/. Размер считается неверно — учтён только верхний уровень.");
        r.setCode(BAD_CODE);

        BadComposite.FolderItem root = new BadComposite.FolderItem("root");
        root.add(new BadComposite.FileItem("a.txt", 10));
        root.add(new BadComposite.FolderItem("sub")
            .add(new BadComposite.FileItem("b.txt", 20))
            .add(new BadComposite.FileItem("c.txt", 5)));
        int shallow = BadComposite.sizeShallow(root);

        tree(r);
        long t = 0;
        r.getSteps().add(new PatternDemoResponse.Step(t++, "root/", "for item :", "обход верхнего уровня", true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "  a.txt", "instanceof File → +10", "сумма = 10", true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "  sub/", "instanceof File? нет", "папка пропущена ✗", false));
        r.getSteps().add(new PatternDemoResponse.Step(t, "root/", "итог", "size = " + shallow + " (недосчёт 25)", false));

        r.setVerdict("FAIL — недосчёт из-за плоского обхода: " + shallow + " вместо 35");
        r.setExplanation(
            "Ручной instanceof-обход не спускается в подпапки, и расширение типов узлов ломает все " +
            "места обхода. Composite убирает различение типов и прячет рекурсию в контейнере.");
        return r;
    }

    /** Дерево узлов для пиксельной визуализации (общая структура для обоих вариантов). */
    private void tree(PatternDemoResponse r) {
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("root/", "dir"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("  a.txt", "10"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("  sub/", "dir"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("    b.txt", "20"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("    c.txt", "5"));
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Composite");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            interface Node { int size(); }                  // лист и контейнер — одно

            class FileNode implements Node {
                public int size() { return size; }
            }
            class Directory implements Node {
                private final List<Node> children = ...;
                public int size() {
                    int total = 0;
                    for (Node c : children) total += c.size(); // рекурсия единообразна
                    return total;
                }
            }

            root.size();   // 35 — без единого instanceof
            """;

    private static final String BAD_CODE = """
            // Нет общего интерфейса — клиент сам обходит через instanceof
            int sizeShallow(Folder f) {
                int total = 0;
                for (Object o : f.items) {
                    if (o instanceof FileItem file) total += file.size;
                    // забыли: else if (o instanceof Folder) спуститься рекурсивно
                }
                return total;   // 10 вместо 35 — вложенная sub/ потеряна
            }
            """;
}
