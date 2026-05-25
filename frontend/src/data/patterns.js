// Static pattern catalog for the JavaPattern UI.
// Browsing data (sections, categories, pattern lists) is static; the Sandbox
// wires Run to the live backend where an endpoint exists (Singleton today).

export const slug = (name) =>
  name.toLowerCase().replace(/[^a-z0-9]+/g, '-').replace(/(^-|-$)/g, '')

export const SECTIONS = [
  {
    id: 'design',
    title: 'Design Patterns',
    sub: 'Паттерны проектирования',
    tag: 'GoF · 23 patterns',
    desc: 'Creational · Structural · Behavioral',
    icon: 'blueprint',
    accent: 'var(--accent)',
    preview: ['Singleton', 'Factory', 'Observer', 'Strategy', '+19'],
    to: '/patterns/design',
  },
  {
    id: 'microservices',
    title: 'Microservices',
    sub: 'Микросервисные паттерны',
    tag: 'Distributed · 14',
    desc: 'Decomp · Comm · Data · Resilience',
    icon: 'micro',
    accent: 'var(--accent-2)',
    preview: ['API Gateway', 'Circuit Breaker', 'Saga', 'CQRS', '+10'],
    to: '/patterns/microservices',
  },
]

export const CATEGORIES = [
  // ── Design Patterns (GoF) ──
  { id: 'creational', section: 'design', kind: 'creat', label: 'Creational', ru: 'Порождающие', count: 5, icon: 'spark',
    desc: 'Управляют созданием объектов — изоляция конструкторов, контроль семейств.' },
  { id: 'structural', section: 'design', kind: 'struct', label: 'Structural', ru: 'Структурные', count: 7, icon: 'column',
    desc: 'Композиция классов и объектов — адаптеры, оболочки, общие интерфейсы.' },
  { id: 'behavioral', section: 'design', kind: 'behav', label: 'Behavioral', ru: 'Поведенческие', count: 11, icon: 'arrows',
    desc: 'Алгоритмы и распределение ответственности между объектами.' },
  // ── Microservices ──
  { id: 'decomposition', section: 'microservices', kind: 'decomp', label: 'Decomposition', ru: 'Декомпозиция', count: 3, icon: 'blueprint',
    desc: 'Разбиение и маршрутизация: единая точка входа, клиентские шлюзы, миграция.' },
  { id: 'communication', section: 'microservices', kind: 'comm', label: 'Communication', ru: 'Коммуникация', count: 3, icon: 'arrows',
    desc: 'Взаимодействие сервисов: обнаружение, сайдкары, событийные шины.' },
  { id: 'data', section: 'microservices', kind: 'data', label: 'Data', ru: 'Данные', count: 4, icon: 'column',
    desc: 'Управление данными в распределённой среде: транзакции, чтение/запись, события.' },
  { id: 'resilience', section: 'microservices', kind: 'resil', label: 'Resilience', ru: 'Надёжность', count: 4, icon: 'micro',
    desc: 'Устойчивость к сбоям: предохранители, повторы, изоляция, ограничение нагрузки.' },
]

// number is assigned sequentially per category below.
const RAW = {
  creational: [
    { name: 'Singleton',        ru: 'Одиночка',            diff: 'easy',   pop: 5, tags: ['global', 'lazy-init'], status: 'done', live: 'singleton' },
    { name: 'Factory Method',   ru: 'Фабричный метод',     diff: 'medium', pop: 5, tags: ['create', 'subclass'], status: 'done' },
    { name: 'Abstract Factory', ru: 'Абстрактная фабрика', diff: 'hard',   pop: 4, tags: ['family'], status: 'done' },
    { name: 'Builder',          ru: 'Строитель',           diff: 'easy',   pop: 4, tags: ['step-by-step'], status: 'done' },
    { name: 'Prototype',        ru: 'Прототип',            diff: 'medium', pop: 2, tags: ['clone'], status: 'done' },
  ],
  structural: [
    { name: 'Adapter',   ru: 'Адаптер',         diff: 'easy',   pop: 5, tags: ['compat'], status: 'done' },
    { name: 'Bridge',    ru: 'Мост',            diff: 'hard',   pop: 3, tags: ['abstraction'], status: 'done' },
    { name: 'Composite', ru: 'Компоновщик',     diff: 'medium', pop: 4, tags: ['tree'], status: 'done' },
    { name: 'Decorator', ru: 'Декоратор',       diff: 'medium', pop: 5, tags: ['wrap'], status: 'done' },
    { name: 'Facade',    ru: 'Фасад',           diff: 'easy',   pop: 5, tags: ['simplify'], status: 'done' },
    { name: 'Flyweight', ru: 'Приспособленец',  diff: 'hard',   pop: 2, tags: ['memory'], status: 'done' },
    { name: 'Proxy',     ru: 'Заместитель',     diff: 'medium', pop: 4, tags: ['access'], status: 'done' },
  ],
  behavioral: [
    { name: 'Chain of Resp.',  ru: 'Цепочка обяз.',    diff: 'medium', pop: 3, tags: ['pipeline'], status: 'done' },
    { name: 'Command',         ru: 'Команда',          diff: 'medium', pop: 4, tags: ['undo', 'queue'], status: 'done' },
    { name: 'Iterator',        ru: 'Итератор',         diff: 'easy',   pop: 5, tags: ['traversal'], status: 'done' },
    { name: 'Mediator',        ru: 'Посредник',        diff: 'hard',   pop: 3, tags: ['coupling'], status: 'done' },
    { name: 'Memento',         ru: 'Снимок',           diff: 'medium', pop: 2, tags: ['state', 'undo'], status: 'done' },
    { name: 'Observer',        ru: 'Наблюдатель',      diff: 'easy',   pop: 5, tags: ['events', 'pub-sub'], status: 'done' },
    { name: 'State',           ru: 'Состояние',        diff: 'medium', pop: 4, tags: ['fsm'], status: 'done' },
    { name: 'Strategy',        ru: 'Стратегия',        diff: 'easy',   pop: 5, tags: ['algorithm', 'swap'], status: 'done' },
    { name: 'Template Method', ru: 'Шаблонный метод',  diff: 'easy',   pop: 4, tags: ['skeleton'], status: 'done' },
    { name: 'Visitor',         ru: 'Посетитель',       diff: 'hard',   pop: 2, tags: ['double-disp'], status: 'done' },
    { name: 'Interpreter',     ru: 'Интерпретатор',    diff: 'hard',   pop: 1, tags: ['grammar'], status: 'done' },
  ],
  decomposition: [
    { name: 'API Gateway',          ru: 'API-шлюз',            diff: 'medium', pop: 5, tags: ['routing', 'aggregate'], status: 'done' },
    { name: 'Backend for Frontend', ru: 'BFF',                 diff: 'medium', pop: 4, tags: ['client-specific'], status: 'done' },
    { name: 'Strangler Fig',        ru: 'Душитель',            diff: 'medium', pop: 3, tags: ['migration'], status: 'done' },
  ],
  communication: [
    { name: 'Service Discovery',    ru: 'Обнаружение сервисов', diff: 'medium', pop: 5, tags: ['registry'], status: 'done' },
    { name: 'Sidecar',              ru: 'Прицеп',              diff: 'medium', pop: 4, tags: ['cross-cutting'], status: 'done' },
    { name: 'Publish-Subscribe',    ru: 'Издатель-подписчик',  diff: 'easy',   pop: 5, tags: ['async', 'events'], status: 'done' },
  ],
  data: [
    { name: 'Saga',                 ru: 'Сага',                diff: 'hard',   pop: 5, tags: ['transactions', 'compensation'], status: 'done' },
    { name: 'CQRS',                 ru: 'CQRS',                diff: 'hard',   pop: 4, tags: ['read-write'], status: 'done' },
    { name: 'Event Sourcing',       ru: 'Источник событий',    diff: 'hard',   pop: 4, tags: ['event-log'], status: 'done' },
    { name: 'Transactional Outbox', ru: 'Транзакц. outbox',    diff: 'medium', pop: 3, tags: ['atomicity'], status: 'done' },
  ],
  resilience: [
    { name: 'Circuit Breaker',      ru: 'Предохранитель',      diff: 'medium', pop: 5, tags: ['fail-fast'], status: 'done' },
    { name: 'Retry',                ru: 'Повтор',              diff: 'easy',   pop: 5, tags: ['backoff'], status: 'done' },
    { name: 'Bulkhead',             ru: 'Переборка',           diff: 'medium', pop: 3, tags: ['isolation'], status: 'done' },
    { name: 'Rate Limiter',         ru: 'Ограничитель',        diff: 'medium', pop: 4, tags: ['throttle'], status: 'done' },
  ],
}

export const PATTERNS = Object.fromEntries(
  Object.entries(RAW).map(([cat, list]) => [
    cat,
    list.map((p, i) => ({ ...p, id: slug(p.name), category: cat, number: i + 1 })),
  ])
)

export const getCategory = (id) => CATEGORIES.find(c => c.id === id)
export const getSectionCategories = (section) => CATEGORIES.filter(c => c.section === section)
export const getSection = (id) => SECTIONS.find(s => s.id === id)
export const getCategoryPatterns = (id) => PATTERNS[id] || []
export const findPattern = (cat, patternId) => (PATTERNS[cat] || []).find(p => p.id === patternId)
export const doneCount = (cat) => (PATTERNS[cat] || []).filter(p => p.status === 'done').length

export function findPatternAnywhere(patternId) {
  for (const cat of Object.keys(PATTERNS)) {
    const hit = findPattern(cat, patternId)
    if (hit) return hit
  }
  return null
}

// ─── Rich detail content ───
const OBSERVER_GOOD = `// Subject
public interface Subject {
  void attach(Observer o);
  void detach(Observer o);
  void notifyObservers();
}

public class NewsFeed implements Subject {
  private final List<Observer> subs = new ArrayList<>();
  private String latest;

  public void publish(String news) {
    this.latest = news;
    notifyObservers();
  }
  public void attach(Observer o) { subs.add(o); }
  public void notifyObservers() {
    for (Observer o : subs) o.update(latest);
  }
}`

const OBSERVER_BAD = `// NewsFeed создаёт и дёргает ВСЕ сервисы сам — жёсткая связь
public class NewsFeed {
  public void publish(String n) {
    new EmailSvc().send(n);
    new PushSvc().send(n);
    new SmsSvc().send(n);
  }
}`

const SINGLETON_GOOD = `public final class Config {
  private static volatile Config instance;
  private Config() {}

  public static Config getInstance() {
    if (instance == null) {
      synchronized (Config.class) {
        if (instance == null) instance = new Config();
      }
    }
    return instance;
  }
}`

const SINGLETON_BAD = `public class Config {
  private static Config instance;
  private Config() {}

  // нет синхронизации — гонка потоков
  public static Config getInstance() {
    if (instance == null) {
      instance = new Config();
    }
    return instance;
  }
}`

// Sandbox-runner source for the Observer demo (line numbers drive the playback).
const OBSERVER_RUNNER = `// loaded from backend
public class NewsFeed implements Subject {
  private final List<Observer> subs
    = new ArrayList<>();

  public void attach(Observer o) {
    subs.add(o);
  }
  public void publish(String news) {
    for (Observer o : subs)
      o.update(news);
  }
}

// runner
NewsFeed f = new NewsFeed();
f.attach(new EmailNotifier());
f.attach(new PushNotifier());
f.publish("breaking news");`

const OBSERVER_FRAMES = [
  { codeLine: 16, viz: 0, label: 'newsFeed.attach(sub1)' },
  { codeLine: 17, viz: 1, label: 'newsFeed.attach(sub2)' },
  { codeLine: 18, viz: 2, label: 'newsFeed.publish("breaking news")' },
  { codeLine: 9,  viz: 3, label: '→ subs.forEach(notify)' },
  { codeLine: 10, viz: 4, label: 'sub1.update(...)' },
  { codeLine: 10, viz: 5, label: 'sub2.update(...)' },
]

const OBSERVER_STDOUT = [
  { line: 'POST /api/v1/run', c: 'var(--ink-3)' },
  { line: '  body: { pattern: "observer", flavor: "good" }', c: 'var(--ink-3)' },
  { line: '→ 200 OK · 42ms', c: 'var(--good)' },
  { line: '', c: '' },
  { line: '[trace]', c: 'var(--ink-3)' },
  { line: 'newsFeed.attach(EmailNotifier@a1)', c: '' },
  { line: 'newsFeed.attach(PushNotifier@b2)', c: '' },
  { line: 'newsFeed.publish("breaking news")', c: '' },
  { line: '  ▷ notifying 2 observers', c: 'var(--accent-3)' },
  { line: '  [Email] got: breaking news', c: 'var(--good)' },
  { line: '  [Push]  got: breaking news', c: 'var(--good)' },
  { line: '', c: '' },
  { line: 'Process exited: 0', c: 'var(--good)' },
]

const CHAIN_GOOD = `abstract class Approver {
  protected Approver next;
  Approver linkTo(Approver next) { this.next = next; return next; }

  void handle(Request req) {
    if (canApprove(req)) {
      approve(req);
    } else if (next != null) {
      next.handle(req);            // передаём дальше по цепочке
    } else {
      System.out.println("Никто не одобрил: " + req);
    }
  }
  abstract boolean canApprove(Request req);
  abstract void approve(Request req);
}

class Manager extends Approver {
  boolean canApprove(Request r) { return r.amount() <= 5_000; }
  void approve(Request r) { System.out.println("Manager одобрил " + r); }
}
// TeamLead, Director, CEO — аналогично, каждый со своим лимитом

Approver chain = new TeamLead();
chain.linkTo(new Manager())
     .linkTo(new Director())
     .linkTo(new CEO());
chain.handle(new Request(15_000));   // → одобрит Director`

const CHAIN_BAD = `// Один класс знает ВСЕ правила и порядок — не расширяется
class ApprovalService {
  void approve(Request r) {
    if (r.amount() <= 1_000) {
      System.out.println("TeamLead одобрил " + r);
    } else if (r.amount() <= 5_000) {
      System.out.println("Manager одобрил " + r);
    } else if (r.amount() <= 20_000) {
      System.out.println("Director одобрил " + r);
    } else {
      System.out.println("CEO одобрил " + r);
    }
    // новый уровень? правим этот метод и рискуем сломать ветки
  }
}`

