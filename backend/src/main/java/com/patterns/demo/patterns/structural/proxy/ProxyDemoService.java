package com.patterns.demo.patterns.structural.proxy;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

/**
 * Делает три запроса (1, 1, 2): через кэширующий прокси (good) и напрямую к
 * дорогому сервису (bad). Отдаёт отчёт для визуализации «клиент → прокси →
 * реальный сервис» с попаданиями/промахами кэша.
 */
@Service
public class ProxyDemoService {

    private static final int[] REQUESTS = {1, 1, 2};

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Proxy — кэширующий заместитель");
        r.setDescription(
            "Клиент зовёт прокси тем же интерфейсом, что и реальный сервис. Прокси отдаёт повтор из " +
            "кэша, поэтому 3 запроса (1, 1, 2) превращаются всего в 2 дорогих обращения.");
        r.setCode(GOOD_CODE);

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Client", "вызывает"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("CachingProxy", "proxy + cache"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("RealService", "дорогой"));

        GoodProxy.RealService real = new GoodProxy.RealService();
        GoodProxy.CachingProxy proxy = new GoodProxy.CachingProxy(real);

        long t = 0;
        for (int id : REQUESTS) {
            boolean cached = proxy.isCached(id);
            String value = proxy.fetch(id);
            if (cached) {
                r.getSteps().add(new PatternDemoResponse.Step(t++, "CachingProxy", "fetch(" + id + ")", "HIT → " + value + " (из кэша)", true));
            } else {
                r.getSteps().add(new PatternDemoResponse.Step(t++, "CachingProxy", "fetch(" + id + ")", "MISS → RealService", true));
                r.getSteps().add(new PatternDemoResponse.Step(t++, "RealService", "load(" + id + ")", value, true));
            }
        }

        r.setVerdict("PASS — 3 запроса, реальных вызовов: " + real.calls());
        r.setExplanation(
            "Прокси прозрачно подменяет реальный сервис и срезает повторную работу кэшем. Тем же приёмом " +
            "делают ленивую инициализацию, контроль доступа и удалённые вызовы — клиент ничего не меняет.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Proxy — антипаттерн: прямые дорогие вызовы");
        r.setDescription(
            "Без прокси клиент дёргает дорогой сервис напрямую. Кэша нет, поэтому повторный запрос за " +
            "теми же данными снова оплачивает полную стоимость: 3 запроса = 3 дорогих вызова.");
        r.setCode(BAD_CODE);

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("Client", "вызывает"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("RealService", "дорогой"));

        BadProxy.DirectService direct = new BadProxy.DirectService();

        long t = 0;
        for (int id : REQUESTS) {
            String value = direct.fetch(id);
            r.getSteps().add(new PatternDemoResponse.Step(t++, "RealService", "fetch(" + id + ")", value + " · дорого", false));
        }

        r.setVerdict("FAIL — 3 запроса = " + direct.calls() + " дорогих вызова");
        r.setExplanation(
            "Каждый запрос идёт прямо в дорогой сервис, повтор не экономится. Proxy добавил бы кэш/ленивость " +
            "за тем же интерфейсом, не меняя клиента.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Proxy");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            interface Service { String fetch(int id); }

            class CachingProxy implements Service {       // тот же интерфейс
                private final RealService real;
                private final Map<Integer, String> cache = new HashMap<>();
                public String fetch(int id) {
                    return cache.computeIfAbsent(id, real::fetch); // дорого только при промахе
                }
            }

            Service s = new CachingProxy(new RealService());
            s.fetch(1); s.fetch(1); s.fetch(2);          // 3 запроса → 2 реальных вызова
            """;

    private static final String BAD_CODE = """
            // Клиент дёргает дорогой сервис напрямую, без кэша
            RealService real = new RealService();
            real.fetch(1);   // дорого
            real.fetch(1);   // снова дорого — тот же id!
            real.fetch(2);   // дорого
            // 3 запроса = 3 дорогих вызова
            """;
}
