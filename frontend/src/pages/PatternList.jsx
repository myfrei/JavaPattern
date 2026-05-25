import { useState } from 'react'
import { useParams, useNavigate, Navigate } from 'react-router-dom'
import { AppTopBar, Crumbs, PixelIcon, Stars, DiffPill, CAT_COLOR } from '../components/Pixel.jsx'
import { getCategory, getSection, getCategoryPatterns, doneCount, getDetail } from '../data/patterns.js'

function PatternArt({ name, cell = 12, color = 'var(--accent-3)' }) {
  return (
    <div style={{ display: 'grid', gridTemplateColumns: `repeat(6, ${cell}px)`, gridTemplateRows: `repeat(4, ${cell}px)`, gap: 2 }}>
      {Array.from({ length: 24 }).map((_, k) => {
        const seed = (name.charCodeAt(k % name.length) + k) % 5
        return <div key={k} style={{ width: cell, height: cell, background: seed < 2 ? 'var(--paper)' : seed < 4 ? 'var(--line)' : 'transparent' }} />
      })}
    </div>
  )
}

export default function PatternList() {
  const { section, category } = useParams()
  const navigate = useNavigate()
  const cat = getCategory(category)
  const meta = getSection(section)
  const patterns = getCategoryPatterns(category)
  const [hovered, setHovered] = useState(patterns[0]?.id || null)

  if (!cat) return <Navigate to="/patterns/design" replace />

  const color = CAT_COLOR[category]
  const open = patterns.find(p => p.id === hovered) || patterns[0]
  const detail = open ? getDetail(open.id) : null

  return (
    <div className="screen">
      <AppTopBar active="Patterns" />
      <Crumbs items={[
        { label: 'Patterns', to: '/' },
        { label: meta?.title || 'Patterns', to: `/patterns/${section}` },
        { label: cat.label, active: true },
      ]} />

      <div className="between p-12" style={{ borderBottom: 'var(--px) solid var(--line)', background: 'var(--paper)' }}>
        <span className="row gap-8 clickable" style={{ alignItems: 'center' }} onClick={() => navigate(`/patterns/${section}`)}>
          <PixelIcon kind="chev" size={12} /> <span className="pix-display" style={{ fontSize: 12 }}>CATEGORIES</span>
        </span>
        <span className="pix-display" style={{ fontSize: 14 }}>{cat.label.toUpperCase()} · {patterns.length}/{cat.count}</span>
        <span className="pix-display" style={{ fontSize: 12 }}>★ {doneCount(category)}</span>
      </div>

      <div className="row f1" style={{ overflow: 'hidden' }}>
        <div className="f1 p-20 scroll-y">
          <div className="grid-3">
            {patterns.map((p, i) => (
              <div key={p.id} className="pix-frame pix-frame--hover col clickable"
                   style={{ position: 'relative' }}
                   onMouseEnter={() => setHovered(p.id)}
                   onClick={() => navigate(`/patterns/${section}/${category}/${p.id}`)}>
                <div className="pix-display" style={{ position: 'absolute', top: 6, left: 8, fontSize: 11, color: 'var(--ink-3)', zIndex: 1 }}>
                  №{String(p.number).padStart(2, '0')}
                </div>
                <div className="center" style={{ background: color, aspectRatio: '16/9', borderBottom: 'var(--px) solid var(--line)' }}>
                  <PatternArt name={p.name} color={color} />
                </div>
                <div className="col p-12 gap-4">
                  <div className="between">
                    <div className="bold" style={{ fontSize: 16 }}>{p.name}</div>
                    <DiffPill diff={p.diff} />
                  </div>
                  <div className="small text-muted">{p.ru}</div>
                  <div className="between" style={{ marginTop: 4 }}>
                    <Stars n={p.pop} />
                    {p.status === 'done' && <PixelIcon kind="check" size={14} color="var(--good)" />}
                    {p.status === 'in-progress' && <span className="pix-tag pix-tag--accent tiny">WIP</span>}
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* focused preview */}
        <div className="col only-desktop" style={{ width: 280, borderLeft: 'var(--px) solid var(--line)', background: 'var(--bg-2)' }}>
          {open && (
            <div className="p-16 col gap-12">
              <div className="tiny upper text-muted">hovered</div>
              <div className="bold" style={{ fontSize: 22 }}>{open.name}</div>
              <div className="small text-muted">№{String(open.number).padStart(2, '0')} · {open.ru}</div>
              <div className="pix-frame center" style={{ background: color, aspectRatio: '1/1', padding: 16 }}>
                <PatternArt name={open.name} cell={18} color={color} />
              </div>
              <div className="small">{detail?.intent || `${open.ru} — ${cat.label.toLowerCase()} pattern.`}</div>
              <button className="pix-btn pix-btn--primary" style={{ background: color }}
                      onClick={() => navigate(`/patterns/${section}/${category}/${open.id}`)}>
                Open pattern →
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}