const FACTORY_GOOD = `interface Notifier { String send(String msg); }

abstract class NotifierFactory {
  abstract Notifier create();            // фабричный метод
  String notify(String msg) {            // логика на абстракции
    return create().send(msg);
  }
}

class EmailFactory extends NotifierFactory {
  Notifier create() { return new EmailNotifier(); }
}
// SmsFactory, PushFactory — аналогично

for (NotifierFactory f : List.of(new EmailFactory(),
                                 new SmsFactory(),
                                 new PushFactory())) {
  f.notify("Заказ #42 оплачен");   // клиент не знает классов продуктов
}`

const FACTORY_BAD = `// Один метод знает про все каналы и создаёт объекты сам
class NotificationService {
  String send(String channel, String msg) {
    switch (channel) {
      case "EMAIL": return new EmailNotifier().send(msg);
      case "SMS":   return new SmsNotifier().send(msg);
      case "PUSH":  return new PushNotifier().send(msg);
      default: throw new IllegalArgumentException(channel);
    }
    // новый канал? правим switch и рискуем сломать ветки
  }
}`

const ABSTRACT_GOOD = `interface Button { String render(); }
interface Checkbox { String render(); }

interface GUIFactory {                 // абстрактная фабрика семейства
  Button createButton();
  Checkbox createCheckbox();
}

class WinFactory implements GUIFactory {
  public Button createButton()    { return new WinButton(); }
  public Checkbox createCheckbox(){ return new WinCheckbox(); }
}
// MacFactory — аналогично, всё семейство macOS

GUIFactory f = osIsMac ? new MacFactory() : new WinFactory();
Button b = f.createButton();           // оба виджета — гарантированно
Checkbox c = f.createCheckbox();       // из одного семейства`

const ABSTRACT_BAD = `// Клиент сам выбирает классы под каждый виджет
Button b   = new WinButton();          // Windows
Checkbox c = new MacCheckbox();        // macOS — другое семейство!

// компилируется, но UI рассинхронизирован:
// ничто не гарантирует, что виджеты из одной платформы`

const BUILDER_GOOD = `Pizza pizza = Pizza.builder()
  .size("L")
  .crust("тонкое")
  .cheese(true)
  .topping("ветчина")
  .topping("грибы")
  .build();              // валидирует size, отдаёт иммутабельный объект

// в Builder:
public Pizza build() {
  if (size == null) throw new IllegalStateException("size обязателен");
  return new Pizza(this);
}`

const BUILDER_BAD = `// Телескопический конструктор: что есть что — видно только по сигнатуре
Pizza pizza = new Pizza("L", "тонкое", true, false, true);
//                                     ^cheese ^ham  ^mushroom
// хотели ham=true, mushroom=false — но переставили флаги местами.
// Компилируется, тихо отдаёт не ту пиццу.`

const PROTOTYPE_GOOD = `class Document {
  private String title;
  private final List<String> tags;

  Document copy() {                       // глубокая копия
    return new Document(title, new ArrayList<>(tags));
  }                                       // ^ новый список, не общая ссылка
}

Document clone = original.copy();
clone.addTag("v2");                       // оригинал не задет`

const PROTOTYPE_BAD = `class Document {
  private String title;
  private List<String> tags;

  Document copy() {                       // поверхностная копия
    return new Document(title, this.tags);
  }                                       // ^ ОБЩАЯ ссылка на список — баг
}

Document clone = original.copy();
clone.addTag("v2");                       // оригинал тоже получил "v2"!`

const ADAPTER_GOOD = `interface PaymentGateway { String pay(int cents); }   // наш интерфейс

class LegacyBank {                                     // чужой API, менять нельзя
  String makeTransfer(double dollars, String ccy) { ... }
}

class BankAdapter implements PaymentGateway {         // переходник
  private final LegacyBank bank;
  public String pay(int cents) {
    return bank.makeTransfer(cents / 100.0, "USD");   // конвертация в одном месте
  }
}

PaymentGateway gateway = new BankAdapter(new LegacyBank());
gateway.pay(2599);                                    // клиент не знает о LegacyBank`

const ADAPTER_BAD = `// Клиент сам зовёт чужой API и дублирует конвертацию повсюду
new LegacyBank().makeTransfer(2599 / 100.0, "USD");   // место A — ок
new LegacyBank().makeTransfer(2599,         "USD");   // место B — забыли /100!
//                            ^ списали $2599 вместо $25.99`

const BRIDGE_GOOD = `interface Renderer { String render(String shape); }   // ось реализации

abstract class Shape {                                 // ось абстракции
  protected final Renderer renderer;                  // ← мост
  abstract String draw();
}
class Circle extends Shape {
  String draw() { return renderer.render("circle"); }
}

new Circle(new VectorRenderer()).draw();              // любая пара
new Square(new RasterRenderer()).draw();              // без новых классов`

const BRIDGE_BAD = `// Класс на каждую КОМБИНАЦИЮ — рост как произведение
class VectorCircle { String draw() { return "vector[circle]"; } }
class RasterCircle { String draw() { return "raster(circle)"; } }
class VectorSquare { String draw() { return "vector[square]"; } }
class RasterSquare { String draw() { return "raster(square)"; } }
// добавили рендерер? +1 класс на КАЖДУЮ фигуру`

const COMPOSITE_GOOD = `interface Node { int size(); }                  // лист и контейнер — одно

class Directory implements Node {
  private final List<Node> children = ...;
  public int size() {
    int total = 0;
    for (Node c : children) total += c.size();    // рекурсия единообразна
    return total;
  }
}

root.size();   // 35 — без единого instanceof`

const COMPOSITE_BAD = `// Нет общего интерфейса — клиент сам обходит через instanceof
int sizeShallow(Folder f) {
  int total = 0;
  for (Object o : f.items) {
    if (o instanceof FileItem file) total += file.size;
    // забыли: else if (o instanceof Folder) спуститься рекурсивно
  }
  return total;   // 10 вместо 35 — вложенная sub/ потеряна
}`

const DECORATOR_GOOD = `interface Beverage { int cost(); String describe(); }

abstract class Decorator implements Beverage {
  protected final Beverage inner;              // вложенный объект
}
class Milk extends Decorator {
  public int cost() { return inner.cost() + 20; }   // добавляет к вложенному
}

Beverage drink = new Sugar(new Milk(new Espresso()));
drink.cost();      // 110, собрано обёртками в рантайме`

const DECORATOR_BAD = `// Класс на каждую комбинацию добавок — рост как 2^n
class Espresso { ... }
class EspressoWithMilk { ... }
class EspressoWithSugar { ... }
class EspressoWithMilkAndSugar { ... }
// добавили сироп? удваиваем число классов`

const FACADE_GOOD = `class OrderFacade {                       // единый вход
  private final Inventory inventory = new Inventory();
  private final Payment payment = new Payment();
  private final Shipping shipping = new Shipping();

  String placeOrder(String sku, int cents) {
    if (!inventory.reserve(sku)) return "out of stock";
    payment.charge(cents);
    return shipping.ship(sku);
  }
}

facade.placeOrder("SKU-42", 2599);        // клиент не знает подсистем`

const FACADE_BAD = `// Клиент сам создаёт подсистемы и помнит порядок вызовов
var inv = new Inventory();
var pay = new Payment();
var ship = new Shipping();

inv.reserve(sku);     // шаг 1
pay.charge(cents);    // шаг 2  — перепутаешь порядок, отгрузишь без оплаты
ship.ship(sku);       // шаг 3`

const FLYWEIGHT_GOOD = `class TreeType { final String name, color; }   // тяжёлое общее состояние

class TreeFactory {
  private final Map<String, TreeType> pool = new HashMap<>();
  TreeType get(String name, String color) {
    return pool.computeIfAbsent(name + "/" + color,
                                k -> new TreeType(name, color)); // общий объект
  }
}

record Tree(int x, int y, TreeType type) {}      // только координаты + ссылка
// 5 деревьев двух видов → 2 общих flyweight'а`

const FLYWEIGHT_BAD = `// Каждое дерево хранит СВОЮ копию тяжёлого состояния
class Tree {
  final int x, y;
  final HeavyType type;                        // texture-blob-256kb на КАЖДОЕ
  Tree(int x, int y, String name, String color) {
    this.type = new HeavyType(name, color);    // новая копия всегда
  }
}
// 1000 одинаковых дубов → 1000 копий одних и тех же данных`

const PROXY_GOOD = `interface Service { String fetch(int id); }

class CachingProxy implements Service {       // тот же интерфейс
  private final RealService real;
  private final Map<Integer, String> cache = new HashMap<>();
  public String fetch(int id) {
    return cache.computeIfAbsent(id, real::fetch); // дорого только при промахе
  }
}

Service s = new CachingProxy(new RealService());
s.fetch(1); s.fetch(1); s.fetch(2);          // 3 запроса → 2 реальных вызова`

const PROXY_BAD = `// Клиент дёргает дорогой сервис напрямую, без кэша
RealService real = new RealService();
real.fetch(1);   // дорого
real.fetch(1);   // снова дорого — тот же id!
real.fetch(2);   // дорого
// 3 запроса = 3 дорогих вызова`

const COMMAND_GOOD = `interface Command { void execute(); void undo(); }

class AppendCommand implements Command {
  public void execute() { doc.append(text); }
  public void undo()    { doc.deleteLast(text.length()); }
}

class Invoker {
  private final Deque<Command> history = new ArrayDeque<>();
  void run(Command c) { c.execute(); history.push(c); }
  boolean undo() { history.pop().undo(); return true; }
}

inv.run(new AppendCommand(doc, "Hello "));
inv.run(new AppendCommand(doc, "World"));
inv.undo();                       // → "Hello "`

const COMMAND_BAD = `// Прямые вызовы: действие нигде не сохраняется
doc.append("Hello ");
doc.append("World");
// undo? нечем — нет истории, очереди и лога`

const ITERATOR_GOOD = `class Repo implements Iterable<String> {
  private final String[] items;
  public Iterator<String> iterator() {
    return new Iterator<>() {
      int i = 0;
      public boolean hasNext() { return i < items.length; }
      public String next()     { return items[i++]; }
    };
  }
}

for (String s : repo) { ... }   // не знает, что внутри массив`

const ITERATOR_BAD = `// Коллекция обнажает массив, клиент ходит по индексам
class Repo { public final String[] data; public int size(){ return data.length; } }

for (int i = 0; i < repo.size(); i++) {
  use(repo.data[i]);          // завязан на массив; смена структуры всё ломает
}`

const MEDIATOR_GOOD = `class ChatRoom {                          // посредник
  private final List<String> users = new ArrayList<>();
  void register(String u) { users.add(u); }
  List<String> send(String from, String msg) {
    return users.stream().filter(u -> !u.equals(from)).toList();
  }
}

room.send("Alice", "hi");                 // комната разошлёт остальным
// каждый знает только комнату → N связей`

const MEDIATOR_BAD = `// Каждый участник знает всех остальных
class User {
  private final List<User> peers = new ArrayList<>();
  void connect(User u) { peers.add(u); }
  void send(String msg) { for (User p : peers) p.receive(msg); }
}
// N участников → N×(N−1) связей (паутина)`

const MEMENTO_GOOD = `class Editor {
  private String content = "";
  Memento save()          { return new Memento(content); } // копия значения
  void restore(Memento m) { this.content = m.state(); }
}

history.push(editor.save());   // S1 = "A"
editor.type("B");
Memento s2 = editor.save();    // S2 = "AB"
editor.type("C");
editor.restore(s2);            // → "AB"`

const MEMENTO_BAD = `// «Снимок» — это ссылка на изменяемое состояние
List<String> snapshot = editor.lines;   // не копия!
editor.type("B");                        // snapshot тоже стал ["A","B"]
restore(snapshot);                       // вернули "AB" вместо "A"`

const STATE_GOOD = `interface State { State onPublish(); }

class Draft implements State {
  public State onPublish() { return new Review(); }
}
class Review implements State {
  public State onPublish() { return new Published(); }
}

class Document {
  private State state = new Draft();
  void publish() { state = state.onPublish(); } // делегирует состоянию
}`

const STATE_BAD = `// Состояние — строка, переходы в одном switch
class Document {
  private String state = "Draft";
  void publish() {
    switch (state) {
      case "Draft":  state = "Review";    break;
      case "Review": state = "Published"; break;
      // новое состояние? правим switch (и все остальные)
    }
  }
}`

const STRATEGY_GOOD = `interface Discount { int apply(int total); }

class Percent implements Discount {
  public int apply(int total) { return total - total * pct / 100; }
}
class Coupon implements Discount {
  public int apply(int total) { return Math.max(0, total - amount); }
}

cart.setDiscount(new Percent(10));   // подмена в рантайме
cart.checkout(1000);                 // 900`

const STRATEGY_BAD = `// Выбор алгоритма зашит в switch по типу
int checkout(int total, String type, int param) {
  switch (type) {
    case "none":    return total;
    case "percent": return total - total * param / 100;
    case "coupon":  return Math.max(0, total - param);
    // новая скидка? ещё одна ветка здесь
  }
}`

const TEMPLATE_GOOD = `abstract class Beverage {
  public final List<String> prepare() {     // скелет фиксирован
    return List.of("boil water", brew(), "pour into cup", addCondiments());
  }
  protected abstract String brew();         // хук
  protected abstract String addCondiments();// хук
}

class Tea extends Beverage {
  protected String brew()          { return "steep tea"; }
  protected String addCondiments() { return "add lemon"; }
}`

const TEMPLATE_BAD = `// Каждый напиток переписывает весь алгоритм
class Tea    { List<String> prepare(){ return List.of("boil water","steep tea","pour","add lemon"); } }
class Coffee { List<String> prepare(){ return List.of("brew coffee","pour","add sugar"); } }
//                                                     ^ забыли boil water`

const VISITOR_GOOD = `interface Shape { <R> R accept(Visitor<R> v); }
interface Visitor<R> { R visitCircle(Circle c); R visitSquare(Square s); }

class Circle implements Shape {
  public <R> R accept(Visitor<R> v) { return v.visitCircle(this); } // двойная диспетчеризация
}
class AreaVisitor implements Visitor<Integer> {
  public Integer visitCircle(Circle c) { return (int) Math.round(PI * c.radius * c.radius); }
  public Integer visitSquare(Square s) { return s.side * s.side; }
}

shape.accept(new AreaVisitor());     // новая операция = новый Visitor`

const VISITOR_BAD = `// Операция через лестницу instanceof
int area(Object shape) {
  if (shape instanceof Circle c) return (int) Math.round(PI * c.radius * c.radius);
  else if (shape instanceof Square s) return s.side * s.side;
  else throw new IllegalArgumentException();
  // новая фигура? правим КАЖДУЮ такую операцию
}`

