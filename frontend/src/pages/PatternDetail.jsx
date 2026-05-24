import { useState } from 'react'
import { useParams, useNavigate, Navigate } from 'react-router-dom'
import { AppTopBar, Crumbs, PixelIcon, Stars, DiffPill, CAT_COLOR } from '../components/Pixel.jsx'
import { CodeBlock } from '../components/CodeView.jsx'
import { MiniUML, ObserverViz, ChainViz } from '../components/Diagrams.jsx'
import { getCategory, getCategoryPatterns, findPattern, getDetail } from '../data/patterns.js'

const TABS = ['Theory', 'UML', 'Code', 'Visualization', 'Use cases', 'Pros / Cons', 'Related']

export default function PatternDetail() {
  const { category, patternId } = useParams()
  const navigate = useNavigate()
  const cat = getCategory(category)
  const pattern = findPattern(category, patternId)
  const detail = getDetail(patternId)
  const [tab, setTab] = useState('Code')
  const [flavor, setFlavor] = useState('good')
  const [tracked, setTracked] = useState(pattern?.status === 'done')

  if (!cat || !pattern) return <Navigate to="/patterns/design" replace />

  const color = CAT_COLOR[category]
  const sandbox = `/patterns/design/${category}/${patternId}/sandbox`
  const code = detail?.code?.[flavor]

  const copyCode = () => { if (code) navigator.clipboard?.writeText(code) }

  return (
    <div className="screen">
      <AppTopBar active="Patterns" />
      <Crumbs items={[
        { label: 'Patterns', to: '/' },
        { label: 'Design', to: '/patterns/design' },
        { label: cat.label, to: `/patterns/design/${category}` },
        { label: pattern.name, active: true },
      ]} />

      <div className="row f1" style={{ overflow: 'hidden' }}>
        {/* left: pattern nav */}
        <div className="col only-desktop scroll-y" style={{ width: 200, borderRight: 'var(--px) solid var(--line)', background: 'var(--bg-2)' }}>
          <div className="p-12 col gap-2">
            <div className="tiny upper text-muted">{cat.label}</div>
            {getCategoryPatterns(category).map(p => {
              const sel = p.id === patternId
              return (
                <div key={p.id} className="between clickable"
                     style={{ padding: '6px 8px', fontSize: 15, background: sel ? color : 'transparent', color: sel ? 'var(--paper)' : 'var(--ink)' }}
                     onClick={() => navigate(`/patterns/design/${category}/${p.id}`)}>
                  <span>{p.name}</span>
                  {sel && <PixelIcon kind="chev" size={12} color="var(--paper)" />}
                </div>
              )
            })}
          </div>
        </div>

        {/* main */}
        <div className="f1 col" style={{ overflow: 'hidden' }}>
          {/* header */}
          <div className="p-20 col gap-12" style={{ borderBottom: 'var(--px) solid var(--line)', background: 'var(--paper)' }}>
            <div className="between gap-12 wrap">
              <div className="col gap-4">
                <div className="row gap-8 wrap" style={{ alignItems: 'center' }}>
                  <span className="pix-tag" style={{ background: color, color: 'var(--paper)' }}>{cat.label.toUpperCase()} · №{String(pattern.number).padStart(2, '0')}</span>
                  <span className="pix-tag"><Stars n={pattern.pop} /></span>
                  <DiffPill diff={pattern.diff} />
                </div>
                <div className="bold" style={{ fontSize: 30 }}>{detail?.name || pattern.name} · {detail?.ru || pattern.ru}</div>
                <div className="small text-ink2" style={{ maxWidth: 580 }}>
                  {detail?.intent || `${pattern.ru} — ${cat.label.toLowerCase()} паттерн.`}
                </div>
              </div>
              <div className="col gap-8">
                <button className="pix-btn pix-btn--primary" style={{ background: color }} onClick={() => navigate(sandbox)}>
                  <PixelIcon kind="play" size={12} color="var(--paper)" /> Run example
                </button>
                <button className="pix-btn" onClick={() => setTracked(t => !t)}>
                  {tracked ? '✓ tracked' : '+ track'}
                </button>
              </div>
            </div>
            {/* tabs */}
            <div className="row gap-6 wrap" style={{ marginTop: 8 }}>
              {TABS.map(t => (
                <button key={t} className="pix-btn"
                        style={{ background: t === tab ? color : 'var(--paper)', color: t === tab ? 'var(--paper)' : 'var(--ink)' }}
                        onClick={() => setTab(t)}>{t}</button>
              ))}
            </div>
          </div>

          {/* content + right rail */}
          <div className="f1 row scroll-y p-20 gap-20 resp-stack">
            <div className="col gap-12 f1">
              <TabContent tab={tab} detail={detail} pattern={pattern} cat={cat} color={color}
                          flavor={flavor} setFlavor={setFlavor} code={code} copyCode={copyCode}
                          onRun={() => navigate(sandbox)} />
            </div>

            <div className="col gap-12 only-desktop" style={{ width: 320 }}>
              <div className="pix-frame p-12">
                <div className="tiny upper text-muted">UML</div>
                {detail?.viz === 'observer' ? <MiniUML /> : (
                  <div className="center text-muted small" style={{ height: 140 }}>UML диаграмма скоро</div>
                )}
              </div>
              {detail?.related && (
                <div className="pix-frame p-12 col gap-8">
                  <div className="tiny upper text-muted">related</div>
                  {detail.related.map(r => (
                    <div key={r.id} className="between clickable" style={{ padding: '4px 0', borderBottom: '1px dashed var(--line)' }}
                         onClick={() => navigate(`/patterns/design/${r.kind === 'creat' ? 'creational' : r.kind === 'struct' ? 'structural' : 'behavioral'}/${r.id}`)}>
                      <span className="row gap-6" style={{ alignItems: 'center' }}>
                        <span style={{ width: 8, height: 8, background: r.kind === 'creat' ? 'var(--accent)' : r.kind === 'struct' ? 'var(--accent-2)' : 'var(--accent-3)' }} />
                        <span>{r.name}</span>
                      </span>
                      <span className="tiny text-muted">{r.why}</span>
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

function TabContent({ tab, detail, pattern, cat, color, flavor, setFlavor, code, copyCode, onRun }) {
  if (tab === 'Code') {
    return (
      <>
        <div className="row gap-8">
          <button className="pix-btn pix-btn--primary" style={{ background: flavor === 'good' ? 'var(--good)' : 'var(--paper)', color: flavor === 'good' ? 'var(--paper)' : 'var(--ink)' }}
                  onClick={() => setFlavor('good')}>✓ Good example</button>
          <button className="pix-btn" style={{ background: flavor === 'bad' ? 'var(--bad)' : 'var(--paper)', color: flavor === 'bad' ? 'var(--paper)' : 'var(--ink)' }}
                  onClick={() => setFlavor('bad')}>✗ Bad example</button>
        </div>
        {code ? <CodeBlock code={code} fileName={`${pattern.name.replace(/[^A-Za-z]/g, '')}.java`} height={340} />
              : <div className="pix-frame p-16 text-muted">Пример кода для этого паттерна скоро появится.</div>}
        <div className="row gap-8 wrap">
          <button className="pix-btn pix-btn--primary" style={{ background: color }} onClick={onRun}>
            <PixelIcon kind="play" size={12} color="var(--paper)" /> Run on backend
          </button>
          <button className="pix-btn" onClick={copyCode}>Copy</button>
          <button className="pix-btn" onClick={onRun}>Open in sandbox →</button>
        </div>
      </>
    )
  }
  if (tab === 'Theory') {
    return (
      <div className="col gap-12">
        <div className="pix-frame p-12 row gap-12" style={{ background: 'var(--bg-2)', boxShadow: 'none' }}>
          <PixelIcon kind="book" size={20} color={color} />
          <div className="small"><b>Intent · </b>{detail?.intent || `${pattern.ru} — ${cat.label.toLowerCase()} паттерн.`}</div>
        </div>
        {detail?.motivation?.length ? (
          <>
            <div className="bold" style={{ fontSize: 20 }}>Motivation</div>
            {detail.motivation.map((m, i) => <p key={i} className="text-ink2" style={{ margin: 0 }}>{m}</p>)}
          </>
        ) : <div className="text-muted">Теория для этого паттерна скоро появится.</div>}
      </div>
    )
  }
  if (tab === 'UML') {
    return detail?.viz === 'observer'
      ? <div className="pix-frame p-16"><MiniUML /></div>
      : <div className="pix-frame p-16 center text-muted" style={{ minHeight: 200 }}>UML диаграмма скоро</div>
  }
  if (tab === 'Visualization') {
    return (
      <div className="col gap-12">
        <div className="pix-frame pix-grid p-16" style={{ height: 280 }}>
          {detail?.viz === 'observer'
            ? <ObserverViz step={3} label="preview · клик откроет полный sandbox" />
            : detail?.viz === 'chain'
            ? <ChainViz handlers={detail.previewHandlers || []} activeIndex={2} decision="approve" />
            : <div className="center text-muted" style={{ height: '100%' }}>Интерактивная визуализация — в sandbox</div>}
        </div>
        <button className="pix-btn pix-btn--primary" style={{ background: color, alignSelf: 'flex-start' }} onClick={onRun}>
          <PixelIcon kind="play" size={12} color="var(--paper)" /> Открыть в полном экране →
        </button>
      </div>
    )
  }
  if (tab === 'Use cases') {
    return detail?.useCases?.length ? (
      <div className="col gap-6">
        {detail.useCases.map(u => (
          <div key={u.item} className="row gap-12" style={{ padding: '6px 0', borderBottom: '1px dashed var(--line)' }}>
            <span className="pix-tag">{u.src}</span>
            <span>{u.item}</span>
          </div>
        ))}
      </div>
    ) : <div className="text-muted">Примеры применения скоро появятся.</div>
  }
  if (tab === 'Pros / Cons') {
    if (!detail?.pros) return <div className="text-muted">Плюсы и минусы скоро появятся.</div>
    return (
      <div className="row gap-16 resp-stack">
        <div className="pix-frame p-12 col gap-6 f1">
          <span className="pix-tag pix-tag--good"><PixelIcon kind="check" size={10} color="var(--paper)" /> pros</span>
          {detail.pros.map(p => <div key={p} className="row gap-6"><span style={{ color: 'var(--good)' }}>+</span> <span>{p}</span></div>)}
        </div>
        <div className="pix-frame p-12 col gap-6 f1">
          <span className="pix-tag pix-tag--bad"><PixelIcon kind="cross" size={10} color="var(--paper)" /> cons</span>
          {detail.cons.map(p => <div key={p} className="row gap-6"><span style={{ color: 'var(--bad)' }}>−</span> <span>{p}</span></div>)}
        </div>
      </div>
    )
  }
  if (tab === 'Related') {
    return detail?.related?.length ? (
      <div className="row gap-8 wrap">
        {detail.related.map(r => (
          <div key={r.id} className="pix-frame pix-frame--flat" style={{ padding: '6px 10px', display: 'flex', gap: 6, alignItems: 'center' }}>
            <span style={{ width: 8, height: 8, background: r.kind === 'creat' ? 'var(--accent)' : r.kind === 'struct' ? 'var(--accent-2)' : 'var(--accent-3)' }} />
            <b>{r.name}</b>
            <span className="tiny text-muted">— {r.why}</span>
          </div>
        ))}
      </div>
    ) : <div className="text-muted">Связанные паттерны скоро появятся.</div>
  }
  return null
}
