package com.patterns.demo.patterns.structural.composite;

import com.patterns.demo.dto.PatternDemoResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CompositeTest {

    // ─── good: рекурсивный размер всего дерева ───
    @Test
    void recursiveSizeCountsNestedDirectories() {
        GoodComposite.Directory root = new GoodComposite.Directory("root")
            .add(new GoodComposite.FileNode("a.txt", 10))
            .add(new GoodComposite.Directory("sub")
                .add(new GoodComposite.FileNode("b.txt", 20))
                .add(new GoodComposite.FileNode("c.txt", 5)));
        assertThat(root.size()).isEqualTo(35);
    }

    // ─── bad: плоский обход недосчитывает вложенное ───
    @Test
    void shallowTraversalMissesNested() {
        BadComposite.FolderItem root = new BadComposite.FolderItem("root");
        root.add(new BadComposite.FileItem("a.txt", 10));
        root.add(new BadComposite.FolderItem("sub")
            .add(new BadComposite.FileItem("b.txt", 20))
            .add(new BadComposite.FileItem("c.txt", 5)));
        assertThat(BadComposite.sizeShallow(root)).isEqualTo(10); // потеряли вложенные 25
    }

    // ─── контракт DemoService ───
    @Test
    void goodRunSumsWholeTree() {
        PatternDemoResponse r = new CompositeDemoService().runGood();
        assertThat(r.getVariant()).isEqualTo("good");
        assertThat(r.getInstances()).hasSize(5);
        assertThat(r.getVerdict()).startsWith("PASS").contains("35");
    }

    @Test
    void badRunUndercounts() {
        PatternDemoResponse r = new CompositeDemoService().runBad();
        assertThat(r.getVariant()).isEqualTo("bad");
        assertThat(r.getVerdict()).startsWith("FAIL");
        assertThat(r.getSteps()).anyMatch(s -> !s.isOk());
    }
}
