import { NavLink } from 'react-router-dom'

/**
 * Жёстко прописанное меню — структура известна заранее, лишний запрос к API
 * для рендера сайдбара не нужен. ready=false помечает «скоро» элементы.
 */
const MENU = [
  {
    id: 'design',
    title: 'Паттерны проектирования',
    groups: [
      {
        id: 'behavioral', title: 'Поведенческие',
        patterns: [
          { id: 'observer', title: 'Observer', ready: false },
          { id: 'strategy', title: 'Strategy', ready: false },
          { id: 'command',  title: 'Command',  ready: false },
        ],
      },
      {
        id: 'structural', title: 'Структурные',
        patterns: [
          { id: 'adapter',   title: 'Adapter',   ready: false },
          { id: 'decorator', title: 'Decorator', ready: false },
          { id: 'facade',    title: 'Facade',    ready: false },
        ],
      },
      {
        id: 'creational', title: 'Порождающие',
        patterns: [
          { id: 'singleton', title: 'Singleton', ready: true },
          { id: 'factory',   title: 'Factory Method', ready: false },
          { id: 'builder',   title: 'Builder', ready: false },
        ],
      },
    ],
  },
  {
    id: 'microservices',
    title: 'Микросервисы',
    groups: [
      {
        id: 'all', title: 'Архитектурные',
        patterns: [
          { id: 'api-gateway',     title: 'API Gateway',     ready: false },
          { id: 'circuit-breaker', title: 'Circuit Breaker', ready: false },
          { id: 'saga',            title: 'Saga',            ready: false },
        ],
      },
    ],
  },
]

export default function Sidebar() {
  return (
    <aside className="sidebar">
      <div className="brand">
        &gt; JAVA<br />
        &nbsp;&nbsp;PATTERNS<span className="blink">_</span>
      </div>

      <NavLink to="/" end className={({isActive}) => 'nav-link' + (isActive ? ' active' : '')}>
        ★ Главная
      </NavLink>

      {MENU.map(section => (
        <div className="nav-section" key={section.id}>
          <div className="nav-title">{section.title}</div>

          {section.id === 'microservices' ? (
            <NavLink
              to="/microservices"
              className={({isActive}) => 'nav-link' + (isActive ? ' active' : '')}
            >
              Обзор раздела
            </NavLink>
          ) : null}

          {section.groups.map(group => (
            <div className="nav-group" key={group.id}>
              <div className="nav-title">{group.title}</div>
              {section.id === 'design' && (
                <NavLink
                  to={`/design/${group.id}`}
                  end
                  className={({isActive}) => 'nav-link' + (isActive ? ' active' : '')}
                >
                  · обзор
                </NavLink>
              )}
              {group.patterns.map(p => (
                p.ready ? (
                  <NavLink
                    key={p.id}
                    to={`/${section.id}/${group.id}/${p.id}`}
                    className={({isActive}) => 'nav-link' + (isActive ? ' active' : '')}
                  >
                    ▸ {p.title}
                  </NavLink>
                ) : (
                  <span key={p.id} className="nav-link soon">▸ {p.title}</span>
                )
              ))}
            </div>
          ))}
        </div>
      ))}
    </aside>
  )
}