const INTERPRETER_GOOD = `interface Expr { int interpret(); }

record Num(int v) implements Expr { public int interpret() { return v; } }
record Add(Expr l, Expr r) implements Expr {
  public int interpret() { return l.interpret() + r.interpret(); }
}
record Sub(Expr l, Expr r) implements Expr {
  public int interpret() { return l.interpret() - r.interpret(); }
}

Expr ast = new Sub(new Add(new Num(5), new Num(3)), new Num(2));
ast.interpret();   // 6, рекурсия по дереву`

const INTERPRETER_BAD = `// Ad-hoc разбор: split и проход слева направо
int eval(String expr) {
  String[] tok = expr.split(" ");
  int acc = parseInt(tok[0]);
  for (int i = 1; i + 1 < tok.length; i += 2) {
    if (tok[i].equals("+")) acc += parseInt(tok[i+1]);
    else if (tok[i].equals("-")) acc -= parseInt(tok[i+1]);
  }
  return acc;   // нет приоритетов, нет скобок, "*" не осилит
}`

const APIGW_GOOD = `class Gateway {                                   // единая точка входа
  Dashboard getDashboard(String userId) {
    return new Dashboard(
      users.get(userId),                    // шлюз сам ходит
      orders.list(userId),                  // в нужные сервисы
      inventory.stock("SKU-1"));            // и агрегирует
  }
}

// клиент:
GET /dashboard?user=u1                       // 1 round-trip`

const APIGW_BAD = `// Болтливый клиент сам ходит в каждый сервис
GET user-service/users/u1                    // round-trip 1
GET order-service/orders?user=u1             // round-trip 2
GET inventory-service/stock/SKU-1            // round-trip 3
// клиент знает адреса всех сервисов и платит за каждый запрос`

const BFF_GOOD = `class MobileBff {                       // шлюз под мобильный клиент
  Map<String,Object> product() {
    Product p = backend();
    return Map.of("name", p.name(), "price", p.price()); // только нужное
  }
}
class WebBff {                          // шлюз под веб
  Map<String,Object> product() {
    return Map.of("name", ..., "price", ..., "description", ...,
                  "reviews", ..., "specs", ...);          // полный набор
  }
}`

const BFF_BAD = `// Один общий API отдаёт всем полный объект
Map<String,Object> generalProduct() {
  return Map.of("name", ..., "price", ..., "description", ...,
                "reviews", ..., "specs", ...);   // всем 5 полей
}
// мобильный клиент качает 5, использует 2 → overfetch`

const STRANGLER_GOOD = `class Router {                                 // фасад перед обеими системами
  private final Set<String> migrated = new HashSet<>();
  void migrate(String route) { migrated.add(route); }
  String handle(String route) {
    return migrated.contains(route) ? "new" : "legacy";
  }
}

router.migrate("/login");   // переводим по одному маршруту
router.handle("/login");    // → new
router.handle("/cart");     // → legacy (ещё не мигрирован)`

const STRANGLER_BAD = `// Big-bang: заморозка, переписывание целиком, переключение разом
freezeLegacy();
rewriteEverything();        // месяцы без новых фич
switchAllAtOnce();          // либо всё взлетело, либо всё упало`

const DISCOVERY_GOOD = `class Registry {
  void register(String service, String instance) { ... }
  String resolve(String service) {                 // round-robin живых
    List<String> in = pool.get(service);
    return in.get(rr++ % in.size());
  }
}

reg.register("order-service", "10.0.0.1:8080");
reg.register("order-service", "10.0.0.2:8080");
String addr = reg.resolve("order-service");          // живой адрес`

const DISCOVERY_BAD = `// Клиент хардкодит адрес сервиса
String addr = "10.0.0.1:8080";   // зашито в код
call(addr);                       // ок, пока инстанс там
// инстанс пересоздан → 10.0.0.7:8080
call(addr);                       // FAIL: no route to host`

const SIDECAR_GOOD = `class OrderApp implements App {
  public String handle(String req) { return "order:" + req; } // только бизнес
}

class Sidecar {                                  // оборачивает сквозным
  List<String> process(String req) {
    tls(); rateLimit();
    String out = app.handle(req);            // бизнес-логика чистая
    metrics();
    return out;
  }
}`

const SIDECAR_BAD = `// Инфра-код вперемешку с бизнесом в каждом сервисе
class OrderApp {
  String handle(String req) {
    tlsTerminate();        // инфра
    rateLimitCheck();      // инфра
    var out = "order:" + req; // бизнес
    metricsEmit();         // инфра
    return out;
  }
}`

const PUBSUB_GOOD = `class Broker {
  private final List<String> subscribers = new ArrayList<>();
  void subscribe(String s) { subscribers.add(s); }
  List<String> publish(String event) {
    return new ArrayList<>(subscribers);   // независимый fan-out
  }
}

broker.subscribe("EmailConsumer");
broker.subscribe("AnalyticsConsumer");
broker.publish("OrderPlaced");   // все получают независимо`

const PUBSUB_BAD = `// Издатель синхронно вызывает получателей по очереди
emailConsumer.handle(event);       // ok
analyticsConsumer.handle(event);   // FAIL → исключение
inventoryConsumer.handle(event);   // не вызван — цепочка оборвана`

const SAGA_GOOD = `List<String> run(List<Step> steps) {
  Deque<Step> done = new ArrayDeque<>();
  for (Step s : steps) {
    if (s.ok()) { done.push(s); }              // шаг прошёл
    else {
      while (!done.isEmpty())
        compensate(done.pop());                // откат завершённых в обратном порядке
      break;
    }
  }
}
// createOrder ✓, chargePayment ✓, reserveInventory ✗ → refund, cancel order`

const SAGA_BAD = `// Шаги без компенсаций
createOrder();        // ✓ закоммичен
chargePayment();      // ✓ деньги списаны
reserveInventory();   // ✗ нет товара → исключение
// откатить createOrder и chargePayment нечем → рассинхрон`

const CQRS_GOOD = `class WriteSide {                       // команды
  void deposit(int amount) { balance += amount; }
}
class ReadSide {                        // запросы
  void project(int balance) { view = "balance: " + balance; } // денормализация
  String query() { return view; }       // быстрый read
}

write.deposit(100); read.project(write.balance());
read.query();   // "balance: 150" из проекции`

const CQRS_BAD = `// Одна модель: запрос пересчитывает по всему логу
class Model {
  List<Integer> deposits = new ArrayList<>();
  void deposit(int a) { deposits.add(a); }
  int query() {
    int sum = 0;
    for (int d : deposits) sum += d;   // O(n) на каждый запрос
    return sum;
  }
}`

const ES_GOOD = `class Account {
  private final List<String> events = new ArrayList<>();   // append-only лог
  void apply(String event) { events.add(event); }
  int balanceAt(int version) {                              // свёртка событий
    int b = 0;
    for (int i = 0; i < version; i++) b += delta(events.get(i));
    return b;
  }
}

acc.apply("deposit 100"); acc.apply("withdraw 30"); acc.apply("deposit 50");
acc.balance();        // 120 (свёртка)
acc.balanceAt(2);     // 70  (replay в прошлое)`

const ES_BAD = `// Хранится только текущее значение
class Account {
  private int balance = 0;
  void deposit(int a)  { balance += a; }   // мутация на месте
  void withdraw(int a) { balance -= a; }
}
// balance = 120, но "как пришли?" и "сколько было после 2 операций?" — не ответить`

const OUTBOX_GOOD = `@Transactional
void createOrder(Order o) {
  db.insert(o);                       // бизнес-строка
  db.insert(outbox("OrderCreated"));  // и сообщение — в ОДНОЙ транзакции
}

// отдельный relay:
void poll() {
  for (var msg : db.outbox()) broker.publish(msg); // at-least-once
  db.clearOutbox();
}`

const OUTBOX_BAD = `// Dual write: две независимые записи
db.save(order);          // COMMIT в БД
// ←—— процесс падает здесь
broker.publish(event);   // не выполнено → сообщение потеряно
// в БД заказ есть, downstream о нём не узнает`

const CB_GOOD = `String call(Downstream ds) {
  if (state == OPEN) return "fast-fail";   // не трогаем упавший сервис
  if (ds.call()) { failures = 0; state = CLOSED; return "ok"; }
  if (++failures >= threshold) state = OPEN; // разомкнуть после N ошибок
  return "fail";
}
// OPEN → (пауза) → HALF_OPEN → пробный вызов → CLOSED при успехе`

const CB_BAD = `// Без брейкера: каждый вызов ждёт таймаут упавшего сервиса
for (int i = 0; i < 5; i++) {
  downstream.call();   // fail (таймаут) — поток висит
}
// зависшие вызовы копятся → каскадный отказ вызывающего сервиса`

const RETRY_GOOD = `for (int n = 1; n <= maxAttempts; n++) {
  long wait = (n == 1) ? 0 : 100L * (1L << (n - 2)); // 0,100,200,400…
  sleep(wait);
  if (service.call()) return SUCCESS;                // успех — выходим
}
// падает 2 раза, успех на 3-й попытке`

const RETRY_BAD = `// Одна попытка, без повторов
boolean ok = service.call();   // ✗ транзиентный сбой → запрос потерян
// а 3-я попытка прошла бы.
// обратная крайность: while(!ok) call();  // шторм без backoff`

const BULKHEAD_GOOD = `Pool reportPool  = new Pool("reportPool", 2);   // отдельные отсеки
Pool paymentPool = new Pool("paymentPool", 2);

reportPool.acquire(); reportPool.acquire();      // report: 2/2 (full)
reportPool.acquire();                            // reject — но только report
paymentPool.acquire();                           // ok — payment изолирован`

const BULKHEAD_BAD = `// Один общий пул на всё
SharedPool pool = new SharedPool(4);
for (int i = 0; i < 4; i++) pool.acquire();   // report занял все 4 слота
pool.acquire();                                // payment → reject (голодает)`

const RATELIMIT_GOOD = `class TokenBucket {
  private int tokens = capacity;          // 3
  boolean tryAcquire() {
    if (tokens > 0) { tokens--; return true; }
    return false;                          // 429 Too Many Requests
  }
}
// 5 запросов при лимите 3 → 3 проходят, 2 получают 429`

const RATELIMIT_BAD = `// Без лимита: принимаем всё подряд
for (int i = 0; i < 5; i++) service.handle();   // load растёт без границ
// всплеск проходит целиком → перегрузка/падение`

