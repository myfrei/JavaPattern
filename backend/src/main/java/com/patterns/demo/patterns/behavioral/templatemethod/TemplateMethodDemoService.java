package com.patterns.demo.patterns.behavioral.templatemethod;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Готовит напиток по шагам: через общий скелет с хуками (good) и через
 * полностью скопированный алгоритм, где Coffee потерял шаг (bad). Отдаёт отчёт
 * для визуализации «пайплайн шагов: фиксированные vs хуки».
 */
@Service
public class TemplateMethodDemoService {

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Template Method — общий скелет, переопределяемые хуки");
        r.setDescription(
            "Beverage.prepare() задаёт неизменную последовательность, подклассы переопределяют только " +
            "brew() и addCondiments(). Tea и Coffee делят шаги boil water / pour, различаясь лишь хуками.");
        r.setCode(GOOD_CODE);

        GoodTemplate.Beverage tea = new GoodTemplate.Tea();
        List<String> steps = tea.prepare();
        // отметим, какие шаги — хуки (нечётные индексы 1 и 3)
        String[] kind = {"fixed", "hook", "fixed", "hook"};
        long t = 0;
        for (int i = 0; i < steps.size(); i++) {
            r.getInstances().add(new PatternDemoResponse.InstanceInfo(steps.get(i), kind[i]));
            r.getSteps().add(new PatternDemoResponse.Step(
                t++, steps.get(i), kind[i].equals("hook") ? "hook (подкласс)" : "fixed (скелет)", "шаг " + (i + 1), true));
        }

        boolean ok = steps.contains("boil water");
        r.setVerdict(ok ? "PASS — скелет общий, варьируются только хуки" : "FAIL");
        r.setExplanation(
            "Шаблонный метод фиксирует порядок шагов один раз. Coffee переопределяет brew()=\"brew coffee\" и " +
            "addCondiments()=\"add sugar\", но boil water/pour не может потерять — они в финальном методе базового класса.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Template Method — антипаттерн: скопированный алгоритм");
        r.setDescription(
            "Каждый напиток переписывает весь алгоритм. Coffee при копировании потерял шаг boil water — " +
            "компилятор это не заметит, общий инвариант последовательности нигде не закреплён.");
        r.setCode(BAD_CODE);

        List<String> steps = new BadTemplate.Coffee().prepare();
        long t = 0;
        for (String step : steps) {
            r.getInstances().add(new PatternDemoResponse.InstanceInfo(step, "copy-paste"));
            r.getSteps().add(new PatternDemoResponse.Step(t++, step, "дублированный шаг", "ok", false));
        }
        boolean missingBoil = !steps.contains("boil water");
        r.getSteps().add(new PatternDemoResponse.Step(t, "boil water", "пропущен", missingBoil ? "шаг потерян при копипасте ✗" : "—", false));

        r.setVerdict("FAIL — Coffee потерял шаг boil water при копировании");
        r.setExplanation(
            "Дублирование алгоритма ведёт к расхождению: один из вариантов теряет шаг. Template Method " +
            "держит скелет в одном месте, оставляя подклассам лишь точки расширения.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Template Method");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            abstract class Beverage {
                public final List<String> prepare() {     // скелет фиксирован
                    return List.of("boil water", brew(), "pour into cup", addCondiments());
                }
                protected abstract String brew();         // хук
                protected abstract String addCondiments();// хук
            }

            class Tea extends Beverage {
                protected String brew()          { return "steep tea"; }
                protected String addCondiments() { return "add lemon"; }
            }
            """;

    private static final String BAD_CODE = """
            // Каждый напиток переписывает весь алгоритм
            class Tea    { List<String> prepare(){ return List.of("boil water","steep tea","pour","add lemon"); } }
            class Coffee { List<String> prepare(){ return List.of("brew coffee","pour","add sugar"); } }
            //                                                     ^ забыли boil water
            """;
}
