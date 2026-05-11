import { useParams, Link } from 'react-router-dom'

const GROUPS = {
  behavioral: {
    title: 'Поведенческие паттерны',
    intro: 'Описывают взаимодействие объектов и распределение обязанностей между ними.',
    patterns: [
      { id: 'observer', title: 'Observer', ready: false, hint: 'Подписка на события' },
      { id: 'strategy', title: 'Strategy', ready: false, hint: 'Семейство алгоритмов' },
      { id: 'command',  title: 'Command',  ready: false, hint: 'Действие как объект' },
    ],
  },
  structural: {
    title: 'Структурные паттерны',
    intro: 'Описывают, как из классов и объектов собирать более крупные структуры.',
    patterns: [
      { id: 'adapter',   title: 'Adapter',   ready: false, hint: 'Совместимость интерфейсов' },
      { id: 'decorator', title: 'Decorator', ready: false, hint: 'Динамическое расширение' },
      { id: 'facade',    title: 'Facade',    ready: false, hint: 'Упрощённый фасад над подсистемой' },
    ],
  },
  creational: {
    title: 'Порождающие паттерны',
    intro: 'Отвечают за гибкое и контролируемое создание объектов.',
    patterns: [
      { id: 'singleton', title: 'Singleton', ready: true,  hint: 'Один экземпляр на JVM' },
      { id: 'factory',   title: 'Factory Method', ready: false, hint: 'Создание через подкласс' },
      { id: 'builder',   title: 'Builder',  ready: false, hint: 'Пошаговое конструирование' },
    ],
  },
}

export default function GroupPage() {
  const { group } = useParams()
  const data = GROUPS[group]
  if (!data) {
    return <div className="pixel-box bad"><h2>404</h2><p>Раздел не найден.</p></div>
  }
  return (
    <div>
      <h1>{data.title}</h1>
      <div className="pixel-box">
        <p>{data.intro}</p>
      </div>
      <div className="cards">
        {data.patterns.map(p => (
          <div key={p.id} className={'pixel-box card ' + (p.ready ? 'accent' : '')}>
            <h2>{p.title}</h2>
            <p style={{color: 'var(--muted)'}}>{p.hint}</p>
            {p.ready ? (
              <Link className="btn good" to={`/design/${group}/${p.id}`}>Открыть</Link>
            ) : (
              <span className="btn" style={{opacity: 0.5}}>SOON</span>
            )}
          </div>
        ))}
      </div>
    </div>
  )
}
