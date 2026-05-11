package com.patterns.demo.patterns.creational.singleton;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * Запускает несколько потоков, одновременно дёргающих getInstance(),
 * собирает шаги и возвращает структурированный отчёт для фронта.
 */
@Service
public class SingletonDemoService {

    private static final int THREAD_COUNT = 4;

    public PatternDemoResponse runGood() {
        GoodSingleton.resetForDemo();
        PatternDemoResponse r = baseResponse("good");
        r.setTitle("Singleton — корректная реализация (DCL + volatile)");
        r.setDescription(
            "Несколько потоков одновременно вызывают GoodSingleton.getInstance(). " +
            "Благодаря volatile-полю и двойной проверке под synchronized, " +
            "ровно один поток создаёт объект, остальные получают тот же ссылочный hash.");
        r.setCode(GOOD_CODE);

        runConcurrent(r, "GoodSingleton",
                () -> System.identityHashCode(GoodSingleton.getInstance()));

        int unique = uniqueHashes(r);
        boolean ok = unique == 1;
        r.setVerdict(ok ? "PASS — 1 экземпляр" : "FAIL — " + unique + " экземпляров");
        r.setExplanation(ok
            ? "volatile гарантирует видимость записи INSTANCE между потоками, " +
              "а synchronized-блок — атомарность проверки и создания. " +
              "Окно гонки закрыто."
            : "Неожиданно: появилось больше одного экземпляра. Проверьте JVM/окружение.");
        return r;
    }

    public PatternDemoResponse runBad() {
        BadSingleton.resetForDemo();
        PatternDemoResponse r = baseResponse("bad");
        r.setTitle("Singleton — сломанная реализация (без синхронизации)");
        r.setDescription(
            "Тот же эксперимент с BadSingleton.getInstance(). Поле instance не volatile, " +
            "проверка null и присваивание не атомарны. Под нагрузкой потоки одновременно " +
            "видят null и создают собственные объекты.");
        r.setCode(BAD_CODE);

        runConcurrent(r, "BadSingleton",
                () -> System.identityHashCode(BadSingleton.getInstance()));

        int unique = uniqueHashes(r);
        boolean broken = unique > 1;
        r.setVerdict(broken
                ? "FAIL — " + unique + " разных экземпляров"
                : "PASS на этом запуске (но баг есть — гонка плавающая)");
        r.setExplanation(broken
            ? "Все потоки прошли проверку instance == null одновременно, " +
              "и каждый создал свой объект. Singleton сломан: состояние теперь дублируется, " +
              "кеши/коннекшен-пулы могут расходиться."
            : "В этот раз повезло — потоки выстроились в очередь сами. " +
              "Это не лечит баг: на другой машине / нагрузке гонка проявится.");
        return r;
    }

    // ---------- internal ----------

    private void runConcurrent(PatternDemoResponse r, String clazz, Supplier<Integer> call) {
        long t0 = System.currentTimeMillis();
        ExecutorService pool = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch ready = new CountDownLatch(THREAD_COUNT);
        CountDownLatch start = new CountDownLatch(1);
        CompletionService<int[]> cs = new ExecutorCompletionService<>(pool);

        for (int i = 0; i < THREAD_COUNT; i++) {
            final int id = i + 1;
            cs.submit(() -> {
                ready.countDown();
                start.await();
                long enter = System.currentTimeMillis() - t0;
                int hash = call.get();
                long exit = System.currentTimeMillis() - t0;
                return new int[]{ id, hash, (int) enter, (int) exit };
            });
        }

        try {
            ready.await();
            start.countDown();                         // стартуем все потоки одновременно
            for (int i = 0; i < THREAD_COUNT; i++) {
                int[] res = cs.take().get();
                String actor = "Thread-" + res[0];
                String hashHex = "0x" + Integer.toHexString(res[1]);
                r.getSteps().add(new PatternDemoResponse.Step(
                        res[2], actor, clazz + ".getInstance()", "ref=" + hashHex, true));
                r.getSteps().add(new PatternDemoResponse.Step(
                        res[3], actor, "returned", hashHex, true));
                if (r.getInstances().stream().noneMatch(x -> x.getHash().equals(hashHex))) {
                    r.getInstances().add(new PatternDemoResponse.InstanceInfo(hashHex, actor));
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
        } finally {
            pool.shutdownNow();
        }
        // сортируем шаги по времени для красивой пиксельной шкалы
        r.getSteps().sort((a, b) -> Long.compare(a.getT(), b.getT()));
    }

    private int uniqueHashes(PatternDemoResponse r) {
        Set<String> s = new HashSet<>();
        for (PatternDemoResponse.InstanceInfo i : r.getInstances()) s.add(i.getHash());
        return s.size();
    }

    private PatternDemoResponse baseResponse(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Singleton");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE =
            "public final class GoodSingleton {\n" +
            "    private static volatile GoodSingleton INSTANCE;\n\n" +
            "    private GoodSingleton() { /* heavy init */ }\n\n" +
            "    public static GoodSingleton getInstance() {\n" +
            "        GoodSingleton local = INSTANCE;\n" +
            "        if (local == null) {\n" +
            "            synchronized (GoodSingleton.class) {\n" +
            "                local = INSTANCE;\n" +
            "                if (local == null) {\n" +
            "                    local = new GoodSingleton();\n" +
            "                    INSTANCE = local;\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "        return local;\n" +
            "    }\n" +
            "}\n";

    private static final String BAD_CODE =
            "public final class BadSingleton {\n" +
            "    private static BadSingleton instance;  // нет volatile\n\n" +
            "    private BadSingleton() { /* heavy init */ }\n\n" +
            "    public static BadSingleton getInstance() {\n" +
            "        if (instance == null) {            // race!\n" +
            "            instance = new BadSingleton(); // двое могут попасть сюда\n" +
            "        }\n" +
            "        return instance;\n" +
            "    }\n" +
            "}\n";
}
