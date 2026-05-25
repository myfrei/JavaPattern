package com.patterns.demo.patterns.behavioral.mediator;

import com.patterns.demo.dto.PatternDemoResponse;
import org.springframework.stereotype.Service;

/**
 * Alice пишет в чат из 3 участников: через посредника-комнату (good) и через
 * прямые связи каждого с каждым (bad). Отдаёт отчёт для визуализации «звезда
 * против паутины».
 */
@Service
public class MediatorDemoService {

    private static final String[] USERS = {"Alice", "Bob", "Carol"};

    public PatternDemoResponse runGood() {
        PatternDemoResponse r = base("good");
        r.setTitle("Mediator — общение через посредника (звезда)");
        r.setDescription(
            "Участники знают только ChatRoom. Alice отправляет сообщение в комнату, а та доставляет его " +
            "остальным. Связей всего " + USERS.length + " (каждый ↔ комната), а не N×N.");
        r.setCode(GOOD_CODE);

        GoodMediator.ChatRoom room = new GoodMediator.ChatRoom();
        for (String u : USERS) {
            room.register(u);
            r.getInstances().add(new PatternDemoResponse.InstanceInfo(u, "participant"));
        }

        long t = 0;
        r.getSteps().add(new PatternDemoResponse.Step(t++, "Alice", "send → ChatRoom", "посредник примет и разошлёт", true));
        for (String to : room.send("Alice", "hi")) {
            r.getSteps().add(new PatternDemoResponse.Step(t++, to, "ChatRoom → " + to, "доставлено: hi", true));
        }

        r.setVerdict("PASS — " + room.connections() + " связи через посредника (mesh было бы " + BadMediator.meshConnections(USERS.length) + ")");
        r.setExplanation(
            "Посредник централизует коммуникацию: участники развязаны между собой и знают только комнату. " +
            "Добавить участника = зарегистрировать его, не трогая остальных.");
        return r;
    }

    public PatternDemoResponse runBad() {
        PatternDemoResponse r = base("bad");
        r.setTitle("Mediator — антипаттерн: каждый знает каждого (паутина)");
        r.setDescription(
            "Без посредника каждый участник держит ссылки на всех остальных и шлёт им напрямую. " +
            "Для " + USERS.length + " участников это " + BadMediator.meshConnections(USERS.length) + " связей; добавить одного — соединить со всеми.");
        r.setCode(BAD_CODE);

        BadMediator.User alice = new BadMediator.User("Alice");
        BadMediator.User bob = new BadMediator.User("Bob");
        BadMediator.User carol = new BadMediator.User("Carol");
        alice.connect(bob); alice.connect(carol);
        bob.connect(alice); bob.connect(carol);
        carol.connect(alice); carol.connect(bob);

        for (String u : USERS) {
            r.getInstances().add(new PatternDemoResponse.InstanceInfo(u, "knows all"));
        }

        long t = 0;
        for (String to : alice.send("hi")) {
            r.getSteps().add(new PatternDemoResponse.Step(t++, to, "Alice → " + to, "прямая связь", false));
        }
        r.getSteps().add(new PatternDemoResponse.Step(t, "mesh", "итого связей", BadMediator.meshConnections(USERS.length) + " (N×(N−1))", false));

        r.setVerdict("FAIL — " + BadMediator.meshConnections(USERS.length) + " связей: каждый знает каждого");
        r.setExplanation(
            "Прямые связи между всеми образуют паутину, которая растёт квадратично и тяжело меняется. " +
            "Mediator стягивает коммуникацию в одну точку.");
        return r;
    }

    private PatternDemoResponse base(String variant) {
        PatternDemoResponse r = new PatternDemoResponse();
        r.setPattern("Mediator");
        r.setVariant(variant);
        return r;
    }

    // ---------- демо-исходник для фронта ----------

    private static final String GOOD_CODE = """
            class ChatRoom {                          // посредник
                private final List<String> users = new ArrayList<>();
                void register(String u) { users.add(u); }
                List<String> send(String from, String msg) {
                    return users.stream().filter(u -> !u.equals(from)).toList();
                }
            }

            room.send("Alice", "hi");                 // комната разошлёт остальным
            // каждый знает только комнату → N связей
            """;

    private static final String BAD_CODE = """
            // Каждый участник знает всех остальных
            class User {
                private final List<User> peers = new ArrayList<>();
                void connect(User u) { peers.add(u); }
                void send(String msg) { for (User p : peers) p.receive(msg); }
            }
            // N участников → N×(N−1) связей (паутина)
            """;
}
