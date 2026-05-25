package com.patterns.demo.patterns.microservices.data.cqrs;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

/**
 * Два депозита и запрос баланса: через раздельные write/read-модели с проекцией
 * (good) и через одну модель с пересчётом на каждый запрос (bad). Отдаёт отчёт
 * для визуализации «команды | запросы».
 */
@Service
public class CqrsDemoService {

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("CQRS — раздельные модели чтения и записи");
        r.setDescription(
            "Команды deposit меняют write-модель и обновляют read-проекцию. Запрос читает готовую " +
            "денормализованную проекцию (balance: 150), не пересчитывая ничего и не конкурируя с записью.");
        r.setCode(GOOD_CODE);

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("WriteSide", "commands"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("ReadSide", "queries"));

        GoodCqrs.WriteSide write = new GoodCqrs.WriteSide();
        GoodCqrs.ReadSide read = new GoodCqrs.ReadSide();

        long t = 0;
        write.deposit(100); read.project(write.balance());
        r.getSteps().add(new PatternDemoResponse.Step(t++, "WriteSide", "command deposit(100)", "balance=" + write.balance(), true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "ReadSide", "project", read.query(), true));
        write.deposit(50); read.project(write.balance());
        r.getSteps().add(new PatternDemoResponse.Step(t++, "WriteSide", "command deposit(50)", "balance=" + write.balance(), true));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "ReadSide", "project", read.query(), true));
        r.getSteps().add(new PatternDemoResponse.Step(t, "ReadSide", "query", read.query() + " (из проекции)", true));

        r.setVerdict("PASS — команды и запросы разделены, чтение из проекции");
        r.setExplanation(
            "Чтение и запись масштабируются и оптимизируются независимо: запросы бьют по денормализованной " +
            "read-модели, команды — по write-модели. Это убирает конкуренцию и тяжёлые запросы на лету.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("CQRS — антипаттерн: одна модель для чтения и записи");
        r.setDescription(
            "Одна модель и для команд, и для запросов. Каждый запрос пересчитывает баланс по всему write-логу — " +
            "чтение конкурирует с записью и деградирует с ростом данных.");
        r.setCode(BAD_CODE);

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Model", "reads + writes"));

        BadCqrs model = new BadCqrs();
        long t = 0;
        model.deposit(100);
        r.getSteps().add(new PatternDemoResponse.Step(t++, "Model", "deposit(100)", "log=" + model.logSize(), true));
        model.deposit(50);
        r.getSteps().add(new PatternDemoResponse.Step(t++, "Model", "deposit(50)", "log=" + model.logSize(), true));
        r.getSteps().add(new PatternDemoResponse.Step(t, "Model", "query", "пересчёт по " + model.logSize() + " записям → " + model.query(), false));

        r.setVerdict("FAIL — одна модель: запрос пересчитывает по всему write-логу");
        r.setExplanation(
            "Совмещённая модель заставляет читать через тот же сторонадж, что и пишет: тяжёлые запросы на лету и " +
            "конкуренция за блокировки. CQRS разделяет стороны и держит готовую read-проекцию.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("CQRS");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            class WriteSide {                       // команды
                void deposit(int amount) { balance += amount; }
            }
            class ReadSide {                        // запросы
                void project(int balance) { view = "balance: " + balance; } // денормализация
                String query() { return view; }     // быстрый read
            }

            write.deposit(100); read.project(write.balance());
            read.query();   // "balance: 150" из проекции
            """;

    private static final String BAD_CODE = """
            // Одна модель: запрос пересчитывает по всему логу
            class Model {
                List<Integer> deposits = new ArrayList<>();
                void deposit(int a) { deposits.add(a); }
                int query() {
                    int sum = 0;
                    for (int d : deposits) sum += d;   // O(n) на каждый запрос
                    return sum;
                }
            }
            """;
}
