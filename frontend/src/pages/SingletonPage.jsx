import { useState } from 'react'
import { api } from '../api/patterns.js'
import CodeBlock from '../components/CodeBlock.jsx'
import { StepTimeline, InstanceGrid, Verdict } from '../components/Visualization.jsx'

export default function SingletonPage() {
  const [good, setGood] = useState(null)
  const [bad, setBad] = useState(null)
  const [loadingGood, setLoadingGood] = useState(false)
  const [loadingBad, setLoadingBad] = useState(false)
  const [error, setError] = useState(null)

  const runGood = async () => {
    setError(null); setLoadingGood(true); setGood(null)
    try { setGood(await api.singletonGood()) }
    catch (e) { setError(e.message) }
    finally { setLoadingGood(false) }
  }
  const runBad = async () => {
    setError(null); setLoadingBad(true); setBad(null)
    try { setBad(await api.singletonBad()) }
    catch (e) { setError(e.message) }
    finally { setLoadingBad(false) }
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
        <button className="btn good" disabled={loadingGood} onClick={runGood}>
          {loadingGood ? 'RUNNING…' : '▶ Запустить хороший'}
        </button>
        <button className="btn bad" disabled={loadingBad} onClick={runBad}>
          {loadingBad ? 'RUNNING…' : '▶ Запустить плохой'}
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

      <ResultPanel result={good} variant="good" />
      <ResultPanel result={bad}  variant="bad" />
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
