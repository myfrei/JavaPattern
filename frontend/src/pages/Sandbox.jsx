import { useState, useEffect, useRef, useCallback } from 'react'
import { useParams, useNavigate, Navigate } from 'react-router-dom'
import { AppTopBar, Crumbs, PixelIcon, CAT_COLOR } from '../components/Pixel.jsx'
import { CodeLines } from '../components/CodeView.jsx'
import { renderViz } from '../components/Diagrams.jsx'
import { findPattern, getCategory, getDetail } from '../data/patterns.js'
import { api } from '../api/patterns.js'
import { buildObserverRun, buildFor, errorRun } from '../sandbox/runner.js'

const PLAY_MS = 600

export default function Sandbox() {
  const { section, category, patternId } = useParams()
  const navigate = useNavigate()
  const cat = getCategory(category)
  const pattern = findPattern(category, patternId)
  const detail = getDetail(patternId)

  const isLive = !!detail?.live
  const hasDemo = !!detail?.demo
  const runnable = isLive || hasDemo

  const [flavor, setFlavor] = useState('good')
  const [runState, setRunState] = useState('idle') // idle | running | done | error
  const [result, setResult] = useState(null)
  const [step, setStep] = useState(0)
  const [playing, setPlaying] = useState(false)
  const timer = useRef(null)

  const runNow = useCallback(async (fl) => {
    if (!runnable) return
    setPlaying(false)
    setRunState('running')
    setStep(0)
    if (isLive) {
      try {
        const resp = await api.run(patternId, fl)
        setResult(buildFor(detail.viz, resp, fl))
        setRunState('done')
      } catch (e) {
        setResult(errorRun(e.message, fl, patternId))
        setRunState('error')
      }
    } else {
      setResult(buildObserverRun(fl))
      setRunState('done')
    }
  }, [isLive, patternId, runnable])

  // auto-run on mount / pattern change
  useEffect(() => { setFlavor('good'); runNow('good') /* eslint-disable-next-line */ }, [patternId])

  // playback
  useEffect(() => {
    if (!playing || !result?.frames?.length) return
    timer.current = setInterval(() => {
      setStep(s => {
        if (s >= result.frames.length - 1) { setPlaying(false); return s }
        return s + 1
      })
    }, PLAY_MS)
    return () => clearInterval(timer.current)
  }, [playing, result])

  if (!cat || !pattern) return <Navigate to="/patterns/design" replace />

  const color = CAT_COLOR[category]
  const frames = result?.frames || []
  const frame = frames[step] || null
  const total = frames.length
  const cmd = frame?.label || '—'
  const setFlavorAndRun = (fl) => { setFlavor(fl); runNow(fl) }

  return (
    <div className="screen">
      <AppTopBar active="Sandbox" compact />
      <Crumbs items={[
        { label: pattern.name, to: `/patterns/${section}/${category}/${patternId}` },
        { label: 'Sandbox · Run', active: true },
      ]} />

      {!runnable ? (
        <div className="screen__body center p-24">
          <div className="pix-frame p-24 col gap-12 center-text" style={{ maxWidth: 420 }}>
            <div className="bold" style={{ fontSize: 20 }}>Раннер пока недоступен</div>
            <div className="small text-muted">
              Для «{pattern.name}» ещё нет интерактивного примера. Доступны: Observer (demo), Singleton и Chain of Responsibility (live backend).
            </div>
            <button className="pix-btn pix-btn--primary" style={{ background: color, alignSelf: 'center' }}
                    onClick={() => navigate('/patterns/design/behavioral/observer/sandbox')}>
              Открыть Observer →
            </button>
          </div>
        </div>
      ) : (
        <div className="row f1 sandbox-panes" style={{ overflow: 'hidden' }}>
          {/* LEFT: code */}
          <div className="col sandbox-code" style={{ width: 380, borderRight: 'var(--px) solid var(--line)', background: 'var(--paper)' }}>
            <div className="between p-10" style={{ borderBottom: 'var(--px) solid var(--line)' }}>
              <div className="row gap-6">
                <button className="pix-btn pix-btn--primary" style={{ background: flavor === 'good' ? 'var(--good)' : 'var(--paper)', color: flavor === 'good' ? 'var(--paper)' : 'var(--ink)', padding: '4px 10px', fontSize: 14 }}
                        onClick={() => setFlavorAndRun('good')}>✓ good</button>
                <button className="pix-btn" style={{ background: flavor === 'bad' ? 'var(--bad)' : 'var(--paper)', color: flavor === 'bad' ? 'var(--paper)' : 'var(--ink)', padding: '4px 10px', fontSize: 14 }}
                        onClick={() => setFlavorAndRun('bad')}>✗ bad</button>
              </div>
              <span className="small text-muted">{result?.fileName || '—'}</span>
            </div>
            <div className="f1" style={{ position: 'relative', overflow: 'hidden', display: 'flex' }}>
              <CodeLines code={result?.code || ''} activeLine={frame?.codeLine ?? -1} />
              {runState === 'running' && (
                <div className="center" style={{ position: 'absolute', inset: 0, background: 'rgba(0,0,0,0.25)' }}>
                  <span className="pix-tag pix-tag--accent">running…</span>
                </div>
              )}
            </div>
            <div className="between p-10" style={{ borderTop: 'var(--px) solid var(--line)' }}>
              <span className="small text-muted">JDK 17 · {isLive ? 'backend' : 'demo'}</span>
              <button className="pix-btn pix-btn--primary" style={{ background: color }} disabled={runState === 'running'}
                      onClick={() => runNow(flavor)}>
                <PixelIcon kind="play" size={12} color="var(--paper)" /> Run
              </button>
            </div>
          </div>

          {/* CENTER: visualization */}
          <div className="col f1 sandbox-viz">
            <div className="between p-10" style={{ borderBottom: 'var(--px) solid var(--line)', background: 'var(--paper)' }}>
              <div className="row gap-6" style={{ alignItems: 'center' }}>
                <span className="pix-tag pix-tag--accent">VISUALIZATION</span>
                <span className="small text-muted">{total ? `step ${step + 1} / ${total}` : '—'}</span>
              </div>
              <div className="row gap-6">
                <button className="pix-btn" style={{ padding: '4px 8px' }} onClick={() => setStep(0)} disabled={!total}>⏮</button>
                <button className="pix-btn" style={{ padding: '4px 8px' }} onClick={() => setStep(s => Math.max(0, s - 1))} disabled={!total}>◀</button>
                <button className="pix-btn pix-btn--primary" style={{ background: color, padding: '4px 10px' }}
                        onClick={() => setPlaying(p => !p)} disabled={!total}>
                  {playing ? '❚❚ pause' : '▶ play'}
                </button>
                <button className="pix-btn" style={{ padding: '4px 8px' }} onClick={() => setStep(s => Math.min(total - 1, s + 1))} disabled={!total}>▶</button>
                <button className="pix-btn" style={{ padding: '4px 8px' }} onClick={() => setStep(total - 1)} disabled={!total}>⏭</button>
              </div>
            </div>

            <div className="f1 p-16" style={{ background: 'var(--bg-2)' }}>
              <div className="pix-frame pix-grid" style={{ height: '100%', minHeight: 220, padding: 10 }}>
                {renderViz(result?.vizKind, { result, frame, step, cmd })}
              </div>
            </div>

            <div className="col p-12 gap-6" style={{ borderTop: 'var(--px) solid var(--line)', background: 'var(--paper)' }}>
              <div className="tiny upper text-muted">timeline</div>
              <div className="row gap-4" style={{ alignItems: 'center' }}>
                {frames.map((_, i) => (
                  <div key={i} className="row" style={{ alignItems: 'center', flex: i < total - 1 ? 1 : 'none' }}>
                    <div className="pix-frame pix-frame--flat center clickable" style={{
                      width: 18, height: 18, fontSize: 10,
                      background: i <= step ? color : 'var(--bg-2)',
                      color: i <= step ? 'var(--paper)' : 'var(--ink)',
                    }} onClick={() => setStep(i)}>{i + 1}</div>
                    {i < total - 1 && <div style={{ flex: 1, height: 2, background: i < step ? color : 'var(--ink-3)' }} />}
                  </div>
                ))}
              </div>
              <div className="pix-mono small" style={{ background: 'var(--bg-2)', padding: '4px 8px' }}>» {cmd}</div>
            </div>
          </div>

          {/* RIGHT: console */}
          <div className="col sandbox-console" style={{ width: 320, borderLeft: 'var(--px) solid var(--line)', background: 'var(--paper)' }}>
            <div className="between p-10" style={{ borderBottom: 'var(--px) solid var(--line)' }}>
              <span className={'pix-tag ' + (runState === 'error' ? 'pix-tag--bad' : 'pix-tag--good')}>▶ stdout</span>
              <span className="small text-muted">{result ? `${result.durationMs} ms · ${runState === 'error' ? 'error' : 'ok'}` : '—'}</span>
            </div>
            <div className="code-frame__body f1 scroll-y" style={{ background: 'var(--paper)', border: 0, boxShadow: 'none', fontFamily: 'var(--font-code)' }}>
              {(result?.stdout || []).map((r, i) => (
                <div key={i} style={{ color: r.c || 'var(--ink)' }}>{r.line || ' '}</div>
              ))}
            </div>
            <div className="between p-10" style={{ borderTop: 'var(--px) solid var(--line)' }}>
              <button className="pix-btn" style={{ padding: '4px 10px', fontSize: 14 }} onClick={() => runNow(flavor)}>↻ rerun</button>
              <button className="pix-btn" style={{ padding: '4px 10px', fontSize: 14 }} onClick={() => setFlavorAndRun(flavor === 'good' ? 'bad' : 'good')}>
                Diff bad ↔ good
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
