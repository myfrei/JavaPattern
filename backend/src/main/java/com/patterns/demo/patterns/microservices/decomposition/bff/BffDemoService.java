package com.patterns.demo.patterns.microservices.decomposition.bff;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Отдаёт карточку товара мобильному и веб-клиенту: через свой BFF на тип клиента
 * (good) и через один общий API с overfetch (bad). Отдаёт отчёт для визуализации
 * «BFF под клиента».
 */
@Service
public class BffDemoService {

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Backend for Frontend — свой шлюз под каждый клиент");
        r.setDescription(
            "Мобильный BFF возвращает 2 поля (name, price), веб-BFF — полный набор из 5. Каждый клиент " +
            "получает ровно нужный payload, без overfetch и без общего компромиссного контракта.");
        r.setCode(GOOD_CODE);

        Map<String, Object> mobile = new GoodBff.MobileBff().product();
        Map<String, Object> web = new GoodBff.WebBff().product();

        r.getInstances().add(new PatternDemoResponse.InstanceInfo("MobileBFF", mobile.size() + " поля"));
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("WebBFF", web.size() + " полей"));

        long t = 0;
        r.getSteps().add(new PatternDemoResponse.Step(t++, "MobileBFF", "mobile → product()", mobile.keySet().toString(), true));
        r.getSteps().add(new PatternDemoResponse.Step(t, "WebBFF", "web → product()", web.keySet().toString(), true));

        r.setVerdict("PASS — mobile получает " + mobile.size() + " поля, web — " + web.size());
        r.setExplanation(
            "Каждый BFF заточен под свой клиент, поэтому payload минимален и контракт не размывается. " +
            "Меняется мобильный экран — меняется только мобильный BFF.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Backend for Frontend — антипаттерн: один общий API");
        r.setDescription(
            "Общий API отдаёт всем полный объект. Мобильный клиент скачивает 5 полей, использует 2 — " +
            "overfetch по трафику, а контракт становится компромиссом между web и mobile.");
        r.setCode(BAD_CODE);

        Map<String, Object> all = BadBff.generalProduct();
        r.getInstances().add(new PatternDemoResponse.InstanceInfo("GeneralAPI", all.size() + " полей всем"));

        long t = 0;
        r.getSteps().add(new PatternDemoResponse.Step(t++, "GeneralAPI", "mobile → product()", all.size() + " полей (использует 2)", false));
        r.getSteps().add(new PatternDemoResponse.Step(t, "GeneralAPI", "web → product()", all.size() + " полей", false));

        r.setVerdict("FAIL — overfetch: мобильный качает " + all.size() + " полей, нужно 2");
        r.setExplanation(
            "Единый контракт под всех клиентов ведёт к overfetch и компромиссам. BFF решает это отдельным " +
            "шлюзом на каждый тип клиента.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Backend for Frontend");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            class MobileBff {                       // шлюз под мобильный клиент
                Map<String,Object> product() {
                    Product p = backend();
                    return Map.of("name", p.name(), "price", p.price()); // только нужное
                }
            }
            class WebBff {                          // шлюз под веб
                Map<String,Object> product() {
                    Product p = backend();
                    return Map.of("name", ..., "price", ..., "description", ...,
                                  "reviews", ..., "specs", ...);          // полный набор
                }
            }
            """;

    private static final String BAD_CODE = """
            // Один общий API отдаёт всем полный объект
            Map<String,Object> generalProduct() {
                return Map.of("name", ..., "price", ..., "description", ...,
                              "reviews", ..., "specs", ...);   // всем 5 полей
            }
            // мобильный клиент качает 5, использует 2 → overfetch
            """;
}
