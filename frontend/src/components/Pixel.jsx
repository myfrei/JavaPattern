import { Link, useNavigate } from 'react-router-dom'
import { useTheme } from '../theme/ThemeProvider.jsx'

// ─── Tiny pixel icons rendered with SVG (crisp at any size) ───
const GRIDS = {
  micro:     ["X.X.", "....", "..XX", "XX.X"],
  blueprint: ["XXXX", "X..X", "XXXX", "X..X"],
  spark:     [".X..", "XXX.", ".X.X", "..XX"],
  column:    ["XXXX", "X.X.", "X.X.", "XXXX"],
  arrows:    [".X..", "XXX.", ".X.X", ".XXX"],
  code:      ["X..X", ".XX.", ".XX.", "X..X"],
  play:      ["X...", "XX..", "XXX.", "XX.."],
  book:      ["XXXX", "X..X", "X..X", "XXXX"],
  search:    [".XX.", "X..X", ".XX.", "...X"],
  sun:       ["X.X.", ".X..", "XX.X", ".X.."],
  moon:      [".XX.", "X...", "X...", ".XX."],
  check:     ["...X", "..X.", "X.X.", ".X.."],
  cross:     ["X..X", ".XX.", ".XX.", "X..X"],
  chev:      [".X..", "..X.", "..X.", ".X.."],
}

export function PixelIcon({ kind, size = 20, color }) {
  const c = color || 'currentColor'
  const g = GRIDS[kind] || GRIDS.code
  return (
    <svg width={size} height={size} viewBox="0 0 4 4" shapeRendering="crispEdges"
         style={{ display: 'inline-block', flex: 'none' }}>
      {g.flatMap((row, y) =>
        [...row].map((ch, x) =>
          ch === 'X' ? <rect key={`${x}-${y}`} x={x} y={y} width="1" height="1" fill={c} /> : null
        )
      )}
    </svg>
  )
}

// Window chrome — top bar with brand + nav + theme toggle
export function AppTopBar({ active = 'Patterns', showSearch = true, compact = false }) {
  const { theme, toggle } = useTheme()
  const navigate = useNavigate()
  const navItem = (label, to) => (
    <a className={active === label ? 'is-active' : ''} onClick={() => to && navigate(to)}>{label}</a>
  )
  return (
    <div className="app-topbar">
      <div className="app-topbar__brand" onClick={() => navigate('/')}>
        <span className="pix-chip" style={{ width: 18, height: 18, background: 'var(--accent)' }} />
        <span>JavaPattern</span>
      </div>
      {!compact && (
        <div className="app-topbar__nav">
          {navItem('Home', '/')}
          {navItem('Patterns', '/patterns/design')}
          {navItem('Sandbox', '/patterns/design/behavioral/observer/sandbox')}
          {navItem('Docs', null)}
        </div>
      )}
      <div className="row gap-8" style={{ alignItems: 'center' }}>
        {showSearch && (
          <div className="pix-frame p-8 hide-sm" style={{ display: 'flex', gap: 6, alignItems: 'center', background: 'var(--bg-2)', boxShadow: 'none', padding: '4px 8px', minWidth: 160 }}>
            <PixelIcon kind="search" size={14} />
            <span className="small text-muted">search pattern…</span>
          </div>
        )}
        <button className="pix-btn" style={{ padding: '4px 8px', boxShadow: 'none' }} onClick={toggle}
                title={theme === 'dark' ? 'Светлая тема' : 'Тёмная тема'}>
          <PixelIcon kind={theme === 'dark' ? 'sun' : 'moon'} size={16} />
        </button>
        <span className="pix-tag hide-sm">⌘K</span>
      </div>
    </div>
  )
}

// Breadcrumbs — items: { label, to?, active? }
export function Crumbs({ items }) {
  return (
    <div className="crumbs">
      {items.map((it, i) => (
        <span key={i} className="row gap-6" style={{ alignItems: 'center' }}>
          {i > 0 && <span style={{ color: 'var(--ink-3)' }}>›</span>}
          {it.active
            ? <b>{it.label}</b>
            : it.to
              ? <Link to={it.to}>{it.label}</Link>
              : <span>{it.label}</span>}
        </span>
      ))}
    </div>
  )
}

// Pattern category stripe
const KIND_BG = {
  creat: 'var(--accent)', struct: 'var(--accent-2)', behav: 'var(--accent-3)',
  decomp: 'var(--accent)', comm: 'var(--accent-2)', data: 'var(--accent-3)', resil: 'var(--accent-4)',
}
export function CatStripe({ kind = 'creat', label, count }) {
  const bg = KIND_BG[kind] || 'var(--accent)'
  return (
    <div className="between" style={{ background: bg, color: 'var(--paper)', padding: '6px 10px', borderBottom: 'var(--px) solid var(--line)' }}>
      <span className="upper small">{label}</span>
      {count != null && <span className="upper small">{count}</span>}
    </div>
  )
}

export function Stars({ n, max = 5 }) {
  return (
    <span className="pix-mono" style={{ color: 'var(--accent)' }}>
      {'★'.repeat(n)}<span style={{ color: 'var(--ink-3)' }}>{'☆'.repeat(max - n)}</span>
    </span>
  )
}

const DIFF = {
  easy:   { label: 'easy',   bg: 'var(--good)' },
  medium: { label: 'medium', bg: 'var(--accent)' },
  hard:   { label: 'hard',   bg: 'var(--bad)' },
}
export function DiffPill({ diff }) {
  const s = DIFF[diff] || DIFF.medium
  return <span className="pix-tag" style={{ background: s.bg, color: 'var(--paper)' }}>{s.label}</span>
}

export const CAT_COLOR = {
  creational: 'var(--accent)', structural: 'var(--accent-2)', behavioral: 'var(--accent-3)',
  decomposition: 'var(--accent)', communication: 'var(--accent-2)', data: 'var(--accent-3)', resilience: 'var(--accent-4)',
}
export const CAT_KIND = {
  creational: 'creat', structural: 'struct', behavioral: 'behav',
  decomposition: 'decomp', communication: 'comm', data: 'data', resilience: 'resil',
}
