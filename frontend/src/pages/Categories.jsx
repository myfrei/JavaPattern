import { useState } from 'react'
import { useParams, useNavigate, Navigate } from 'react-router-dom'
import { AppTopBar, Crumbs, CatStripe, PixelIcon, CAT_COLOR } from '../components/Pixel.jsx'
import { getSectionCategories, getSection, getCategoryPatterns } from '../data/patterns.js'

export default function Categories() {
  const { section } = useParams()
  const navigate = useNavigate()
  const cats = getSectionCategories(section)
  const meta = getSection(section)
  const [expanded, setExpanded] = useState(cats[0]?.id || null)

  if (!cats.length) return <Navigate to="/patterns/design" replace />

  const total = cats.reduce((n, c) => n + c.count, 0)

  return (
    <div className="screen">
      <AppTopBar active="Patterns" />
      <Crumbs items={[{ label: 'Patterns', to: '/' }, { label: meta?.title || 'Patterns', active: true }]} />
      <div className="screen__body p-20 gap-16 scroll-y maxw">
        <div className="between gap-12 wrap">
          <div className="col gap-4">
            <div className="bold" style={{ fontSize: 26 }}>{meta?.title || 'Patterns'} · {meta?.sub || ''}</div>
            <div className="small text-muted">{total} паттернов в {cats.length} категориях. Кликни категорию — раскроется список.</div>
          </div>
          <div className="row gap-8">
            <button className="pix-btn"><PixelIcon kind="search" size={14} /> Filter</button>
            <button className="pix-btn">Sort: A→Z</button>
          </div>
        </div>

        <div className="row gap-20 resp-stack" style={{ alignItems: 'stretch' }}>
          {cats.map((c) => {
            const open = expanded === c.id
            const bg = CAT_COLOR[c.id] || 'var(--accent)'
            const patterns = getCategoryPatterns(c.id)
            return (
              <div key={c.id} className="pix-frame col" style={{ flex: 1 }}>
                <div className="clickable" onClick={() => navigate(`/patterns/${section}/${c.id}`)}>
                  <CatStripe kind={c.kind} label={`${c.label} · ${c.ru}`} count={`${c.count} patterns`} />
                </div>
                <div className="p-16 col gap-12 f1">
                  <div className="row gap-12" style={{ alignItems: 'flex-start' }}>
                    <div style={{ width: 48, height: 48, background: bg, border: 'var(--px) solid var(--line)', flex: 'none' }} className="center">
                      <PixelIcon kind={c.icon} size={28} color="var(--paper)" />
                    </div>
                    <div className="small text-ink2 f1">{c.desc}</div>
                  </div>

                  {open ? (
                    <div className="col gap-6">
                      <div className="tiny upper text-muted">patterns</div>
                      <div className="col gap-4">
                        {patterns.map(p => (
                          <div key={p.id} className="between pix-frame pix-frame--flat clickable"
                               style={{ padding: '6px 10px', background: 'var(--bg-2)' }}
                               onClick={() => navigate(`/patterns/${section}/${c.id}/${p.id}`)}>
                            <span>{p.name}</span>
                            <PixelIcon kind="chev" size={14} color="var(--ink-3)" />
                          </div>
                        ))}
                      </div>
                    </div>
                  ) : (
                    <div className="col gap-6">
                      <div className="tiny upper text-muted">preview</div>
                      <div className="row gap-6 wrap">
                        {patterns.slice(0, 4).map(p => (
                          <span key={p.id} className="pix-tag clickable"
                                onClick={() => navigate(`/patterns/${section}/${c.id}/${p.id}`)}>{p.name}</span>
                        ))}
                        {patterns.length > 4 && <span className="pix-tag">+{patterns.length - 4}</span>}
                      </div>
                    </div>
                  )}

                  <div className="f1" />
                  <div className="row gap-8">
                    <button className="pix-btn" onClick={() => setExpanded(open ? null : c.id)}>
                      {open ? 'Свернуть' : 'Раскрыть'} <PixelIcon kind="chev" size={12} />
                    </button>
                    <button className="pix-btn pix-btn--primary" style={{ background: bg }}
                            onClick={() => navigate(`/patterns/${section}/${c.id}`)}>
                      Open →
                    </button>
                  </div>
                </div>
              </div>
            )
          })}
        </div>
      </div>
    </div>
  )
}