export const DETAILS = {
  'circuit-breaker': {
    name: 'Circuit Breaker',
    ru: 'Предохранитель',
    intent: 'Размыкает вызовы к упавшей зависимости после серии ошибок и мгновенно отклоняет их, давая сервису восстановиться.',
    aka: 'Предохранитель',
    motivation: [
      'Когда downstream падает, каждый вызов ждёт таймаут и держит поток. Под нагрузкой зависшие вызовы исчерпывают пулы и каскадом валят вызывающий сервис.',
      'Брейкер считает ошибки: после порога переходит в OPEN и мгновенно отклоняет вызовы (fast-fail), не трогая упавший сервис. Через паузу пробует один вызов (HALF_OPEN) и закрывается при успехе.',
    ],
    pros: [
      'Останавливает каскад таймаутов (fail-fast)',
      'Даёт упавшей зависимости время восстановиться',
      'Освобождает потоки и пулы вызывающего',
    ],
    cons: [
      'Нужно настраивать пороги/таймауты',
      'В OPEN часть валидных запросов тоже отклоняется',
    ],
    useCases: [
      { src: 'Resilience4j', item: 'CircuitBreaker' },
      { src: 'Netflix', item: 'Hystrix (legacy)' },
      { src: 'Istio', item: 'outlier detection' },
    ],
    related: [
      { name: 'Retry', id: 'retry', kind: 'resil', why: 'часто комбинируют: retry под брейкером' },
      { name: 'Bulkhead', id: 'bulkhead', kind: 'resil', why: 'оба ограничивают радиус сбоя' },
      { name: 'Proxy', id: 'proxy', kind: 'struct', why: 'брейкер — обёртка-заместитель вызова' },
    ],
    code: { good: CB_GOOD, bad: CB_BAD },
    viz: 'circuit-breaker',
    live: 'circuit-breaker',
    backend: '/patterns/microservices/resilience/circuit-breaker',
    preview: {
      instances: [
        { hash: 'CLOSED', createdBy: 'state' },
        { hash: 'OPEN', createdBy: 'state' },
        { hash: 'HALF_OPEN', createdBy: 'state' },
      ],
      frame: { actor: 'OPEN', action: 'call', result: 'fast-fail (downstream не вызван)', ok: true },
    },
  },
  retry: {
    name: 'Retry',
    ru: 'Повтор',
    intent: 'Повторяет неудавшийся вызов несколько раз с нарастающей паузой, чтобы пережить транзиентные сбои.',
    aka: 'Повтор с backoff',
    motivation: [
      'Сеть и поды иногда «моргают»: одиночная транзиентная ошибка роняет запрос, хотя следующая попытка прошла бы. А повтор без пауз превращается в шторм, добивающий сервис.',
      'Retry повторяет вызов с экспоненциальным backoff (и jitter), переживая временные сбои и не создавая лавины повторов.',
    ],
    pros: [
      'Переживает транзиентные сбои незаметно для клиента',
      'Экспоненциальный backoff + jitter гасят шторм',
      'Просто добавить вокруг идемпотентного вызова',
    ],
    cons: [
      'Только для идемпотентных операций',
      'Повторы добавляют задержку; нужен предел попыток',
    ],
    useCases: [
      { src: 'Resilience4j', item: 'Retry' },
      { src: 'Spring', item: 'Spring Retry / @Retryable' },
      { src: 'AWS SDK', item: 'exponential backoff с jitter' },
    ],
    related: [
      { name: 'Circuit Breaker', id: 'circuit-breaker', kind: 'resil', why: 'брейкер обрывает бесполезные повторы' },
      { name: 'Rate Limiter', id: 'rate-limiter', kind: 'resil', why: 'ограничивает интенсивность повторов' },
      { name: 'Transactional Outbox', id: 'transactional-outbox', kind: 'data', why: 'надёжная доставка вместо повтора на лету' },
    ],
    code: { good: RETRY_GOOD, bad: RETRY_BAD },
    viz: 'retry',
    live: 'retry',
    backend: '/patterns/microservices/resilience/retry',
    preview: {
      instances: [
        { hash: 'attempt 1', createdBy: '0ms · fail' },
        { hash: 'attempt 2', createdBy: '100ms · fail' },
        { hash: 'attempt 3', createdBy: '200ms · ok' },
      ],
      frame: { actor: 'attempt 3', action: 'backoff 200ms → call', result: '✓ ok', ok: true },
    },
  },
  bulkhead: {
    name: 'Bulkhead',
    ru: 'Переборка',
    intent: 'Делит ресурсы на изолированные пулы, чтобы сбой или насыщение одной зависимости не топили остальные.',
    aka: 'Переборка (как в корабле)',
    motivation: [
      'Один общий пул потоков/соединений на все зависимости опасен: медленная зависимость занимает все слоты, и не связанные с ней вызовы голодают — частичный сбой расползается на всё.',
      'Bulkhead режет ресурсы на отсеки: у каждой зависимости свой пул. Насыщение одного отсека не затрагивает другие, ограничивая радиус поражения.',
    ],
    pros: [
      'Изолирует сбой/насыщение зависимости',
      'Гарантирует ресурсы критичным вызовам',
      'Ограничивает радиус поражения',
    ],
    cons: [
      'Ресурсы поделены — ниже пиковая утилизация',
      'Нужно подбирать размеры отсеков',
    ],
    useCases: [
      { src: 'Resilience4j', item: 'Bulkhead / ThreadPoolBulkhead' },
      { src: 'Hystrix', item: 'изоляция по thread-pool' },
      { src: 'K8s', item: 'resource limits на под' },
    ],
    related: [
      { name: 'Circuit Breaker', id: 'circuit-breaker', kind: 'resil', why: 'оба ограничивают радиус сбоя' },
      { name: 'Rate Limiter', id: 'rate-limiter', kind: 'resil', why: 'тоже ограничивает нагрузку' },
      { name: 'Sidecar', id: 'sidecar', kind: 'comm', why: 'часто настраивается в сайдкаре/меше' },
    ],
    code: { good: BULKHEAD_GOOD, bad: BULKHEAD_BAD },
    viz: 'bulkhead',
    live: 'bulkhead',
    backend: '/patterns/microservices/resilience/bulkhead',
    preview: {
      instances: [
        { hash: 'paymentPool', createdBy: '1/2 · ok' },
        { hash: 'reportPool', createdBy: '2/2 · full' },
      ],
      frame: { actor: 'paymentPool', action: 'payment acquire', result: 'ok (изолирован)', ok: true },
    },
  },
  'rate-limiter': {
    name: 'Rate Limiter',
    ru: 'Ограничитель скорости',
    intent: 'Ограничивает частоту запросов к сервису, отклоняя лишние, чтобы защитить его от перегрузки.',
    aka: 'Throttling / Token Bucket',
    motivation: [
      'Без лимита любой всплеск трафика (или цикл повторов) проходит целиком и превышает ёмкость сервиса — деградация или падение под нагрузкой.',
      'Rate Limiter (token bucket) выдаёт ограниченное число токенов на окно; запросы сверх лимита быстро отклоняются с 429, удерживая нагрузку на посильном уровне.',
    ],
    pros: [
      'Защищает сервис от перегрузки и всплесков',
      'Справедливое распределение ёмкости между клиентами',
      'Дешёвый отказ (429) вместо деградации всех',
    ],
    cons: [
      'Часть валидных запросов отклоняется',
      'Нужны настройка лимитов и распределённый счётчик',
    ],
    useCases: [
      { src: 'Resilience4j', item: 'RateLimiter' },
      { src: 'Gateway', item: 'лимиты на API Gateway / nginx' },
      { src: 'Bucket4j', item: 'token bucket для Java' },
    ],
    related: [
      { name: 'Bulkhead', id: 'bulkhead', kind: 'resil', why: 'оба ограничивают нагрузку/ресурсы' },
      { name: 'Circuit Breaker', id: 'circuit-breaker', kind: 'resil', why: 'дополняют друг друга в защите' },
      { name: 'API Gateway', id: 'api-gateway', kind: 'decomp', why: 'лимиты часто живут на шлюзе' },
    ],
    code: { good: RATELIMIT_GOOD, bad: RATELIMIT_BAD },
    viz: 'rate-limiter',
    live: 'rate-limiter',
    backend: '/patterns/microservices/resilience/rate-limiter',
    preview: {
      instances: [
        { hash: 'req 1', createdBy: 'allowed' },
        { hash: 'req 2', createdBy: 'allowed' },
        { hash: 'req 3', createdBy: 'allowed' },
        { hash: 'req 4', createdBy: '429' },
        { hash: 'req 5', createdBy: '429' },
      ],
      frame: { actor: 'req 4', action: 'tryAcquire', result: '✗ 429 Too Many Requests', ok: false },
    },
  },
  saga: {
    name: 'Saga',
    ru: 'Сага',
    intent: 'Поддерживает согласованность данных в распределённой транзакции через последовательность локальных шагов с компенсациями.',
    aka: 'Сага',
    motivation: [
      'Операция охватывает несколько сервисов (заказ, оплата, склад), а распределённой ACID-транзакции (2PC) нет или она дорогая. Если поздний шаг падает, ранние уже закоммичены — рассинхрон.',
      'Сага выполняет шаги по очереди и при сбое запускает компенсирующие действия для уже завершённых шагов в обратном порядке, возвращая систему в согласованное состояние.',
    ],
    pros: [
      'Согласованность без распределённой блокировки (2PC)',
      'Каждый шаг — локальная транзакция своего сервиса',
      'Явные компенсации описывают откат',
    ],
    cons: [
      'Только eventual consistency (нет изоляции)',
      'Компенсации сложно проектировать и тестировать',
    ],
    useCases: [
      { src: 'Axon', item: 'Saga в event-driven системах' },
      { src: 'Camunda', item: 'оркестрация саги/процесса' },
      { src: 'Order', item: 'оформление заказа через сервисы' },
    ],
    related: [
      { name: 'Publish-Subscribe', id: 'publish-subscribe', kind: 'comm', why: 'хореографическая сага на событиях' },
      { name: 'Command', id: 'command', kind: 'behav', why: 'шаг + компенсация как execute/undo' },
      { name: 'Transactional Outbox', id: 'transactional-outbox', kind: 'data', why: 'надёжная публикация событий саги' },
    ],
    code: { good: SAGA_GOOD, bad: SAGA_BAD },
    viz: 'saga',
    live: 'saga',
    backend: '/patterns/microservices/data/saga',
    preview: {
      instances: [
        { hash: 'createOrder', createdBy: 'compensated' },
        { hash: 'chargePayment', createdBy: 'compensated' },
        { hash: 'reserveInventory', createdBy: 'failed' },
      ],
      frame: { actor: 'chargePayment', action: 'compensate', result: '↩ refund (деньги возвращены)', ok: true },
    },
  },
  cqrs: {
    name: 'CQRS',
    ru: 'CQRS',
    intent: 'Разделяет модели команд (запись) и запросов (чтение), позволяя оптимизировать и масштабировать их независимо.',
    aka: 'Command Query Responsibility Segregation',
    motivation: [
      'Когда чтение и запись делят одну модель, сложные запросы конкурируют с командами за блокировки и тормозят; одна схема не оптимальна и для того, и для другого.',
      'CQRS разносит стороны: команды меняют write-модель и обновляют денормализованную read-проекцию, а запросы читают готовую проекцию — быстро и без конкуренции.',
    ],
    pros: [
      'Чтение и запись оптимизируются/масштабируются отдельно',
      'Денормализованные проекции под конкретные запросы',
      'Меньше конкуренции за блокировки',
    ],
    cons: [
      'Сложнее: две модели и синхронизация проекций',
      'Eventual consistency между write и read',
    ],
    useCases: [
      { src: 'Axon', item: 'CQRS + Event Sourcing' },
      { src: 'Search', item: 'read-модель в Elasticsearch' },
      { src: 'Reporting', item: 'отдельные read-реплики' },
    ],
    related: [
      { name: 'Event Sourcing', id: 'event-sourcing', kind: 'data', why: 'события источника строят read-проекции' },
      { name: 'Observer', id: 'observer', kind: 'behav', why: 'проекции обновляются по событиям' },
      { name: 'Saga', id: 'saga', kind: 'data', why: 'часто работают в паре через события' },
    ],
    code: { good: CQRS_GOOD, bad: CQRS_BAD },
    viz: 'cqrs',
    live: 'cqrs',
    backend: '/patterns/microservices/data/cqrs',
    preview: {
      instances: [
        { hash: 'WriteSide', createdBy: 'commands' },
        { hash: 'ReadSide', createdBy: 'queries' },
      ],
      frame: { actor: 'ReadSide', action: 'query', result: 'balance: 150 (из проекции)', ok: true },
    },
  },
  'event-sourcing': {
    name: 'Event Sourcing',
    ru: 'Источник событий',
    intent: 'Хранит состояние как append-only последовательность событий; текущее состояние получается их свёрткой.',
    aka: 'Источник событий',
    motivation: [
      'Если хранить только текущее значение и мутировать его на месте, история теряется: нельзя провести аудит, восстановить состояние на момент в прошлом или перепроиграть события.',
      'Event Sourcing делает источником истины лог событий. Состояние выводится свёрткой, а лог даёт аудит, replay и построение любых проекций.',
    ],
    pros: [
      'Полный аудит и история изменений',
      'Replay и восстановление состояния на любой момент',
      'Из лога строятся любые проекции (с CQRS)',
    ],
    cons: [
      'Сложнее: версионирование событий, снапшоты',
      'Запросы требуют проекций (свёртка дорога)',
    ],
    useCases: [
      { src: 'Kafka', item: 'лог как источник истины' },
      { src: 'Banking', item: 'журнал транзакций (ledger)' },
      { src: 'Axon', item: 'event store' },
    ],
    related: [
      { name: 'CQRS', id: 'cqrs', kind: 'data', why: 'проекции чтения из событий' },
      { name: 'Memento', id: 'memento', kind: 'behav', why: 'снапшоты состояния для ускорения' },
      { name: 'Saga', id: 'saga', kind: 'data', why: 'саги реагируют на доменные события' },
    ],
    code: { good: ES_GOOD, bad: ES_BAD },
    viz: 'event-sourcing',
    live: 'event-sourcing',
    backend: '/patterns/microservices/data/event-sourcing',
    preview: {
      instances: [
        { hash: 'deposit 100', createdBy: '+100' },
        { hash: 'withdraw 30', createdBy: '-30' },
        { hash: 'deposit 50', createdBy: '+50' },
      ],
      frame: { actor: 'deposit 50', action: 'append', result: 'Σ = 120', ok: true, reveal: 3 },
    },
  },
  'transactional-outbox': {
    name: 'Transactional Outbox',
    ru: 'Транзакционный outbox',
    intent: 'Гарантирует атомарность изменения данных и отправки сообщения, записывая их в одну транзакцию БД и публикуя из outbox отдельным relay.',
    aka: 'Transactional Outbox',
    motivation: [
      'Dual write (сначала commit в БД, потом publish в брокер) не атомарен: падение между шагами оставляет данные без сообщения (или наоборот) — системы рассинхронизируются.',
      'Outbox пишет бизнес-строку и сообщение в одной транзакции БД, а отдельный relay позже читает outbox и публикует (at-least-once). Потерять сообщение нельзя.',
    ],
    pros: [
      'Атомарность данных и сообщения (одна транзакция)',
      'At-least-once доставка без dual write',
      'Переживает падения между шагами',
    ],
    cons: [
      'Нужен relay (polling/CDC) и дедупликация',
      'Доставка at-least-once → возможны дубли',
    ],
    useCases: [
      { src: 'Debezium', item: 'CDC из outbox-таблицы' },
      { src: 'Spring', item: 'Spring Modulith event publication' },
      { src: 'Kafka', item: 'outbox → топик' },
    ],
    related: [
      { name: 'Saga', id: 'saga', kind: 'data', why: 'надёжно публикует события шагов саги' },
      { name: 'Publish-Subscribe', id: 'publish-subscribe', kind: 'comm', why: 'relay публикует в брокер' },
      { name: 'Event Sourcing', id: 'event-sourcing', kind: 'data', why: 'события как источник публикаций' },
    ],
    code: { good: OUTBOX_GOOD, bad: OUTBOX_BAD },
    viz: 'transactional-outbox',
    live: 'transactional-outbox',
    backend: '/patterns/microservices/data/transactional-outbox',
    preview: {
      instances: [
        { hash: 'DB+Outbox', createdBy: 'one tx' },
        { hash: 'Relay', createdBy: 'poll' },
        { hash: 'Broker', createdBy: 'published' },
      ],
      frame: { actor: 'Relay', action: 'poll → publish', result: 'Broker получил [OrderCreated]', ok: true },
    },
  },
  'service-discovery': {
    name: 'Service Discovery',
    ru: 'Обнаружение сервисов',
    intent: 'Позволяет клиентам находить сетевые адреса сервисов динамически через реестр, а не по жёстко зашитым адресам.',
    aka: 'Service Registry / Обнаружение сервисов',
    motivation: [
      'В облаке инстансы постоянно поднимаются, гибнут и переезжают. Жёстко зашитый host:port рвётся при любом пересоздании инстанса или масштабировании.',
      'Сервисы регистрируются в реестре, а клиент резолвит их по имени и получает актуальный живой адрес (с балансировкой). Переезд инстанса для клиента прозрачен.',
    ],
    pros: [
      'Адреса не зашиты — переезд/масштабирование прозрачны',
      'Балансировка по живым инстансам',
      'Автоматический failover при гибели инстанса',
    ],
    cons: [
      'Реестр — критичный компонент (нужна отказоустойчивость)',
      'Лишний сетевой хоп/кэш при резолве',
    ],
    useCases: [
      { src: 'Netflix', item: 'Eureka' },
      { src: 'HashiCorp', item: 'Consul' },
      { src: 'K8s', item: 'kube-dns / Services' },
    ],
    related: [
      { name: 'API Gateway', id: 'api-gateway', kind: 'decomp', why: 'шлюз резолвит сервисы через реестр' },
      { name: 'Sidecar', id: 'sidecar', kind: 'comm', why: 'резолв часто делает сайдкар/прокси' },
    ],
    code: { good: DISCOVERY_GOOD, bad: DISCOVERY_BAD },
    viz: 'service-discovery',
    live: 'service-discovery',
    backend: '/patterns/microservices/communication/service-discovery',
    preview: {
      instances: [
        { hash: '10.0.0.1:8080', createdBy: 'instance' },
        { hash: '10.0.0.2:8080', createdBy: 'instance' },
      ],
      frame: { actor: '10.0.0.2:8080', action: 'resolve()', result: '→ 10.0.0.2:8080', ok: true },
    },
  },
  sidecar: {
    name: 'Sidecar',
    ru: 'Прицеп',
    intent: 'Выносит сквозные задачи (TLS, ретраи, метрики, лимиты) в отдельный процесс рядом с приложением, не меняя его код.',
    aka: 'Sidecar / Ambassador',
    motivation: [
      'Если каждый сервис сам реализует TLS, ретраи, метрики и лимиты, инфра-код дублируется по всем сервисам и перемешивается с бизнес-логикой; обновить политику — править все сервисы.',
      'Сайдкар — отдельный процесс в том же поде, который перехватывает трафик и берёт сквозные задачи на себя. Приложение остаётся только про бизнес, а сайдкар переиспользуется всеми.',
    ],
    pros: [
      'Сквозные задачи вне бизнес-кода',
      'Переиспользуется любым сервисом и языком',
      'Политики обновляются централизованно',
    ],
    cons: [
      'Лишний процесс на каждый под (ресурсы, латентность)',
      'Сложнее локальная отладка и эксплуатация',
    ],
    useCases: [
      { src: 'Istio', item: 'Envoy как sidecar-прокси' },
      { src: 'Linkerd', item: 'data-plane proxy' },
      { src: 'Dapr', item: 'sidecar для building blocks' },
    ],
    related: [
      { name: 'Service Discovery', id: 'service-discovery', kind: 'comm', why: 'резолв адресов часто в сайдкаре' },
      { name: 'API Gateway', id: 'api-gateway', kind: 'decomp', why: 'сквозные задачи на краю vs рядом с сервисом' },
      { name: 'Proxy', id: 'proxy', kind: 'struct', why: 'тот же приём перехвата на уровне кода' },
    ],
    code: { good: SIDECAR_GOOD, bad: SIDECAR_BAD },
    viz: 'sidecar',
    live: 'sidecar',
    backend: '/patterns/microservices/communication/sidecar',
    preview: {
      instances: [
        { hash: 'OrderApp', createdBy: 'business' },
        { hash: 'Sidecar', createdBy: 'cross-cutting' },
      ],
      frame: { actor: 'OrderApp', action: 'handle(#42)', result: 'biz: order:#42 (чистая логика)', ok: true },
    },
  },
  'publish-subscribe': {
    name: 'Publish-Subscribe',
    ru: 'Издатель-подписчик',
    intent: 'Издатель публикует события в брокер, а подписчики получают их асинхронно и независимо, не зная друг о друге.',
    aka: 'Pub/Sub / Event-Driven',
    motivation: [
      'Когда издатель синхронно вызывает каждого получателя по очереди, он связан с ними и их порядком; сбой одного получателя каскадом обрывает доставку остальным.',
      'Брокер развязывает стороны: издатель публикует событие и забывает о нём, а подписчики обрабатывают его независимо. Новый подписчик не требует правок издателя.',
    ],
    pros: [
      'Слабая связанность издателя и подписчиков',
      'Независимая доставка — сбой одного не рушит остальных',
      'Подписчики добавляются без правки издателя',
    ],
    cons: [
      'Брокер — инфраструктура и точка отказа',
      'Сложнее отладка и гарантии доставки/порядка',
    ],
    useCases: [
      { src: 'Kafka', item: 'topics / consumer groups' },
      { src: 'RabbitMQ', item: 'exchange → queues' },
      { src: 'Cloud', item: 'SNS+SQS / Google Pub/Sub' },
    ],
    related: [
      { name: 'Observer', id: 'observer', kind: 'behav', why: 'тот же приём «издатель → подписчики» в коде' },
      { name: 'Mediator', id: 'mediator', kind: 'behav', why: 'брокер централизует коммуникацию' },
      { name: 'Saga', id: 'saga', kind: 'data', why: 'саги часто на событиях через брокер' },
    ],
    code: { good: PUBSUB_GOOD, bad: PUBSUB_BAD },
    viz: 'publish-subscribe',
    live: 'publish-subscribe',
    backend: '/patterns/microservices/communication/publish-subscribe',
    preview: {
      instances: [
        { hash: 'EmailConsumer', createdBy: 'subscriber' },
        { hash: 'AnalyticsConsumer', createdBy: 'subscriber' },
        { hash: 'InventoryConsumer', createdBy: 'subscriber' },
      ],
      frame: { actor: 'AnalyticsConsumer', action: 'deliver', result: 'получил OrderPlaced', ok: true },
    },
  },
  'api-gateway': {
    name: 'API Gateway',
    ru: 'API-шлюз',
    intent: 'Единая точка входа для клиентов: маршрутизирует запросы к сервисам, агрегирует ответы и берёт на себя сквозные задачи.',
    aka: 'API-шлюз / Edge Service',
    motivation: [
      'Если клиент сам ходит в каждый микросервис, он становится «болтливым»: несколько сетевых round-trip на один экран и жёсткое знание адресов и состава всех сервисов.',
      'Шлюз даёт один вход: клиент делает один запрос, а шлюз сам обращается к нужным сервисам, агрегирует ответ и скрывает топологию бэкенда.',
    ],
    pros: [
      'Один вход вместо знания всех сервисов',
      'Агрегация снижает число round-trip клиента',
      'Удобное место для auth, лимитов, кэша',
    ],
    cons: [
      'Ещё один компонент и точка отказа',
      'Может стать узким местом / «божественным» шлюзом',
    ],
    useCases: [
      { src: 'Spring', item: 'Spring Cloud Gateway' },
      { src: 'Netflix', item: 'Zuul' },
      { src: 'Cloud', item: 'AWS API Gateway / Kong' },
    ],
    related: [
      { name: 'Backend for Frontend', id: 'backend-for-frontend', kind: 'decomp', why: 'шлюз под конкретный тип клиента' },
      { name: 'Service Discovery', id: 'service-discovery', kind: 'comm', why: 'шлюз резолвит сервисы через реестр' },
      { name: 'Facade', id: 'facade', kind: 'struct', why: 'тот же приём «единый фасад» на уровне кода' },
    ],
    code: { good: APIGW_GOOD, bad: APIGW_BAD },
    viz: 'api-gateway',
    live: 'api-gateway',
    backend: '/patterns/microservices/decomposition/api-gateway',
    preview: {
      instances: [
        { hash: 'Client', createdBy: 'client' },
        { hash: 'Gateway', createdBy: 'gateway' },
        { hash: 'UserService', createdBy: 'service' },
        { hash: 'OrderService', createdBy: 'service' },
        { hash: 'InventoryService', createdBy: 'service' },
      ],
      frame: { actor: 'OrderService', action: 'Gateway → orders.list', result: '[order#1, order#2]', ok: true },
    },
  },
  'backend-for-frontend': {
    name: 'Backend for Frontend',
    ru: 'BFF',
    intent: 'Отдельный шлюз-бэкенд на каждый тип клиента, отдающий ровно тот payload и API, что нужен именно этому клиенту.',
    aka: 'BFF',
    motivation: [
      'Один общий API под web и mobile превращается в компромисс: мобильный клиент качает поля, которые ему не нужны (overfetch), а изменения под один клиент рискуют сломать другой.',
      'BFF выделяет шлюз под каждый клиент: мобильный BFF отдаёт компактный payload, веб-BFF — полный. Контракты независимы и заточены под свой UI.',
    ],
    pros: [
      'Payload и API заточены под клиента — нет overfetch',
      'Изменения под один клиент не задевают других',
      'Команда клиента владеет своим BFF',
    ],
    cons: [
      'Дублирование логики между BFF',
      'Больше развёртываемых компонентов',
    ],
    useCases: [
      { src: 'Netflix', item: 'device-specific edge API' },
      { src: 'SoundCloud', item: 'ввели термин BFF' },
      { src: 'GraphQL', item: 'часто реализует роль BFF' },
    ],
    related: [
      { name: 'API Gateway', id: 'api-gateway', kind: 'decomp', why: 'BFF — специализация шлюза под клиента' },
      { name: 'Adapter', id: 'adapter', kind: 'struct', why: 'подгоняет ответ под нужный клиенту формат' },
    ],
    code: { good: BFF_GOOD, bad: BFF_BAD },
    viz: 'backend-for-frontend',
    live: 'backend-for-frontend',
    backend: '/patterns/microservices/decomposition/backend-for-frontend',
    preview: {
      instances: [
        { hash: 'MobileBFF', createdBy: '2 поля' },
        { hash: 'WebBFF', createdBy: '5 полей' },
      ],
      frame: { actor: 'MobileBFF', action: 'mobile → product()', result: '[name, price]', ok: true },
    },
  },
  'strangler-fig': {
    name: 'Strangler Fig',
    ru: 'Душитель',
    intent: 'Постепенно заменяет легаси-систему, перенаправляя по одному маршруту на новый сервис, пока старая система не «задушена».',
    aka: 'Strangler Application / Душитель',
    motivation: [
      'Переписать большую систему целиком (big-bang) — это месяцы заморозки фич и огромный риск на выкатке: либо всё взлетело, либо всё упало.',
      'Strangler Fig ставит фасад-роутер перед старой и новой системами и переключает маршруты по одному. Система всё время живая, риск разбит на маленькие обратимые шаги.',
    ],
    pros: [
      'Миграция без big-bang и долгой заморозки',
      'Риск разбит на мелкие обратимые шаги',
      'Старая и новая системы работают параллельно',
    ],
    cons: [
      'Долгое сосуществование двух систем',
      'Нужен фасад-роутер и слой совместимости',
    ],
    useCases: [
      { src: 'Cloud', item: 'миграция монолита в облако по частям' },
      { src: 'Proxy', item: 'роутинг через reverse-proxy/шлюз' },
      { src: 'Legacy', item: 'постепенная замена унаследованных модулей' },
    ],
    related: [
      { name: 'API Gateway', id: 'api-gateway', kind: 'decomp', why: 'фасад-роутер для маршрутизации трафика' },
      { name: 'Proxy', id: 'proxy', kind: 'struct', why: 'тот же приём перехвата вызовов' },
    ],
    code: { good: STRANGLER_GOOD, bad: STRANGLER_BAD },
    viz: 'strangler-fig',
    live: 'strangler-fig',
    backend: '/patterns/microservices/decomposition/strangler-fig',
    preview: {
      instances: [
        { hash: '/login', createdBy: 'route' },
        { hash: '/cart', createdBy: 'route' },
        { hash: '/pay', createdBy: 'route' },
        { hash: '/profile', createdBy: 'route' },
      ],
      frame: { actor: '/cart', action: 'migrate(/cart)', result: '→ new (2/4)', ok: true, reveal: 2 },
    },
  },
  'factory-method': {
    name: 'Factory Method',
    ru: 'Фабричный метод',
    intent: 'Определяет интерфейс создания объекта, но позволяет подклассам решать, какой конкретный класс инстанцировать.',
    aka: 'Virtual Constructor / Фабричный метод',
    motivation: [
      'Сервис уведомлений должен слать сообщения через e-mail, SMS, push. Если клиент сам делает new EmailNotifier() / new SmsNotifier(), он намертво привязан к конкретным классам и к их списку.',
      'Решение: вынести создание в фабричный метод create(). Каждый канал — отдельный подкласс-фабрика, который переопределяет только create(). Клиент работает с абстракциями Creator/Notifier; новый канал не трогает существующий код.',
    ],
    pros: [
      'Убирает привязку клиента к конкретным классам продуктов',
      'Single Responsibility: создание собрано в одном методе',
      'Open/Closed: новый продукт = новый подкласс-фабрика',
    ],
    cons: [
      'Растёт число классов (фабрика на каждый продукт)',
      'Нужна иерархия Creator даже ради одного продукта',
    ],
    useCases: [
      { src: 'JDK', item: 'Collection.iterator()' },
      { src: 'JDK', item: 'Calendar.getInstance()' },
      { src: 'Spring', item: 'BeanFactory / FactoryBean<T>' },
      { src: 'JDBC', item: 'Connection.createStatement()' },
    ],
    related: [
      { name: 'Abstract Factory', id: 'abstract-factory', kind: 'creat', why: 'часто реализуется набором фабричных методов' },
      { name: 'Template Method', id: 'template-method', kind: 'behav', why: 'create() — частный случай хука' },
      { name: 'Prototype', id: 'prototype', kind: 'creat', why: 'альтернатива созданию через клонирование' },
    ],
    code: { good: FACTORY_GOOD, bad: FACTORY_BAD },
    viz: 'factory-method',
    live: 'factory-method',
    backend: '/patterns/creational/factory-method',
    preview: {
      instances: [
        { hash: 'EmailNotifier', createdBy: 'EmailFactory' },
        { hash: 'SmsNotifier', createdBy: 'SmsFactory' },
        { hash: 'PushNotifier', createdBy: 'PushFactory' },
      ],
      frame: { actor: 'SmsNotifier', action: 'SmsFactory.create()', result: '→ new SmsNotifier', ok: true },
    },
  },
  'abstract-factory': {
    name: 'Abstract Factory',
    ru: 'Абстрактная фабрика',
    intent: 'Предоставляет интерфейс для создания целых семейств связанных продуктов без указания их конкретных классов.',
    aka: 'Kit / Абстрактная фабрика',
    motivation: [
      'UI-тулкит должен поддерживать Windows и macOS. Виджеты внутри одной платформы обязаны сочетаться: нельзя смешать кнопку Windows и чекбокс macOS. Если клиент сам делает new под каждый виджет, такую гарантию никто не даёт.',
      'Решение: одна фабрика создаёт всё семейство (createButton + createCheckbox). Клиент выбирает фабрику один раз и получает согласованные продукты. Смена платформы — другая фабрика, и перемешать стили физически невозможно.',
    ],
    pros: [
      'Гарантирует совместимость продуктов одного семейства',
      'Изолирует клиента от конкретных классов',
      'Смена семейства — заменой одной фабрики',
    ],
    cons: [
      'Много интерфейсов и классов',
      'Добавить новый вид продукта тяжело — правки во всех фабриках',
    ],
    useCases: [
      { src: 'JDK', item: 'DocumentBuilderFactory / SAXParserFactory' },
      { src: 'JDK', item: 'javax.xml.transform.TransformerFactory' },
      { src: 'Swing', item: 'LookAndFeel — семейства UI-компонентов' },
    ],
    related: [
      { name: 'Factory Method', id: 'factory-method', kind: 'creat', why: 'фабрика часто состоит из фабричных методов' },
      { name: 'Singleton', id: 'singleton', kind: 'creat', why: 'конкретная фабрика обычно одна на приложение' },
      { name: 'Builder', id: 'builder', kind: 'creat', why: 'тоже про создание сложных объектов' },
    ],
    code: { good: ABSTRACT_GOOD, bad: ABSTRACT_BAD },
    viz: 'abstract-factory',
    live: 'abstract-factory',
    backend: '/patterns/creational/abstract-factory',
    preview: {
      instances: [
        { hash: 'WinButton', createdBy: 'Windows' },
        { hash: 'WinCheckbox', createdBy: 'Windows' },
      ],
      frame: { actor: 'WinCheckbox', action: 'WinFactory.createCheckbox()', result: '[Win ☑ checkbox]', ok: true },
    },
  },
  builder: {
    name: 'Builder',
    ru: 'Строитель',
    intent: 'Отделяет конструирование сложного объекта от его представления, позволяя собирать объект пошагово.',
    aka: 'Строитель',
    motivation: [
      'У объекта много опциональных полей (размер, тесто, сыр, десяток начинок). Конструктор на все случаи превращается в телескоп из перегрузок, а позиционные булевы аргументы легко переставить местами — тихий баг.',
      'Решение: текучий Builder с именованными шагами. Код читается как описание, build() валидирует обязательные поля и отдаёт иммутабельный объект. Опциональные части задаются явно и по желанию.',
    ],
    pros: [
      'Читаемая пошаговая сборка вместо телескопа конструкторов',
      'Валидация и иммутабельность в build()',
      'Опциональные поля без комбинаторного взрыва перегрузок',
    ],
    cons: [
      'Больше кода — отдельный класс Builder',
      'Оправдан в основном для объектов с многими полями',
    ],
    useCases: [
      { src: 'JDK', item: 'StringBuilder' },
      { src: 'JDK', item: 'Stream.Builder · Calendar.Builder' },
      { src: 'HTTP', item: 'HttpRequest.newBuilder()' },
      { src: 'Lombok', item: '@Builder' },
    ],
    related: [
      { name: 'Abstract Factory', id: 'abstract-factory', kind: 'creat', why: 'тоже о создании сложных объектов' },
      { name: 'Factory Method', id: 'factory-method', kind: 'creat', why: 'альтернатива для простых случаев' },
      { name: 'Composite', id: 'composite', kind: 'struct', why: 'билдеры часто строят деревья Composite' },
    ],
    code: { good: BUILDER_GOOD, bad: BUILDER_BAD },
    viz: 'builder',
    live: 'builder',
    backend: '/patterns/creational/builder',
    preview: {
      instances: [
        { hash: 'size = L', createdBy: '.size("L")' },
        { hash: 'crust = тонкое', createdBy: '.crust(...)' },
        { hash: 'cheese = да', createdBy: '.cheese(true)' },
        { hash: '+ ветчина', createdBy: '.topping(...)' },
        { hash: '+ грибы', createdBy: '.topping(...)' },
      ],
      frame: { actor: '+ грибы', action: '.topping("грибы")', result: 'ok', ok: true, reveal: 5 },
    },
  },
  prototype: {
    name: 'Prototype',
    ru: 'Прототип',
    intent: 'Создаёт новые объекты копированием существующего экземпляра-прототипа, не привязываясь к их конкретным классам.',
    aka: 'Clone / Прототип',
    motivation: [
      'Иногда дешевле скопировать готовый объект, чем собирать новый с нуля (тяжёлая инициализация, конфиг-шаблон). Но наивное клонирование копирует только верхний уровень: вложенный изменяемый список остаётся общим.',
      'Решение: метод copy(), который делает глубокую копию вложенного состояния. Клон получается полностью независимым — правки клона не протекают в оригинал.',
    ],
    pros: [
      'Клонирование без привязки к конкретным классам',
      'Дешевле повторной тяжёлой инициализации',
      'Удобно тиражировать настроенные объекты-шаблоны',
    ],
    cons: [
      'Глубокое копирование циклических ссылок — нетривиально',
      'Легко ошибиться и сделать поверхностную копию',
    ],
    useCases: [
      { src: 'JDK', item: 'Object.clone() / Cloneable' },
      { src: 'JDK', item: 'ArrayList.clone() · копирующие конструкторы' },
      { src: 'Spring', item: '@Scope("prototype") — новый бин на запрос' },
    ],
    related: [
      { name: 'Factory Method', id: 'factory-method', kind: 'creat', why: 'альтернативный способ создания' },
      { name: 'Abstract Factory', id: 'abstract-factory', kind: 'creat', why: 'фабрики иногда хранят прототипы' },
      { name: 'Memento', id: 'memento', kind: 'behav', why: 'тоже про снимок состояния' },
    ],
    code: { good: PROTOTYPE_GOOD, bad: PROTOTYPE_BAD },
    viz: 'prototype',
    live: 'prototype',
    backend: '/patterns/creational/prototype',
    preview: {
      instances: [
        { hash: 'Original', createdBy: 'tags=[draft]' },
        { hash: 'Clone', createdBy: 'tags=[draft, v2]' },
      ],
      frame: { actor: 'Clone', action: 'addTag("v2")', result: 'tags=[draft, v2]', ok: true },
    },
  },
  adapter: {
    name: 'Adapter',
    ru: 'Адаптер',
    intent: 'Преобразует интерфейс класса в другой интерфейс, который ожидает клиент, позволяя работать вместе несовместимым классам.',
    aka: 'Wrapper / Адаптер',
    motivation: [
      'Наш код ожидает интерфейс PaymentGateway.pay(cents), а у банковской библиотеки другая сигнатура и единицы (доллары). Менять чужой код нельзя, а вызывать его напрямую — значит размазать конвертацию по всему приложению.',
      'Решение: адаптер реализует наш интерфейс и внутри транслирует вызов к чужому API. Несовместимость и конвертация живут в одном месте; заменить банк — заменить адаптер.',
    ],
    pros: [
      'Соединяет несовместимые интерфейсы без правки их кода',
      'Конвертация и трансляция собраны в одном месте',
      'Single Responsibility: трансляция отделена от бизнес-логики',
    ],
    cons: [
      'Ещё один слой косвенности',
      'Иногда проще изменить сам сервис, чем плодить адаптеры',
    ],
    useCases: [
      { src: 'JDK', item: 'Arrays.asList() · Collections.list()' },
      { src: 'JDK', item: 'InputStreamReader (byte → char)' },
      { src: 'Spring', item: 'HandlerAdapter' },
    ],
    related: [
      { name: 'Bridge', id: 'bridge', kind: 'struct', why: 'тоже про интерфейсы, но проектируется заранее' },
      { name: 'Decorator', id: 'decorator', kind: 'struct', why: 'оборачивает, но не меняет интерфейс' },
      { name: 'Facade', id: 'facade', kind: 'struct', why: 'упрощает интерфейс целой подсистемы' },
    ],
    code: { good: ADAPTER_GOOD, bad: ADAPTER_BAD },
    viz: 'adapter',
    live: 'adapter',
    backend: '/patterns/structural/adapter',
    preview: {
      instances: [
        { hash: 'PaymentGateway', createdBy: 'наш интерфейс' },
        { hash: 'BankAdapter', createdBy: 'адаптер' },
        { hash: 'LegacyBank', createdBy: 'чужой API' },
      ],
      frame: { actor: 'BankAdapter', action: '2599¢ → $25.99', result: 'makeTransfer(25.99, USD)', ok: true },
    },
  },
  bridge: {
    name: 'Bridge',
    ru: 'Мост',
    intent: 'Разделяет абстракцию и реализацию на две независимые иерархии, которые можно развивать отдельно.',
    aka: 'Handle/Body · Мост',
    motivation: [
      'Есть фигуры (Circle, Square) и способы их рисовать (Vector, Raster). Если делать класс на каждую пару, число классов растёт как произведение: 2×2=4, 3×3=9.',
      'Решение: связать оси композицией. Фигура держит ссылку на рендерер — «мост». Любая фигура работает с любым рендерером, а классов растёт как сумма осей.',
    ],
    pros: [
      'Абстракция и реализация развиваются независимо',
      'Рост классов — сумма осей, а не произведение',
      'Реализацию можно менять в рантайме',
    ],
    cons: [
      'Усложняет код при единственной оси',
      'Нужно заранее выделить две независимые иерархии',
    ],
    useCases: [
      { src: 'JDBC', item: 'Driver / DriverManager' },
      { src: 'SLF4J', item: 'фасад логирования ↔ бэкенды' },
      { src: 'AWT', item: 'Component ↔ peer-реализации' },
    ],
    related: [
      { name: 'Adapter', id: 'adapter', kind: 'struct', why: 'адаптер чинит постфактум, мост — заранее' },
      { name: 'Abstract Factory', id: 'abstract-factory', kind: 'creat', why: 'может создавать реализации для моста' },
      { name: 'State', id: 'state', kind: 'behav', why: 'тоже композиция вместо наследования' },
    ],
    code: { good: BRIDGE_GOOD, bad: BRIDGE_BAD },
    viz: 'bridge',
    live: 'bridge',
    backend: '/patterns/structural/bridge',
    preview: {
      instances: [
        { hash: 'Circle', createdBy: 'abstraction' },
        { hash: 'Square', createdBy: 'abstraction' },
        { hash: 'VectorRenderer', createdBy: 'implementation' },
        { hash: 'RasterRenderer', createdBy: 'implementation' },
      ],
      frame: null,
    },
  },
  composite: {
    name: 'Composite',
    ru: 'Компоновщик',
    intent: 'Группирует объекты в древовидную структуру и позволяет работать с деревом так же, как с отдельным объектом.',
    aka: 'Компоновщик',
    motivation: [
      'Файлы и папки образуют дерево. Если у них нет общего типа, клиент вынужден различать узлы через instanceof и вручную обходить уровни — и легко забывает спуститься во вложенную папку.',
      'Решение: лист и контейнер реализуют один интерфейс Node. Контейнер рекурсивно делегирует операцию детям. Клиент зовёт size() единообразно, не зная глубины дерева.',
    ],
    pros: [
      'Единообразная работа с листом и контейнером',
      'Рекурсия скрыта внутри контейнера',
      'Open/Closed: новые виды узлов без правки клиента',
    ],
    cons: [
      'Слишком общий интерфейс — труднее ограничить состав',
      'Дерево из разнородных узлов сложнее типизировать',
    ],
    useCases: [
      { src: 'JDK', item: 'java.awt Container / Component' },
      { src: 'Swing', item: 'JComponent — дерево виджетов' },
      { src: 'DOM', item: 'Node / Element' },
    ],
    related: [
      { name: 'Decorator', id: 'decorator', kind: 'struct', why: 'тоже рекурсивная композиция' },
      { name: 'Iterator', id: 'iterator', kind: 'behav', why: 'обход дерева Composite' },
      { name: 'Visitor', id: 'visitor', kind: 'behav', why: 'операции над деревом узлов' },
    ],
    code: { good: COMPOSITE_GOOD, bad: COMPOSITE_BAD },
    viz: 'composite',
    live: 'composite',
    backend: '/patterns/structural/composite',
    preview: {
      instances: [
        { hash: 'root/', createdBy: 'dir' },
        { hash: '  a.txt', createdBy: '10' },
        { hash: '  sub/', createdBy: 'dir' },
        { hash: '    b.txt', createdBy: '20' },
        { hash: '    c.txt', createdBy: '5' },
      ],
      frame: { actor: '    c.txt', action: '+5', result: 'сумма = 35', ok: true },
    },
  },
  decorator: {
    name: 'Decorator',
    ru: 'Декоратор',
    intent: 'Динамически добавляет объекту новые обязанности, оборачивая его в объекты-обёртки с тем же интерфейсом.',
    aka: 'Wrapper / Декоратор',
    motivation: [
      'Кофе можно дополнять молоком, сахаром, сиропом в любых сочетаниях. Класс на каждую комбинацию растёт как 2^n и не переиспользуется.',
      'Решение: каждый декоратор реализует тот же интерфейс Beverage и оборачивает вложенный напиток, добавляя к его результату своё. Комбинация собирается обёртками в рантайме.',
    ],
    pros: [
      'Добавление обязанностей без подклассов на каждую комбинацию',
      'Комбинируется в рантайме в любом порядке',
      'Single Responsibility: каждый слой — одна функция',
    ],
    cons: [
      'Много мелких объектов-обёрток',
      'Порядок декораторов может быть важен и неочевиден',
    ],
    useCases: [
      { src: 'JDK', item: 'java.io: BufferedInputStream и пр.' },
      { src: 'JDK', item: 'Collections.unmodifiable*/synchronized*' },
      { src: 'Servlet', item: 'HttpServletRequestWrapper' },
    ],
    related: [
      { name: 'Composite', id: 'composite', kind: 'struct', why: 'тоже рекурсивная композиция' },
      { name: 'Adapter', id: 'adapter', kind: 'struct', why: 'оборачивает, но меняет интерфейс' },
      { name: 'Proxy', id: 'proxy', kind: 'struct', why: 'тоже обёртка с тем же интерфейсом' },
    ],
    code: { good: DECORATOR_GOOD, bad: DECORATOR_BAD },
    viz: 'decorator',
    live: 'decorator',
    backend: '/patterns/structural/decorator',
    preview: {
      instances: [
        { hash: 'Espresso', createdBy: '80' },
        { hash: '+ Milk', createdBy: '+20' },
        { hash: '+ Sugar', createdBy: '+10' },
      ],
      frame: { actor: '+ Sugar', action: 'wrap', result: 'cost = 110', ok: true, reveal: 3 },
    },
  },
  facade: {
    name: 'Facade',
    ru: 'Фасад',
    intent: 'Предоставляет единый упрощённый интерфейс к набору интерфейсов сложной подсистемы.',
    aka: 'Фасад',
    motivation: [
      'Оформление заказа задействует склад, оплату и доставку. Если клиент вызывает их сам, он связан с тремя подсистемами и обязан знать правильный порядок шагов.',
      'Решение: фасад OrderFacade.placeOrder() прячет подсистемы и оркеструет их внутри. Клиент делает один вызов и развязан с внутренним устройством.',
    ],
    pros: [
      'Простой вход вместо знания всей подсистемы',
      'Развязывает клиента и внутренние классы',
      'Порядок и детали скрыты в одном месте',
    ],
    cons: [
      'Фасад может разрастись в «божественный объект»',
      'Скрывает возможности подсистемы, иногда нужен прямой доступ',
    ],
    useCases: [
      { src: 'SLF4J', item: 'LoggerFactory как фасад' },
      { src: 'Spring', item: 'JdbcTemplate над JDBC' },
      { src: 'JDK', item: 'javax.faces.context.FacesContext' },
    ],
    related: [
      { name: 'Adapter', id: 'adapter', kind: 'struct', why: 'адаптер меняет интерфейс, фасад — упрощает' },
      { name: 'Mediator', id: 'mediator', kind: 'behav', why: 'тоже централизует взаимодействие' },
      { name: 'Singleton', id: 'singleton', kind: 'creat', why: 'фасад часто один на приложение' },
    ],
    code: { good: FACADE_GOOD, bad: FACADE_BAD },
    viz: 'facade',
    live: 'facade',
    backend: '/patterns/structural/facade',
    preview: {
      instances: [
        { hash: 'OrderFacade', createdBy: 'facade' },
        { hash: 'Inventory', createdBy: 'subsystem' },
        { hash: 'Payment', createdBy: 'subsystem' },
        { hash: 'Shipping', createdBy: 'subsystem' },
      ],
      frame: { actor: 'Shipping', action: 'ship()', result: 'shipped SKU-42', ok: true },
    },
  },
  flyweight: {
    name: 'Flyweight',
    ru: 'Приспособленец',
    intent: 'Экономит память, разделяя общее (intrinsic) состояние множества мелких объектов через общий пул.',
    aka: 'Приспособленец',
    motivation: [
      'Лес из тысяч деревьев: у каждого свои координаты, но вид (название, цвет, текстура) повторяется. Хранить тяжёлые данные типа в каждом дереве — линейный расход памяти.',
      'Решение: вынести общее состояние в объект-flyweight (TreeType), который раздаёт фабрика-пул. Деревья хранят лишь координаты и ссылку на общий тип.',
    ],
    pros: [
      'Резкая экономия памяти при множестве похожих объектов',
      'Общее состояние переиспользуется через пул',
    ],
    cons: [
      'Усложняет код разделением intrinsic/extrinsic состояния',
      'Экономия памяти ценой процессорного времени/контекста',
    ],
    useCases: [
      { src: 'JDK', item: 'Integer.valueOf() — кэш малых значений' },
      { src: 'JDK', item: 'String pool (интернирование)' },
      { src: 'JDK', item: 'Boolean.TRUE / FALSE' },
    ],
    related: [
      { name: 'Singleton', id: 'singleton', kind: 'creat', why: 'пул flyweight часто синглтон' },
      { name: 'Factory Method', id: 'factory-method', kind: 'creat', why: 'фабрика раздаёт flyweight' },
      { name: 'Composite', id: 'composite', kind: 'struct', why: 'flyweight как листья дерева' },
    ],
    code: { good: FLYWEIGHT_GOOD, bad: FLYWEIGHT_BAD },
    viz: 'flyweight',
    live: 'flyweight',
    backend: '/patterns/structural/flyweight',
    preview: {
      instances: [
        { hash: 'T1', createdBy: 'oak/green' },
        { hash: 'T2', createdBy: 'oak/green' },
        { hash: 'T3', createdBy: 'pine/dark' },
        { hash: 'T4', createdBy: 'oak/green' },
        { hash: 'T5', createdBy: 'pine/dark' },
      ],
      frame: { actor: 'T3', action: 'factory.get(pine)', result: 'new flyweight #2', ok: true },
    },
  },
  proxy: {
    name: 'Proxy',
    ru: 'Заместитель',
    intent: 'Подставляет вместо объекта заместителя с тем же интерфейсом, чтобы контролировать доступ к нему.',
    aka: 'Surrogate / Заместитель',
    motivation: [
      'Реальный сервис дорогой: каждый запрос — тяжёлая операция. Дёргать его напрямую при повторах — переплачивать за одни и те же данные.',
      'Решение: заместитель реализует тот же интерфейс и добавляет кэш (или ленивую загрузку, контроль доступа, удалённый вызов). Повтор отдаётся из кэша; клиент не знает, что перед ним прокси.',
    ],
    pros: [
      'Кэш / ленивость / контроль доступа без изменения клиента',
      'Тот же интерфейс — прозрачная подмена',
      'Управляет жизненным циклом дорогого объекта',
    ],
    cons: [
      'Ещё один слой косвенности и задержка',
      'Усложняет код при простых сценариях',
    ],
    useCases: [
      { src: 'JDK', item: 'java.lang.reflect.Proxy' },
      { src: 'Spring', item: 'AOP-прокси · @Transactional' },
      { src: 'Hibernate', item: 'ленивые прокси сущностей' },
    ],
    related: [
      { name: 'Decorator', id: 'decorator', kind: 'struct', why: 'тоже обёртка с тем же интерфейсом' },
      { name: 'Adapter', id: 'adapter', kind: 'struct', why: 'обёртка, но меняет интерфейс' },
      { name: 'Facade', id: 'facade', kind: 'struct', why: 'упрощает, прокси — контролирует' },
    ],
    code: { good: PROXY_GOOD, bad: PROXY_BAD },
    viz: 'proxy',
    live: 'proxy',
    backend: '/patterns/structural/proxy',
    preview: {
      instances: [
        { hash: 'Client', createdBy: 'вызывает' },
        { hash: 'CachingProxy', createdBy: 'proxy + cache' },
        { hash: 'RealService', createdBy: 'дорогой' },
      ],
      frame: { actor: 'CachingProxy', action: 'fetch(1)', result: 'HIT → data#1 (из кэша)', ok: true },
    },
  },
  command: {
    name: 'Command',
    ru: 'Команда',
    intent: 'Превращает запрос в объект, позволяя ставить запросы в очередь, логировать их и отменять.',
    aka: 'Action / Transaction · Команда',
    motivation: [
      'Редактору нужны undo/redo, очередь задач, макросы. Если действие — это разовый вызов метода, отменить или повторить его нечем: нет объекта, который знал бы, что и как откатить.',
      'Решение: упаковать действие в объект-команду с execute() и undo(). Invoker хранит историю и откатывает последнюю команду — запрос стал данными.',
    ],
    pros: [
      'undo/redo, очередь, лог и макросы из коробки',
      'Развязывает инициатора и исполнителя',
      'Команды комбинируются и переиспользуются',
    ],
    cons: [
      'Много мелких классов-команд',
      'Хранение истории расходует память',
    ],
    useCases: [
      { src: 'Swing', item: 'Action / AbstractAction' },
      { src: 'JDK', item: 'Runnable как команда для пула потоков' },
      { src: 'Spring', item: 'JdbcTemplate / TransactionCallback' },
    ],
    related: [
      { name: 'Memento', id: 'memento', kind: 'behav', why: 'хранит состояние для undo' },
      { name: 'Chain of Responsibility', id: 'chain-of-resp', kind: 'behav', why: 'запрос как объект, передаётся по цепи' },
      { name: 'Observer', id: 'observer', kind: 'behav', why: 'событие как объект' },
    ],
    code: { good: COMMAND_GOOD, bad: COMMAND_BAD },
    viz: 'command',
    live: 'command',
    backend: '/patterns/behavioral/command',
    preview: {
      instances: [
        { hash: 'append("Hello ")', createdBy: 'cmd #1' },
        { hash: 'append("World")', createdBy: 'cmd #2' },
      ],
      frame: { actor: 'append("World")', action: 'undo()', result: 'text="Hello "', ok: true },
    },
  },
  iterator: {
    name: 'Iterator',
    ru: 'Итератор',
    intent: 'Даёт способ последовательно обходить элементы коллекции, не раскрывая её внутреннее устройство.',
    aka: 'Cursor · Итератор',
    motivation: [
      'Клиент, который ходит по массиву через индексы, намертво завязан на структуру. Сменили массив на список или дерево — переписывать все циклы, плюс легко словить off-by-one.',
      'Решение: коллекция отдаёт итератор с hasNext()/next() и сама прячет позицию обхода. Клиент перебирает элементы единообразно, не зная внутренностей.',
    ],
    pros: [
      'Обход без раскрытия структуры коллекции',
      'Единый интерфейс перебора для разных хранилищ',
      'Несколько независимых обходов одновременно',
    ],
    cons: [
      'Для простых коллекций — лишняя абстракция',
      'Итератор может отстать от изменений коллекции',
    ],
    useCases: [
      { src: 'JDK', item: 'java.util.Iterator / Iterable' },
      { src: 'JDK', item: 'for-each по любой коллекции' },
      { src: 'JDK', item: 'Stream / Spliterator' },
    ],
    related: [
      { name: 'Composite', id: 'composite', kind: 'struct', why: 'итератор обходит дерево Composite' },
      { name: 'Factory Method', id: 'factory-method', kind: 'creat', why: 'iterator() — фабричный метод' },
      { name: 'Visitor', id: 'visitor', kind: 'behav', why: 'другой способ обхода структуры' },
    ],
    code: { good: ITERATOR_GOOD, bad: ITERATOR_BAD },
    viz: 'iterator',
    live: 'iterator',
    backend: '/patterns/behavioral/iterator',
    preview: {
      instances: [
        { hash: 'alpha', createdBy: 'element' },
        { hash: 'beta', createdBy: 'element' },
        { hash: 'gamma', createdBy: 'element' },
      ],
      frame: { actor: 'beta', action: 'next()', result: '→ beta', ok: true, idx: 1 },
    },
  },
  mediator: {
    name: 'Mediator',
    ru: 'Посредник',
    intent: 'Инкапсулирует взаимодействие набора объектов в отдельном посреднике, устраняя прямые связи между ними.',
    aka: 'Посредник',
    motivation: [
      'Когда участники общаются напрямую, каждый хранит ссылки на всех остальных. Связи образуют паутину: N участников — до N×(N−1) связей, любое изменение задевает многих.',
      'Решение: ввести посредника (ChatRoom), через которого идёт вся коммуникация. Каждый знает только посредника — связей становится линейно, добавить участника просто.',
    ],
    pros: [
      'Убирает связи «каждый с каждым»',
      'Логика взаимодействия собрана в одном месте',
      'Участники переиспользуются независимо',
    ],
    cons: [
      'Посредник может разрастись в «божественный объект»',
      'Централизация — единая точка сложности',
    ],
    useCases: [
      { src: 'Spring', item: 'ApplicationEventMulticaster' },
      { src: 'JMS', item: 'брокер сообщений как посредник' },
      { src: 'UI', item: 'контроллер диалога координирует виджеты' },
    ],
    related: [
      { name: 'Observer', id: 'observer', kind: 'behav', why: 'посредник часто рассылает события' },
      { name: 'Facade', id: 'facade', kind: 'struct', why: 'тоже централизует, но однонаправленно' },
      { name: 'Command', id: 'command', kind: 'behav', why: 'запросы между коллегами как объекты' },
    ],
    code: { good: MEDIATOR_GOOD, bad: MEDIATOR_BAD },
    viz: 'mediator',
    live: 'mediator',
    backend: '/patterns/behavioral/mediator',
    preview: {
      instances: [
        { hash: 'Alice', createdBy: 'participant' },
        { hash: 'Bob', createdBy: 'participant' },
        { hash: 'Carol', createdBy: 'participant' },
      ],
      frame: { actor: 'Bob', action: 'ChatRoom → Bob', result: 'доставлено: hi', ok: true },
    },
  },
  memento: {
    name: 'Memento',
    ru: 'Снимок',
    intent: 'Фиксирует и сохраняет внутреннее состояние объекта, не нарушая инкапсуляцию, чтобы позже восстановить его.',
    aka: 'Token · Снимок',
    motivation: [
      'Нужен undo: вернуть объект к прошлому состоянию. Если хранить «снимок» как ссылку на изменяемое поле, он протечёт — изменится вместе с оригиналом и ничего не восстановит.',
      'Решение: объект сам отдаёт непрозрачный снимок с копией состояния, а смотритель (History) хранит снимки, не зная их внутренностей. restore возвращает точное прошлое.',
    ],
    pros: [
      'Откат состояния без нарушения инкапсуляции',
      'Смотритель не зависит от внутренностей снимка',
      'Простой фундамент для undo/redo',
    ],
    cons: [
      'Снимки тяжёлого состояния расходуют память',
      'Частые снимки бьют по производительности',
    ],
    useCases: [
      { src: 'JDK', item: 'java.io.Serializable как снимок' },
      { src: 'БД', item: 'savepoint / откат транзакции' },
      { src: 'Редакторы', item: 'история undo/redo' },
    ],
    related: [
      { name: 'Command', id: 'command', kind: 'behav', why: 'команда хранит снимок для undo' },
      { name: 'Prototype', id: 'prototype', kind: 'creat', why: 'тоже о копировании состояния' },
      { name: 'State', id: 'state', kind: 'behav', why: 'снимок может хранить состояние FSM' },
    ],
    code: { good: MEMENTO_GOOD, bad: MEMENTO_BAD },
    viz: 'memento',
    live: 'memento',
    backend: '/patterns/behavioral/memento',
    preview: {
      instances: [
        { hash: 'S1', createdBy: '"A"' },
        { hash: 'S2', createdBy: '"AB"' },
      ],
      frame: { actor: 'S2', action: 'restore(S2)', result: 'content="AB"', ok: true },
    },
  },
  state: {
    name: 'State',
    ru: 'Состояние',
    intent: 'Позволяет объекту менять поведение при изменении внутреннего состояния так, будто меняется его класс.',
    aka: 'Objects for States · Состояние',
    motivation: [
      'Поведение зависит от состояния (Draft → Review → Published). Гигантский switch(state) в каждом методе превращает добавление состояния в правку всех switch-ей с риском сломать переходы.',
      'Решение: каждое состояние — отдельный класс, знающий свои переходы. Контекст делегирует действие текущему состоянию; невозможные переходы просто не описаны.',
    ],
    pros: [
      'Переходы и поведение собраны по состояниям',
      'Open/Closed: новое состояние = новый класс',
      'Убирает разросшиеся switch(state)',
    ],
    cons: [
      'Много классов при простой FSM',
      'Логику переходов нужно где-то согласовать',
    ],
    useCases: [
      { src: 'TCP', item: 'состояния соединения' },
      { src: 'Workflow', item: 'статусы документа/заказа' },
      { src: 'UI', item: 'режимы инструмента (рисование/выбор)' },
    ],
    related: [
      { name: 'Strategy', id: 'strategy', kind: 'behav', why: 'та же структура, но State знает переходы' },
      { name: 'Singleton', id: 'singleton', kind: 'creat', why: 'состояния без полей часто синглтоны' },
      { name: 'Memento', id: 'memento', kind: 'behav', why: 'снимок текущего состояния' },
    ],
    code: { good: STATE_GOOD, bad: STATE_BAD },
    viz: 'state',
    live: 'state',
    backend: '/patterns/behavioral/state',
    preview: {
      instances: [
        { hash: 'Draft', createdBy: 'state' },
        { hash: 'Review', createdBy: 'state' },
        { hash: 'Published', createdBy: 'state' },
      ],
      frame: { actor: 'Review', action: 'publish() [Draft]', result: 'Draft → Review', ok: true },
    },
  },
  strategy: {
    name: 'Strategy',
    ru: 'Стратегия',
    intent: 'Определяет семейство взаимозаменяемых алгоритмов, инкапсулирует каждый и делает их подменяемыми в рантайме.',
    aka: 'Policy · Стратегия',
    motivation: [
      'Расчёт скидки бывает разный (нет скидки, процент, купон). Зашитый в один метод switch(type) растёт ветками, дублируется по коду и плохо тестируется в изоляции.',
      'Решение: вынести каждый алгоритм в отдельную стратегию за общим интерфейсом. Контекст хранит стратегию и подменяет её в рантайме; новый алгоритм = новый класс.',
    ],
    pros: [
      'Алгоритмы взаимозаменяемы в рантайме',
      'Каждый алгоритм изолирован и тестируем',
      'Убирает ветвления выбора алгоритма',
    ],
    cons: [
      'Больше объектов/классов',
      'Клиент должен знать, какую стратегию выбрать',
    ],
    useCases: [
      { src: 'JDK', item: 'Comparator для сортировки' },
      { src: 'JDK', item: 'ThreadPoolExecutor RejectedExecutionHandler' },
      { src: 'Spring', item: 'PasswordEncoder / выбор реализации' },
    ],
    related: [
      { name: 'State', id: 'state', kind: 'behav', why: 'та же структура, но без знания переходов' },
      { name: 'Template Method', id: 'template-method', kind: 'behav', why: 'варьирование шага через наследование vs композицию' },
      { name: 'Factory Method', id: 'factory-method', kind: 'creat', why: 'часто создаёт нужную стратегию' },
    ],
    code: { good: STRATEGY_GOOD, bad: STRATEGY_BAD },
    viz: 'strategy',
    live: 'strategy',
    backend: '/patterns/behavioral/strategy',
    preview: {
      instances: [
        { hash: 'NoDiscount', createdBy: 'strategy' },
        { hash: 'Percent(10%)', createdBy: 'strategy' },
        { hash: 'Coupon(-150)', createdBy: 'strategy' },
      ],
      frame: { actor: 'Percent(10%)', action: 'checkout(1000)', result: '= 900', ok: true },
    },
  },
  'template-method': {
    name: 'Template Method',
    ru: 'Шаблонный метод',
    intent: 'Задаёт скелет алгоритма в методе базового класса, отдавая отдельные шаги на переопределение подклассам.',
    aka: 'Шаблонный метод',
    motivation: [
      'Похожие алгоритмы отличаются парой шагов. Если копировать весь алгоритм в каждый подкласс, шаги дублируются и расходятся — одна из копий легко теряет важный шаг.',
      'Решение: вынести неизменный скелет в final-метод базового класса, оставив подклассам только хуки. Общая последовательность фиксирована в одном месте.',
    ],
    pros: [
      'Скелет алгоритма не дублируется',
      'Подклассы меняют только нужные шаги',
      'Инвариант последовательности зафиксирован',
    ],
    cons: [
      'Жёсткая структура — ограничен наследованием',
      'Много хуков усложняют понимание',
    ],
    useCases: [
      { src: 'JDK', item: 'AbstractList / AbstractMap' },
      { src: 'Servlet', item: 'HttpServlet.service() → doGet/doPost' },
      { src: 'Spring', item: '*Template (Jdbc/Rest) — скелет операции' },
    ],
    related: [
      { name: 'Strategy', id: 'strategy', kind: 'behav', why: 'варьирует шаг композицией, а не наследованием' },
      { name: 'Factory Method', id: 'factory-method', kind: 'creat', why: 'фабричный метод — частный хук' },
    ],
    code: { good: TEMPLATE_GOOD, bad: TEMPLATE_BAD },
    viz: 'template-method',
    live: 'template-method',
    backend: '/patterns/behavioral/template-method',
    preview: {
      instances: [
        { hash: 'boil water', createdBy: 'fixed' },
        { hash: 'steep tea', createdBy: 'hook' },
        { hash: 'pour into cup', createdBy: 'fixed' },
        { hash: 'add lemon', createdBy: 'hook' },
      ],
      frame: { actor: 'steep tea', action: 'hook (подкласс)', result: 'шаг 2', ok: true, reveal: 4 },
    },
  },
  visitor: {
    name: 'Visitor',
    ru: 'Посетитель',
    intent: 'Выносит операцию из иерархии объектов в отдельный посетитель, позволяя добавлять операции без изменения классов.',
    aka: 'Посетитель',
    motivation: [
      'Нужно много операций над иерархией фигур (площадь, рендер, экспорт). Лестница instanceof для каждой операции дублируется и ломается при добавлении фигуры.',
      'Решение: элементы реализуют accept(visitor) и через двойную диспетчеризацию выбирают нужный метод посетителя. Новая операция = новый Visitor, классы фигур не трогаются.',
    ],
    pros: [
      'Новая операция без правки классов элементов',
      'Родственные операции собраны в одном посетителе',
      'Убирает лестницы instanceof',
    ],
    cons: [
      'Добавить тип элемента — править всех посетителей',
      'Посетителю нужен доступ к внутренностям элементов',
    ],
    useCases: [
      { src: 'JDK', item: 'java.nio.file.FileVisitor' },
      { src: 'Annotation', item: 'ElementVisitor (javax.lang.model)' },
      { src: 'Compiler', item: 'обход AST посетителями' },
    ],
    related: [
      { name: 'Composite', id: 'composite', kind: 'struct', why: 'посетитель обходит дерево Composite' },
      { name: 'Iterator', id: 'iterator', kind: 'behav', why: 'другой способ обхода элементов' },
      { name: 'Interpreter', id: 'interpreter', kind: 'behav', why: 'операции над узлами AST' },
    ],
    code: { good: VISITOR_GOOD, bad: VISITOR_BAD },
    viz: 'visitor',
    live: 'visitor',
    backend: '/patterns/behavioral/visitor',
    preview: {
      instances: [
        { hash: 'Circle(r=2)', createdBy: 'element' },
        { hash: 'Square(side=3)', createdBy: 'element' },
      ],
      frame: { actor: 'Circle(r=2)', action: 'accept(AreaVisitor)', result: 'area = 13', ok: true },
    },
  },
  interpreter: {
    name: 'Interpreter',
    ru: 'Интерпретатор',
    intent: 'Задаёт грамматику языка в виде классов и интерпретирует предложения языка, обходя дерево разбора.',
    aka: 'Интерпретатор',
    motivation: [
      'Простой язык выражений (5 + 3 - 2) можно разобрать «на коленке» split-ом, но такой парсер не знает приоритетов и скобок и не расширяется без переписывания.',
      'Решение: каждое правило грамматики — класс выражения с interpret(). Из них строится дерево (AST), которое вычисляется рекурсией; новое правило = новый класс.',
    ],
    pros: [
      'Грамматика выражена классами, легко расширять',
      'AST задаёт приоритеты и вложенность структурой',
      'Правила переиспользуются и комбинируются',
    ],
    cons: [
      'Для сложных грамматик — взрыв классов',
      'Медленнее специализированных парсеров',
    ],
    useCases: [
      { src: 'JDK', item: 'java.util.regex.Pattern' },
      { src: 'Spring', item: 'SpEL — Spring Expression Language' },
      { src: 'JDK', item: 'java.text.MessageFormat' },
    ],
    related: [
      { name: 'Composite', id: 'composite', kind: 'struct', why: 'AST — это дерево Composite' },
      { name: 'Visitor', id: 'visitor', kind: 'behav', why: 'операции над узлами AST' },
      { name: 'Iterator', id: 'iterator', kind: 'behav', why: 'обход дерева разбора' },
    ],
    code: { good: INTERPRETER_GOOD, bad: INTERPRETER_BAD },
    viz: 'interpreter',
    live: 'interpreter',
    backend: '/patterns/behavioral/interpreter',
    preview: {
      instances: [
        { hash: 'Num(5)', createdBy: '5' },
        { hash: 'Num(3)', createdBy: '3' },
        { hash: 'Add', createdBy: '5 + 3 = 8' },
        { hash: 'Num(2)', createdBy: '2' },
        { hash: 'Sub', createdBy: '8 - 2 = 6' },
      ],
      frame: { actor: 'Add', action: '5 + 3', result: '= 8', ok: true },
    },
  },
  observer: {
    intent: 'Определяет зависимость один-ко-многим: при изменении состояния субъекта все подписанные наблюдатели уведомляются автоматически.',
    aka: 'Наблюдатель / Publish-Subscribe',
    motivation: [
      'Представь UI с виджетами: график, таблица, метрика. Все рендерят один и тот же стрим данных. Прямые ссылки от data-source к каждому виджету связывают сильно — меняешь виджет, правишь источник.',
      'Решение: источник знает только про абстрактный Observer и держит список подписчиков. Любой компонент может присоединиться или отвалиться без изменений в источнике.',
    ],
    pros: [
      'Слабая связанность Subject и Observer',
      'Динамическое подписание / отписка в рантайме',
      'Открыт для расширения — новые подписчики без правки субъекта',
    ],
    cons: [
      'Неочевидный порядок уведомления подписчиков',
      'Утечки памяти при незакрытых подписках',
      'Каскад уведомлений → лавина обновлений',
    ],
    useCases: [
      { src: 'JDK', item: 'java.util.Observable / Observer (deprecated)' },
      { src: 'JDK', item: 'PropertyChangeListener / Swing event model' },
      { src: 'Reactor', item: 'Publisher<T> / Subscriber<T>' },
      { src: 'RxJava', item: 'Observable.subscribe()' },
      { src: 'Spring', item: 'ApplicationEventPublisher' },
    ],
    related: [
      { name: 'Mediator', id: 'mediator', kind: 'behav', why: 'тоже централизует коммуникацию' },
      { name: 'Command', id: 'command', kind: 'behav', why: 'событие как объект' },
      { name: 'Singleton', id: 'singleton', kind: 'creat', why: 'часто Subject — синглтон' },
    ],
    code: { good: OBSERVER_GOOD, bad: OBSERVER_BAD },
    viz: 'observer',
    live: 'observer',
    backend: '/patterns/behavioral/observer',
  },
  singleton: {
    intent: 'Гарантирует единственный экземпляр класса на JVM и предоставляет глобальную точку доступа к нему.',
    aka: 'Одиночка',
    motivation: [
      'Конфиг, реестр сервисов, пул соединений, логгер — это объекты, которых по смыслу должно быть ровно по одному. Создавать их повсюду через new опасно: рассинхрон состояния, лишние ресурсы.',
      'Singleton прячет конструктор и отдаёт единственный экземпляр через статический accessor. Главная ловушка — потокобезопасность ленивой инициализации.',
    ],
    pros: [
      'Контролируемый единственный доступ к экземпляру',
      'Ленивая инициализация по требованию',
      'Экономия ресурсов на общих объектах',
    ],
    cons: [
      'Глобальное состояние усложняет юнит-тесты',
      'Скрытые зависимости вместо явных',
      'Наивная реализация ломается в многопоточке',
    ],
    useCases: [
      { src: 'JDK', item: 'Runtime.getRuntime()' },
      { src: 'Spring', item: '@Bean singleton scope (по умолчанию)' },
      { src: 'JDK', item: 'java.util.logging.Logger' },
    ],
    related: [
      { name: 'Factory Method', id: 'factory-method', kind: 'creat', why: 'часто отдаёт синглтон' },
      { name: 'Observer', id: 'observer', kind: 'behav', why: 'Subject нередко синглтон' },
    ],
    code: { good: SINGLETON_GOOD, bad: SINGLETON_BAD },
    viz: 'singleton',
    live: 'singleton',
    backend: '/patterns/creational/singleton',
  },
  'chain-of-resp': {
    name: 'Chain of Responsibility',
    ru: 'Цепочка обязанностей',
    intent: 'Передаёт запрос по цепочке обработчиков: каждое звено решает — обработать его само или передать дальше, не зная, кто следующий.',
    aka: 'CoR / Цепочка обязанностей',
    motivation: [
      'Согласование расхода: суммы до $1 000 утверждает тимлид, до $5 000 — менеджер, до $20 000 — директор, выше — CEO. Зашить эту лестницу в один if-else значит намертво связать отправителя со всеми уровнями и их порядком.',
      'Решение: каждый уровень — отдельное звено, знающее только свой лимит и ссылку на следующего. Запрос идёт по цепочке, пока кто-то не возьмёт его на себя. Порядок и состав цепи настраиваются в рантайме.',
    ],
    pros: [
      'Слабая связанность отправителя и получателей',
      'Single Responsibility: каждое звено — один вид проверки',
      'Open/Closed: новый уровень = новое звено без правки старых',
      'Порядок и состав цепочки настраиваются в рантайме',
    ],
    cons: [
      'Запрос может дойти до конца необработанным',
      'Сложнее отследить, кто именно обработал запрос',
      'Длинная цепочка бьёт по производительности',
    ],
    useCases: [
      { src: 'Servlet', item: 'javax.servlet.Filter / FilterChain' },
      { src: 'Spring', item: 'Security FilterChain · HandlerInterceptor' },
      { src: 'Netty', item: 'ChannelPipeline / ChannelHandler' },
      { src: 'JDK', item: 'java.util.logging — родительские Logger' },
    ],
    related: [
      { name: 'Decorator', id: 'decorator', kind: 'struct', why: 'тоже оборачивает, но выполняет всегда' },
      { name: 'Command', id: 'command', kind: 'behav', why: 'запрос как объект' },
      { name: 'Composite', id: 'composite', kind: 'struct', why: 'часто образует дерево обработчиков' },
    ],
    code: { good: CHAIN_GOOD, bad: CHAIN_BAD },
    viz: 'chain',
    live: 'chain',
    backend: '/patterns/behavioral/chain-of-responsibility',
    previewHandlers: [
      { name: 'TeamLead', limit: '≤ $1 000' },
      { name: 'Manager', limit: '≤ $5 000' },
      { name: 'Director', limit: '≤ $20 000' },
      { name: 'CEO', limit: '∞' },
    ],
  },
}

export const getDetail = (patternId) => DETAILS[patternId] || null
