import { useEffect, useState } from 'react'

/**
 * Анимированная лента шагов: проигрывает их с задержкой,
 * чтобы было видно «в какой момент потоки столкнулись».
 */
export function StepTimeline({ steps, bad }) {
  const [visible, setVisible] = useState(0)

  useEffect(() => {
    setVisible(0)
    if (!steps || steps.length === 0) return
    let i = 0
    const id = setInterval(() => {
      i += 1
      setVisible(i)
      if (i >= steps.length) clearInterval(id)
    }, 200)
    return () => clearInterval(id)
  }, [steps])

  if (!steps || steps.length === 0) return null

  return (
    <div className="timeline">
      {steps.slice(0, visible).map((s, idx) => (
        <div className={'step ' + (s.ok ? 'ok' : 'fail')} key={idx}>
          <span className="t">{s.t}ms</span>
          <span className="actor">{s.actor}</span>
          <span className="action">{s.action}</span>
          <span className="result">{s.result}</span>
          <span className="dot">{s.ok ? '■' : '✕'}</span>
        </div>
      ))}
    </div>
  )
}

/**
 * Отрисовывает «коробки в памяти» — по одной на уникальный hash.
 * Если коробок больше одной — паттерн сломан.
 */
export function InstanceGrid({ instances, bad }) {
  if (!instances || instances.length === 0) return null
  const broken = bad && instances.length > 1
  return (
    <div className="instances">
      {instances.map((inst, idx) => (
        <div className={'instance ' + (broken ? 'bad' : '')} key={idx}>
          <span className="icon">▣</span>
          <span className="hash">{inst.hash}</span>
          <span className="who">{inst.createdBy}</span>
        </div>
      ))}
    </div>
  )
}

export function Verdict({ verdict }) {
  if (!verdict) return null
  const pass = verdict.startsWith('PASS')
  return (
    <div className={'verdict ' + (pass ? 'pass' : 'fail')}>
      {pass ? '✔ ' : '✕ '}{verdict}
    </div>
  )
}
