import { useState } from 'react'
import { api } from '../api/patterns.js'
import CodeBlock from '../components/CodeBlock.jsx'
import { StepTimeline, InstanceGrid, Verdict } from '../components/Visualization.jsx'

export default function SingletonPage() {
  // Одно окно результата: новый запуск заменяет предыдущий, а не добавляет ниже.
  const [result, setResult] = useState(null)   // { data, variant } | null
  const [loading, setLoading] = useState(null) // 'good' | 'bad' | null
  const [error, setError] = useState(null)

  const run = async (variant) => {
    setError(null); setLoading(variant); setResult(null)
    try {
      const data = variant === 'good' ? await api.singletonGood() : await api.singletonBad()
      setResult({ data, variant })
    } catch (e) {
      setError(e.message)
    } finally {
      setLoading(null)
    }
  }

  return (
    <div>
      <h1><span className="sprite-singleton" /> SINGLETON</h1>

      <div className="pixel-box">
        <h3>Что это</h3>
        <p>
          Порождающий паттерн: гарантирует, что у класса есть <b>ровно один</b> экземпляр
          и предоставляет глобальную точку доступа к нему. Типовое применение —
          конфиг, реестр сервисов, пул соединений, логгер.
        </p>
        <h3 style={{marginTop: 14}}>На что смотреть</h3>
        <p>
          В обоих демо запускаются 4 потока одновременно. Если паттерн реализован верно —
          все вернут <i>один и тот же</i> hash объекта. Если сломан — мы увидим
          несколько разных коробок в памяти.
        </p>
      </div>

      <div>
        <button className="btn good" disabled={loading !== null} onClick={() => run('good')}>
          {loading === 'good' ? 'RUNNING…' : '▶ Запустить хороший'}
        </button>
        <button className="btn bad" disabled={loading !== null} onClick={() => run('bad')}>
          {loading === 'bad' ? 'RUNNING…' : '▶ Запустить плохой'}
        </button>
      </div>

      {error && (
        <div className="pixel-box bad" style={{marginTop: 12}}>
          <h3>Ошибка</h3>
          <p>{error}</p>
          <p style={{color: 'var(--muted)'}}>
            Проверь, что бэкенд поднят на <code>http://localhost:8080</code>.
          </p>
        </div>
      )}

      {result && <ResultPanel result={result.data} variant={result.variant} />}
    </div>
  )
}

function ResultPanel({ result, variant }) {
  if (!result) return null
  const isBad = variant === 'bad'
  return (
    <div className={'pixel-box ' + (isBad ? 'bad' : 'good')} style={{marginTop: 18}}>
      <h2>{result.title}</h2>
      <p>{result.description}</p>

      <h3>Исходник</h3>
      <CodeBlock code={result.code} />

      <h3 style={{marginTop: 16}}>Шаги выполнения</h3>
      <StepTimeline steps={result.steps} bad={isBad} />

      <h3>Объекты в памяти</h3>
      <InstanceGrid instances={result.instances} bad={isBad} />

      <Verdict verdict={result.verdict} />

      <h3>Что произошло</h3>
      <p>{result.explanation}</p>
    </div>
  )
}
