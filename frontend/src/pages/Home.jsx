import { Link } from 'react-router-dom'

export default function Home() {
  return (
    <div>
      <div className="hero">
        <h1>JAVA PATTERNS</h1>
        <div className="sub">Пиксельная демо-площадка хороших и плохих реализаций паттернов</div>
      </div>

      <div className="cards">
        <div className="pixel-box card accent">
          <h2>Паттерны проектирования</h2>
          <ul>
            <li>· Поведенческие — Observer, Strategy, Command…</li>
            <li>· Структурные — Adapter, Decorator, Facade…</li>
            <li>· Порождающие — <Link to="/design/creational/singleton">Singleton</Link>, Factory, Builder…</li>
          </ul>
          <p style={{marginTop: 12}}>
            <Link className="btn" to="/design/creational">Открыть раздел</Link>
          </p>
        </div>

        <div className="pixel-box card accent">
          <h2>Микросервисы</h2>
          <ul>
            <li>· API Gateway</li>
            <li>· Circuit Breaker</li>
            <li>· Saga</li>
          </ul>
          <p style={{marginTop: 12}}>
            <Link className="btn" to="/microservices">Открыть раздел</Link>
          </p>
        </div>
      </div>

      <div className="pixel-box" style={{marginTop: 24}}>
        <h3>Как это устроено</h3>
        <p>
          Каждая страница паттерна вызывает Java-бэкенд (Spring Boot) на <code>/api/patterns/...</code>.
          Бэк запускает <b>хороший</b> и <b>плохой</b> вариант реализации в реальной JVM,
          собирает шаги выполнения и возвращает JSON. Фронт визуализирует шаги
          в пиксельном стиле — видно, где паттерн ломается и почему.
        </p>
      </div>
    </div>
  )
}
