export default function MicroservicesPage() {
  const patterns = [
    { id: 'api-gateway',     title: 'API Gateway',     hint: 'Единая точка входа для клиентов' },
    { id: 'circuit-breaker', title: 'Circuit Breaker', hint: 'Защита от каскадных сбоев' },
    { id: 'saga',            title: 'Saga',            hint: 'Распределённые транзакции через шаги' },
    { id: 'event-sourcing',  title: 'Event Sourcing',  hint: 'Состояние как лог событий' },
    { id: 'cqrs',            title: 'CQRS',            hint: 'Разделение чтения и записи' },
    { id: 'bulkhead',        title: 'Bulkhead',        hint: 'Изоляция ресурсов между сервисами' },
  ]
  return (
    <div>
      <h1>Паттерны микросервисов</h1>
      <div className="pixel-box">
        <p>
          Раздел в работе. Здесь так же будут демо «хорошо / плохо» с реальным
          Java-бэкендом: мини-кластер сервисов, через который видно, как
          паттерн ведёт себя при сбоях и под нагрузкой.
        </p>
      </div>
      <div className="cards">
        {patterns.map(p => (
          <div key={p.id} className="pixel-box card">
            <h2>{p.title}</h2>
            <p style={{color: 'var(--muted)'}}>{p.hint}</p>
            <span className="btn" style={{opacity: 0.5}}>SOON</span>
          </div>
        ))}
      </div>
    </div>
  )
}
