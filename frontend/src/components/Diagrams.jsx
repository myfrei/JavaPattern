import { Fragment } from 'react'

// SVG pixel diagrams used across detail + sandbox screens.

export function MiniUML() {
  return (
    <svg viewBox="0 0 280 180" style={{ width: '100%', height: 'auto', display: 'block' }} shapeRendering="crispEdges">
      <rect x="20" y="20" width="100" height="60" fill="var(--paper)" stroke="currentColor" strokeWidth="2" />
      <line x1="20" y1="38" x2="120" y2="38" stroke="currentColor" strokeWidth="2" />
      <text x="70" y="34" textAnchor="middle" fontSize="11" fontFamily="var(--font-body)" fill="currentColor">«Subject»</text>
      <text x="26" y="54" fontSize="10" fontFamily="var(--font-code)" fill="currentColor">+attach(o)</text>
      <text x="26" y="68" fontSize="10" fontFamily="var(--font-code)" fill="currentColor">+notify()</text>

      <rect x="160" y="20" width="100" height="60" fill="var(--paper)" stroke="currentColor" strokeWidth="2" />
      <line x1="160" y1="38" x2="260" y2="38" stroke="currentColor" strokeWidth="2" />
      <text x="210" y="34" textAnchor="middle" fontSize="11" fontFamily="var(--font-body)" fill="currentColor">«Observer»</text>
      <text x="166" y="54" fontSize="10" fontFamily="var(--font-code)" fill="currentColor">+update(s)</text>

      <rect x="20" y="120" width="100" height="40" fill="var(--accent-3)" stroke="currentColor" strokeWidth="2" />
      <text x="70" y="134" textAnchor="middle" fontSize="11" fontFamily="var(--font-body)" fill="var(--paper)">NewsFeed</text>
      <text x="70" y="150" textAnchor="middle" fontSize="10" fontFamily="var(--font-code)" fill="var(--paper)">+publish()</text>

      <rect x="160" y="120" width="100" height="40" fill="var(--accent-3)" stroke="currentColor" strokeWidth="2" />
      <text x="210" y="134" textAnchor="middle" fontSize="11" fontFamily="var(--font-body)" fill="var(--paper)">Subscriber</text>
      <text x="210" y="150" textAnchor="middle" fontSize="10" fontFamily="var(--font-code)" fill="var(--paper)">+update(s)</text>

      <line x1="70" y1="120" x2="70" y2="80" stroke="currentColor" strokeWidth="2" />
      <line x1="210" y1="120" x2="210" y2="80" stroke="currentColor" strokeWidth="2" />
      <line x1="120" y1="50" x2="160" y2="50" stroke="currentColor" strokeWidth="2" strokeDasharray="4 2" />
      <text x="140" y="46" textAnchor="middle" fontSize="9" fontFamily="var(--font-body)" fill="currentColor">notifies *</text>
    </svg>
  )
}

function NodeBox({ x, y, w = 80, h = 36, label, sub, color, ghost }) {
  return (
    <g>
      <rect x={x} y={y} width={w} height={h} fill={ghost ? 'var(--paper)' : (color || 'var(--paper)')}
            stroke="currentColor" strokeWidth="2" />
      <text x={x + w / 2} y={y + h / 2 - 2} textAnchor="middle" fontSize="11"
            fontFamily="var(--font-body)" fill={ghost ? 'var(--ink)' : (color ? 'var(--paper)' : 'var(--ink)')}>
        {label}
      </text>
      {sub && <text x={x + w / 2} y={y + h / 2 + 12} textAnchor="middle" fontSize="9"
                    fontFamily="var(--font-code)" fill={ghost ? 'var(--ink-3)' : (color ? 'var(--paper)' : 'var(--ink-3)')}>
        {sub}
      </text>}
    </g>
  )
}

// Observer diagram — animates by step (0..5)
export function ObserverViz({ step = 3, label }) {
  return (
    <svg viewBox="0 0 420 280" style={{ width: '100%', height: '100%', display: 'block' }} shapeRendering="crispEdges">
      <NodeBox x={170} y={20} w={90} h={48} label="NewsFeed" sub="(subject)" color="var(--accent-3)" />
      {step >= 3 && (
        <g stroke="currentColor" strokeWidth="2" fill="none">
          <line x1="215" y1="68" x2="60" y2="170" strokeDasharray={step === 3 ? '4 2' : '0'} />
          <line x1="215" y1="68" x2="215" y2="170" strokeDasharray={step === 3 ? '4 2' : '0'} />
          <line x1="215" y1="68" x2="370" y2="170" strokeDasharray={step === 3 ? '4 2' : '0'} />
        </g>
      )}
      <NodeBox x={20} y={170} w={80} h={48} label="Sub #1" sub="EmailNotifier" color={step >= 4 ? 'var(--good)' : null} />
      <NodeBox x={175} y={170} w={80} h={48} label="Sub #2" sub="PushNotifier" color={step >= 5 ? 'var(--good)' : null} />
      <NodeBox x={330} y={170} w={80} h={48} label="Sub #3" sub="(unsubscribed)" ghost />
      {step >= 3 && (
        <g>
          <text x="120" y="130" fontSize="10" fontFamily="var(--font-code)" fill="var(--accent-3)">update("...")</text>
          <text x="220" y="120" fontSize="10" fontFamily="var(--font-code)" fill="var(--accent-3)">update("...")</text>
          <text x="290" y="130" fontSize="10" fontFamily="var(--font-code)" fill="var(--accent-3)">update("...")</text>
        </g>
      )}
      {label && (
        <text x="210" y="260" textAnchor="middle" fontSize="10" fontFamily="var(--font-body)" fill="var(--ink-3)">
          {label}
        </text>
      )}
    </svg>
  )
}

