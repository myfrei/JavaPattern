package com.patterns.demo.patterns.microservices.decomposition.stranglerfig;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

/**
 * Переводит 4 маршрута со старой системы на новую: инкрементально через роутер
 * (good) и big-bang переписыванием (bad). Отдаёт отчёт для визуализации
 * «постепенная миграция маршрутов».
 */
@Service
public class StranglerFigDemoService {

    private static final String[] ROUTES = {"/login", "/cart", "/pay", "/profile"};

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Strangler Fig — постепенная миграция за фасадом");
        r.setDescription(
            "Роутер переводит маршруты со старой системы на новую по одному. Немигрированные идут в legacy, " +
            "мигрированные — в new. Система всё время живая, риск разбит на маленькие шаги.");
        r.setCode(GOOD_CODE);

        for (String route : ROUTES) {
            r.getInstances().add(new PatternDemoResponse.InstanceInfo(route, "route"));
        }

        GoodStrangler.Router router = new GoodStrangler.Router();
        long t = 0;
        for (String route : ROUTES) {
            router.migrate(route);
            r.getSteps().add(new PatternDemoResponse.Step(
                t++, route, "migrate(" + route + ")",
                "→ " + router.handle(route) + " (" + router.migratedCount() + "/" + ROUTES.length + ")", true));
        }

        r.setVerdict("PASS — все " + ROUTES.length + " маршрута мигрированы инкрементально, legacy отключён");
        r.setExplanation(
            "Фасад-роутер позволяет переключать маршруты по одному и откатывать точечно. Никакой долгой " +
            "заморозки и big-bang-выкатки: новый сервис «душит» старый постепенно.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Strangler Fig — антипаттерн: big-bang переписывание");
        r.setDescription(
            "Старую систему замораживают, переписывают целиком и переключают всё разом. Месяцы без новых " +
            "фич и огромный риск на выкатке: либо всё взлетело, либо всё упало.");
        r.setCode(BAD_CODE);

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Legacy", "off"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("New", "all at once"));

        long t = 0;
        r.getSteps().add(new PatternDemoResponse.Step(t++, "Legacy", "freeze", "заморозка фич", false));
        r.getSteps().add(new PatternDemoResponse.Step(t++, "New", "rewrite all", "переписываем целиком", false));
        r.getSteps().add(new PatternDemoResponse.Step(t, "New", "switch", BadStrangler.bigBang(ROUTES.length), false));

        r.setVerdict("FAIL — big-bang: всё разом, высокий риск и простой");
        r.setExplanation(
            "Переключение всего сразу — это длинная заморозка и риск катастрофы на выкатке. Strangler Fig " +
            "заменяет это безопасной пошаговой миграцией за фасадом.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Strangler Fig");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            class Router {                                 // фасад перед обеими системами
                private final Set<String> migrated = new HashSet<>();
                void migrate(String route) { migrated.add(route); }
                String handle(String route) {
                    return migrated.contains(route) ? "new" : "legacy";
                }
            }

            router.migrate("/login");   // переводим по одному маршруту
            router.handle("/login");    // → new
            router.handle("/cart");     // → legacy (ещё не мигрирован)
            """;

    private static final String BAD_CODE = """
            // Big-bang: заморозка, переписывание целиком, переключение разом
            freezeLegacy();
            rewriteEverything();        // месяцы без новых фич
            switchAllAtOnce();          // либо всё взлетело, либо всё упало
            """;
}
