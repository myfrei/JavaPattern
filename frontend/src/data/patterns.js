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
    tag: 'Distributed · 30+',
    desc: 'Decomp · Comm · Data · Resilience',
    icon: 'micro',
    accent: 'var(--accent-2)',
    preview: ['API Gateway', 'Saga', 'CQRS', 'Sidecar', '+26'],
    to: '/patterns/microservices',
  },
]

export const CATEGORIES = [
  { id: 'creational', kind: 'creat', label: 'Creational', ru: 'Порождающие', count: 5, icon: 'spark',
    desc: 'Управляют созданием объектов — изоляция конструкторов, контроль семейств.' },
  { id: 'structural', kind: 'struct', label: 'Structural', ru: 'Структурные', count: 7, icon: 'column',
    desc: 'Композиция классов и объектов — адаптеры, оболочки, общие интерфейсы.' },
  { id: 'behavioral', kind: 'behav', label: 'Behavioral', ru: 'Поведенческие', count: 11, icon: 'arrows',
    desc: 'Алгоритмы и распределение ответственности между объектами.' },
]

// number is assigned sequentially per category below.
const RAW = {
  creational: [
    { name: 'Singleton',        ru: 'Одиночка',            diff: 'easy',   pop: 5, tags: ['global', 'lazy-init'], status: 'in-progress', live: 'singleton' },
    { name: 'Factory Method',   ru: 'Фабричный метод',     diff: 'medium', pop: 5, tags: ['create', 'subclass'], status: '' },
    { name: 'Abstract Factory', ru: 'Абстрактная фабрика', diff: 'hard',   pop: 4, tags: ['family'], status: '' },
    { name: 'Builder',          ru: 'Строитель',           diff: 'easy',   pop: 4, tags: ['step-by-step'], status: '' },
    { name: 'Prototype',        ru: 'Прототип',            diff: 'medium', pop: 2, tags: ['clone'], status: '' },
  ],
  structural: [
    { name: 'Adapter',   ru: 'Адаптер',         diff: 'easy',   pop: 5, tags: ['compat'], status: '' },
    { name: 'Bridge',    ru: 'Мост',            diff: 'hard',   pop: 3, tags: ['abstraction'], status: '' },
    { name: 'Composite', ru: 'Компоновщик',     diff: 'medium', pop: 4, tags: ['tree'], status: '' },
    { name: 'Decorator', ru: 'Декоратор',       diff: 'medium', pop: 5, tags: ['wrap'], status: '' },
    { name: 'Facade',    ru: 'Фасад',           diff: 'easy',   pop: 5, tags: ['simplify'], status: '' },
    { name: 'Flyweight', ru: 'Приспособленец',  diff: 'hard',   pop: 2, tags: ['memory'], status: '' },
    { name: 'Proxy',     ru: 'Заместитель',     diff: 'medium', pop: 4, tags: ['access'], status: '' },
  ],
  behavioral: [
    { name: 'Chain of Resp.',  ru: 'Цепочка обяз.',    diff: 'medium', pop: 3, tags: ['pipeline'], status: '' },
    { name: 'Command',         ru: 'Команда',          diff: 'medium', pop: 4, tags: ['undo', 'queue'], status: 'in-progress' },
    { name: 'Iterator',        ru: 'Итератор',         diff: 'easy',   pop: 5, tags: ['traversal'], status: 'done' },
    { name: 'Mediator',        ru: 'Посредник',        diff: 'hard',   pop: 3, tags: ['coupling'], status: '' },
    { name: 'Memento',         ru: 'Снимок',           diff: 'medium', pop: 2, tags: ['state', 'undo'], status: '' },
    { name: 'Observer',        ru: 'Наблюдатель',      diff: 'easy',   pop: 5, tags: ['events', 'pub-sub'], status: 'done' },
    { name: 'State',           ru: 'Состояние',        diff: 'medium', pop: 4, tags: ['fsm'], status: '' },
    { name: 'Strategy',        ru: 'Стратегия',        diff: 'easy',   pop: 5, tags: ['algorithm', 'swap'], status: 'done' },
    { name: 'Template Method', ru: 'Шаблонный метод',  diff: 'easy',   pop: 4, tags: ['skeleton'], status: '' },
    { name: 'Visitor',         ru: 'Посетитель',       diff: 'hard',   pop: 2, tags: ['double-disp'], status: '' },
    { name: 'Interpreter',     ru: 'Интерпретатор',    diff: 'hard',   pop: 1, tags: ['grammar'], status: '' },
  ],
}

export const PATTERNS = Object.fromEntries(
  Object.entries(RAW).map(([cat, list]) => [
    cat,
    list.map((p, i) => ({ ...p, id: slug(p.name), category: cat, number: i + 1 })),
  ])
)

export const getCategory = (id) => CATEGORIES.find(c => c.id === id)
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

export const DETAILS = {
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
    demo: { runner: OBSERVER_RUNNER, frames: OBSERVER_FRAMES, stdout: OBSERVER_STDOUT },
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
    previewHandlers: [
      { name: 'TeamLead', limit: '≤ $1 000' },
      { name: 'Manager', limit: '≤ $5 000' },
      { name: 'Director', limit: '≤ $20 000' },
      { name: 'CEO', limit: '∞' },
    ],
  },
}

export const getDetail = (patternId) => DETAILS[patternId] || null