// Chain of Responsibility viz — handler boxes; highlights the active link + its decision.
// `activeIndex` = current handler (-1 = request entering); decision ∈ enter|pass|approve|reject.
export function ChainViz({ handlers = [], activeIndex = -1, decision = 'enter', broken = false }) {
  const monolith = handlers.length <= 1
  const active = handlers[activeIndex]
  let status = 'запрос вошёл в цепь'
  let statusColor = 'var(--ink-3)'
  if (decision === 'approve' && active) { status = `✓ одобрено: ${active.name}`; statusColor = 'var(--good)' }
  else if (decision === 'reject' && active) { status = `✗ отклонено: ${active.name}`; statusColor = 'var(--bad)' }
  else if (decision === 'pass' && active) { status = `${active.name} передал дальше`; statusColor = 'var(--accent-3)' }

  return (
    <div className="col gap-12 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">{monolith ? 'monolithic approval' : 'expense approval chain'}</div>
      <div className="row gap-6 wrap center" style={{ maxWidth: 580, alignItems: 'center' }}>
        <span className="pix-tag pix-tag--accent">REQ</span>
        <span style={{ color: activeIndex >= 0 ? 'var(--accent-3)' : 'var(--ink-3)' }}>→</span>
        {handlers.map((h, k) => {
          const passed = activeIndex > k
          const isActive = activeIndex === k
          let bg = 'var(--paper)', fg = 'var(--ink)', bd = 'var(--line)'
          if (isActive && decision === 'approve') { bg = 'var(--good)'; fg = 'var(--paper)' }
          else if (isActive && decision === 'reject') { bg = 'var(--bad)'; fg = 'var(--paper)' }
          else if (isActive) { bg = 'var(--accent-3)'; fg = 'var(--paper)' }
          else if (passed) { bg = 'var(--bg-2)' }
          const upcoming = activeIndex >= 0 && k > activeIndex
          return (
            <Fragment key={h.name}>
              {k > 0 && <span style={{ color: passed || isActive ? 'var(--accent-3)' : 'var(--ink-3)' }}>→</span>}
              <div className="pix-frame col center" style={{
                minWidth: monolith ? 200 : 96, padding: '8px 10px', gap: 2,
                background: bg, color: fg, border: `var(--px) solid ${bd}`,
                opacity: upcoming ? 0.45 : 1,
              }}>
                <span className="small bold" style={{ color: fg }}>{h.name}</span>
                <span className="tiny" style={{ color: isActive ? 'var(--paper)' : 'var(--ink-3)' }}>{h.limit}</span>
              </div>
            </Fragment>
          )
        })}
      </div>
      <div className="small" style={{ color: statusColor }}>{status}</div>
      {monolith && <div className="tiny text-muted center-text" style={{ maxWidth: 360 }}>один класс знает все правила — звеньев цепи нет</div>}
    </div>
  )
}

// Singleton memory viz — shows instance boxes created so far (driven by live backend data).
// `instances` is the set of unique instances; `revealed` is how many to show.
export function SingletonViz({ instances = [], revealed = 0, broken = false }) {
  const shown = instances.slice(0, Math.max(0, revealed))
  return (
    <div className="col gap-12 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">heap · getInstance()</div>
      {shown.length === 0 ? (
        <div className="text-muted small">— нет объектов —</div>
      ) : (
        <div className="row gap-16 wrap center" style={{ maxWidth: 460 }}>
          {shown.map((inst, i) => (
            <div key={i} className="pix-frame col center" style={{
              width: 110, height: 110, padding: 8, gap: 6,
              background: 'var(--paper)',
              boxShadow: `var(--pxr) var(--pxr) 0 0 ${broken ? 'var(--bad)' : 'var(--accent-2)'}`,
              border: `var(--px) solid ${broken ? 'var(--bad)' : 'var(--line)'}`,
            }}>
              <span style={{ fontSize: 26, color: broken ? 'var(--bad)' : 'var(--accent-2)' }}>▣</span>
              <span className="pix-mono tiny" style={{ color: 'var(--accent)' }}>{inst.hash}</span>
              <span className="tiny text-muted">{inst.createdBy}</span>
            </div>
          ))}
        </div>
      )}
      <div className="small" style={{ color: broken ? 'var(--bad)' : 'var(--good)' }}>
        {shown.length <= 1 ? 'один экземпляр на JVM' : `${shown.length} разных объектов — паттерн сломан`}
      </div>
    </div>
  )
}
