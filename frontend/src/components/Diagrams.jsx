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

// ─── Shared building blocks for per-pattern visualizations ───

// A labelled box; highlights when active. Used by most bespoke viz below.
export function VizBox({ title, sub, active, done, bad, dim, minWidth = 96 }) {
  let bg = 'var(--paper)', fg = 'var(--ink)', bd = 'var(--line)'
  if (active && bad) { bg = 'var(--bad)'; fg = 'var(--paper)'; bd = 'var(--bad)' }
  else if (active) { bg = 'var(--accent-3)'; fg = 'var(--paper)'; bd = 'var(--accent-3)' }
  else if (done) { bg = 'var(--bg-2)' }
  return (
    <div className="pix-frame col center" style={{
      minWidth, padding: '8px 10px', gap: 2, background: bg, color: fg,
      border: `var(--px) solid ${bd}`, opacity: dim ? 0.45 : 1,
    }}>
      <span className="small bold" style={{ color: fg }}>{title}</span>
      {sub && <span className="tiny" style={{ color: active ? 'var(--paper)' : 'var(--ink-3)' }}>{sub}</span>}
    </div>
  )
}

export function VizStatus({ text, color = 'var(--ink-3)' }) {
  return <div className="small" style={{ color }}>{text}</div>
}

// Status colour from a Step's ok flag + result keywords.
export function stepTone(frame) {
  const r = (frame?.result || '')
  if (/FAIL|REJECT|✗|сломан|race|гонка/i.test(r) || frame?.ok === false) return 'var(--bad)'
  if (/PASS|OK|✓|APPROVED|готов/i.test(r)) return 'var(--good)'
  return 'var(--accent-3)'
}

// Factory Method — creator→product cards; highlights the product being built.
export function FactoryMethodViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  const monolith = instances.length <= 1
  const status = frame ? `${frame.action} → ${frame.result}`
    : monolith ? 'один класс знает все каналы' : 'клиент работает с абстракциями Creator/Notifier'
  return (
    <div className="col gap-12 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">{monolith ? 'monolithic switch' : 'creator → product'}</div>
      <div className="row gap-12 wrap center" style={{ maxWidth: 540, alignItems: 'flex-start' }}>
        {instances.map((inst, i) => {
          const active = inst.hash === activeName
          return (
            <div key={i} className="col center gap-4">
              {!monolith && <span className="pix-tag" style={{ fontSize: 11 }}>{inst.createdBy}</span>}
              {!monolith && <span className="tiny" style={{ color: active ? 'var(--accent-3)' : 'var(--ink-3)' }}>↓ create()</span>}
              <VizBox title={inst.hash} sub={monolith ? inst.createdBy : 'Notifier'}
                      active={active} bad={broken && (active || monolith)} minWidth={130} />
            </div>
          )
        })}
      </div>
      <VizStatus text={status} color={monolith && !frame ? 'var(--bad)' : stepTone(frame)} />
    </div>
  )
}

// Abstract Factory — product cards tagged by family; flags mixed families.
export function AbstractFactoryViz({ result, frame }) {
  const instances = result?.instances || []
  const families = [...new Set(instances.map(i => i.createdBy))]
  const consistent = families.length <= 1
  const activeName = frame?.actor
  return (
    <div className="col gap-12 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">{consistent ? `family · ${families[0] || '—'}` : 'mixed families ✗'}</div>
      <div className="row gap-12 wrap center" style={{ maxWidth: 520, alignItems: 'flex-start' }}>
        {instances.map((inst, i) => {
          const active = inst.hash === activeName
          const famColor = inst.createdBy === families[0] ? 'var(--accent)' : 'var(--accent-2)'
          return (
            <div key={i} className="col center gap-4">
              <span className="pix-tag" style={{ background: consistent ? 'var(--accent)' : famColor, color: 'var(--paper)', fontSize: 11 }}>
                {inst.createdBy}
              </span>
              <VizBox title={inst.hash} sub="widget" active={active} bad={!consistent && active} minWidth={130} />
            </div>
          )
        })}
      </div>
      <VizStatus
        text={frame ? `${frame.action} → ${frame.result}` : (consistent ? 'все виджеты из одной фабрики' : 'разные платформы в одном UI')}
        color={consistent ? 'var(--good)' : 'var(--bad)'} />
    </div>
  )
}

// Builder — object panel that fills part-by-part as steps progress.
export function BuilderViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const reveal = frame?.reveal ?? instances.length
  const shown = instances.slice(0, reveal)
  const isBuild = (frame?.action || '').includes('build') || (frame?.action || '').includes('new Pizza')
  const done = isBuild || !frame
  return (
    <div className="col gap-12 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">{broken ? 'telescoping constructor' : 'step-by-step build'}</div>
      <div className="pix-frame col gap-4" style={{
        minWidth: 260, padding: 12, background: 'var(--paper)',
        border: `var(--px) solid ${broken ? 'var(--bad)' : 'var(--line)'}`,
        boxShadow: `var(--pxr) var(--pxr) 0 0 ${broken ? 'var(--bad)' : 'var(--accent-2)'}`,
      }}>
        <span className="small bold">{broken ? 'new Pizza( … )' : 'Pizza.builder()'}</span>
        {shown.map((p, i) => (
          <div key={i} className="between" style={{ gap: 12 }}>
            <span className="tiny text-muted">{p.createdBy}</span>
            <span className="small pix-mono" style={{ color: broken ? 'var(--bad)' : 'var(--ink)' }}>{p.hash}</span>
          </div>
        ))}
        {!broken && <span className="small" style={{ color: done ? 'var(--good)' : 'var(--ink-3)' }}>{done ? '→ build() ✓' : '…собираем'}</span>}
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : 'итоговый объект иммутабелен'}
                 color={broken ? 'var(--bad)' : stepTone(frame)} />
    </div>
  )
}

// Prototype — original vs clone; shows whether the nested state is shared.
export function PrototypeViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  return (
    <div className="col gap-16 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">{broken ? 'shallow copy · shared ref' : 'deep copy · independent'}</div>
      <div className="row gap-12 center" style={{ alignItems: 'center' }}>
        {instances.map((inst, i) => {
          const active = inst.hash === activeName
          return (
            <Fragment key={i}>
              {i > 0 && <span className="small" style={{ color: broken ? 'var(--bad)' : 'var(--good)' }}>{broken ? '⇄ shared' : '∥ copy'}</span>}
              <div className="pix-frame col center" style={{
                minWidth: 150, padding: 10, gap: 4,
                border: `var(--px) solid ${broken ? 'var(--bad)' : 'var(--line)'}`,
                background: active ? 'var(--accent-3)' : 'var(--paper)',
                color: active ? 'var(--paper)' : 'var(--ink)',
              }}>
                <span className="small bold" style={{ color: active ? 'var(--paper)' : 'var(--ink)' }}>{inst.hash}</span>
                <span className="tiny pix-mono" style={{ color: active ? 'var(--paper)' : 'var(--ink-3)' }}>{inst.createdBy}</span>
              </div>
            </Fragment>
          )
        })}
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : (broken ? 'мутация клона портит оригинал' : 'клон полностью независим')}
                 color={broken ? 'var(--bad)' : 'var(--good)'} />
    </div>
  )
}

// Adapter — incompatible interfaces joined by a converter.
export function AdapterViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  return (
    <div className="col gap-12 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">{broken ? 'direct call · manual convert' : 'client → adapter → legacy api'}</div>
      <div className="row gap-6 wrap center" style={{ alignItems: 'center', maxWidth: 560 }}>
        {instances.map((inst, i) => {
          const active = inst.hash === activeName
          const bridgeLink = /adapter/i.test(inst.createdBy) || /adapter/i.test(instances[i - 1]?.createdBy || '')
          return (
            <Fragment key={i}>
              {i > 0 && <span style={{ color: 'var(--accent-3)' }}>{bridgeLink ? '⇄' : '→'}</span>}
              <VizBox title={inst.hash} sub={inst.createdBy} active={active} bad={broken && active} minWidth={120} />
            </Fragment>
          )
        })}
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : (broken ? 'конвертация дублируется в клиенте' : 'несовместимость спрятана в адаптере')}
                 color={broken ? 'var(--bad)' : stepTone(frame)} />
    </div>
  )
}

// Bridge — two axes via composition (good) vs N×M class grid (bad).
export function BridgeViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  if (broken) {
    return (
      <div className="col gap-12 center" style={{ height: '100%', justifyContent: 'center' }}>
        <div className="tiny upper text-muted">N × M combination classes</div>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: 8, maxWidth: 360 }}>
          {instances.map((inst, i) => <VizBox key={i} title={inst.hash} sub="class" active={inst.hash === activeName} bad minWidth={150} />)}
        </div>
        <VizStatus text={frame ? `${frame.action} → ${frame.result}` : 'рост как произведение осей'} color="var(--bad)" />
      </div>
    )
  }
  const abstractions = instances.filter(i => i.createdBy === 'abstraction')
  const impls = instances.filter(i => i.createdBy === 'implementation')
  return (
    <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">abstraction ── bridge ── implementation</div>
      <div className="row gap-8 center">{abstractions.map((a, i) => <VizBox key={i} title={a.hash} sub="shape" active={a.hash === activeName} />)}</div>
      <div style={{ color: 'var(--accent)', letterSpacing: 4 }}>◇──────◇</div>
      <div className="row gap-8 center">{impls.map((a, i) => <VizBox key={i} title={a.hash} sub="renderer" active={a.hash === activeName} />)}</div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : 'rows + cols, не rows × cols'} color={stepTone(frame)} />
    </div>
  )
}

// Composite — indented tree with size badges (hash carries indentation).
export function CompositeViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  return (
    <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">{broken ? 'flat traversal · misses nested' : 'recursive tree size'}</div>
      <div className="pix-frame col" style={{ minWidth: 240, padding: 12, gap: 2, background: 'var(--paper)' }}>
        {instances.map((inst, i) => {
          const active = inst.hash === activeName
          const isDir = inst.createdBy === 'dir'
          return (
            <div key={i} className="between" style={{ gap: 16, padding: '1px 4px', background: active ? 'var(--accent-3)' : 'transparent', color: active ? 'var(--paper)' : 'var(--ink)' }}>
              <span className="small pix-mono" style={{ whiteSpace: 'pre' }}>{inst.hash}</span>
              <span className="tiny" style={{ color: active ? 'var(--paper)' : isDir ? 'var(--ink-3)' : 'var(--accent)' }}>{isDir ? 'dir' : inst.createdBy + 'b'}</span>
            </div>
          )
        })}
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : (broken ? 'вложенные узлы потеряны' : 'сумма по всему дереву')}
                 color={broken ? 'var(--bad)' : stepTone(frame)} />
    </div>
  )
}

// Decorator — wrappers stacked around a core (good) vs class-per-combo (bad).
export function DecoratorViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  if (broken) {
    return (
      <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
        <div className="tiny upper text-muted">class per combination (2ⁿ)</div>
        <div className="col gap-4 center">
          {instances.map((inst, i) => <VizBox key={i} title={inst.hash} sub="class" active={inst.hash === activeName} bad minWidth={240} />)}
        </div>
        <VizStatus text={frame ? `${frame.action} → ${frame.result}` : 'каждая добавка удваивает классы'} color="var(--bad)" />
      </div>
    )
  }
  const reveal = frame?.reveal ?? instances.length
  const shown = instances.slice(0, Math.max(1, reveal))
  return (
    <div className="col gap-8 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">wrappers around core</div>
      <div className="col center" style={{ gap: 0 }}>
        {shown.map((inst, i) => {
          const active = inst.hash === activeName
          return (
            <div key={i} className="row center" style={{
              padding: '6px 12px', minWidth: 160 + i * 18, gap: 8, marginTop: i ? -2 : 0,
              background: active ? 'var(--accent-3)' : 'var(--paper)', color: active ? 'var(--paper)' : 'var(--ink)',
              border: 'var(--px) solid var(--line)',
            }}>
              <span className="small bold" style={{ color: active ? 'var(--paper)' : 'var(--ink)' }}>{inst.hash}</span>
              <span className="tiny" style={{ color: active ? 'var(--paper)' : 'var(--ink-3)' }}>{inst.createdBy}</span>
            </div>
          )
        })}
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : 'композиция обёрток в рантайме'} color={stepTone(frame)} />
    </div>
  )
}

// Facade — single entry in front of hidden subsystems (good) vs client→all (bad).
export function FacadeViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  const facade = instances.find(i => i.createdBy === 'facade')
  const subs = instances.filter(i => i.createdBy !== 'facade')
  return (
    <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">{broken ? 'client → many subsystems' : 'facade hides subsystems'}</div>
      {facade
        ? <VizBox title={facade.hash} sub="single entry" active={facade.hash === activeName} minWidth={210} />
        : <span className="pix-tag pix-tag--bad">CLIENT</span>}
      <span style={{ color: broken ? 'var(--bad)' : 'var(--accent-3)' }}>{broken ? '↙  ↓  ↘' : '↓'}</span>
      <div className="row gap-8 wrap center" style={{ maxWidth: 420 }}>
        {subs.map((s, i) => <VizBox key={i} title={s.hash} sub="subsystem" active={s.hash === activeName} bad={broken && s.hash === activeName} />)}
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : (broken ? 'клиент знает все подсистемы' : 'один вызов вместо многих')}
                 color={broken ? 'var(--bad)' : stepTone(frame)} />
    </div>
  )
}

// Flyweight — shared pool + many light refs (good) vs heavy per-object (bad).
const FLY_COLORS = ['var(--accent)', 'var(--accent-2)', 'var(--accent-3)']
export function FlyweightViz({ result, frame }) {
  const instances = result?.instances || [] // trees: hash=id, createdBy=type
  const broken = result?.broken
  const activeName = frame?.actor
  if (broken) {
    return (
      <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
        <div className="tiny upper text-muted">heavy data per object</div>
        <div className="row gap-8 wrap center" style={{ maxWidth: 460 }}>
          {instances.map((t, i) => <VizBox key={i} title={t.hash} sub={t.createdBy} active={t.hash === activeName} bad minWidth={92} />)}
        </div>
        <VizStatus text={frame ? `${frame.action} → ${frame.result}` : 'копия тяжёлых данных в каждом'} color="var(--bad)" />
      </div>
    )
  }
  const pool = [...new Set(instances.map(t => t.createdBy))]
  const colorOf = (ty) => FLY_COLORS[pool.indexOf(ty) % FLY_COLORS.length]
  return (
    <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">shared pool · {pool.length} flyweights</div>
      <div className="row gap-10 center">
        {pool.map((ty, i) => (
          <div key={i} className="pix-frame col center" style={{
            minWidth: 110, padding: 8, gap: 2, background: 'var(--paper)',
            boxShadow: `var(--pxr) var(--pxr) 0 0 ${colorOf(ty)}`, border: 'var(--px) solid var(--line)',
          }}>
            <span style={{ fontSize: 20, color: colorOf(ty) }}>▣</span>
            <span className="tiny bold">{ty}</span>
          </div>
        ))}
      </div>
      <div className="tiny text-muted">↑ {instances.length} деревьев ссылаются на пул</div>
      <div className="row gap-4 wrap center" style={{ maxWidth: 420 }}>
        {instances.map((t, i) => {
          const active = t.hash === activeName
          return <span key={i} className="pix-tag" style={{
            background: active ? colorOf(t.createdBy) : 'var(--bg-2)',
            color: active ? 'var(--paper)' : 'var(--ink)', fontSize: 11, border: `2px solid ${colorOf(t.createdBy)}`,
          }}>{t.hash}</span>
        })}
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : 'тяжёлое состояние — общее'} color={stepTone(frame)} />
    </div>
  )
}

// Proxy — client → proxy(cache) → real, with HIT/MISS (good) vs direct (bad).
export function ProxyViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  const hit = /HIT/i.test(frame?.result || '')
  return (
    <div className="col gap-12 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">{broken ? 'client → real (every call)' : 'client → proxy(cache) → real'}</div>
      <div className="row gap-6 center" style={{ alignItems: 'center', flexWrap: 'wrap' }}>
        {instances.map((inst, i) => {
          const active = inst.hash === activeName
          const isProxy = /proxy/i.test(inst.createdBy)
          return (
            <Fragment key={i}>
              {i > 0 && <span style={{ color: 'var(--accent-3)' }}>→</span>}
              <VizBox title={inst.hash} sub={inst.createdBy} active={active} bad={broken && /Real/i.test(inst.hash) && active} minWidth={112} />
              {isProxy && <span className="pix-tag" style={{ background: hit ? 'var(--good)' : 'var(--bg-2)', color: hit ? 'var(--paper)' : 'var(--ink-3)', fontSize: 10 }}>cache{hit ? ' HIT' : ''}</span>}
            </Fragment>
          )
        })}
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : (broken ? 'каждый запрос — дорогой вызов' : 'повтор берётся из кэша')}
                 color={broken ? 'var(--bad)' : hit ? 'var(--good)' : stepTone(frame)} />
    </div>
  )
}

// Observer (live) — subject → subscribers; highlights the one being notified.
export function ObserverLiveViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  return (
    <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">{broken ? 'hardcoded calls · tight coupling' : 'subject → subscribers'}</div>
      <VizBox title="NewsFeed" sub="subject" active={/NewsFeed/i.test(activeName || '')} bad={broken && /NewsFeed/i.test(activeName || '')} minWidth={150} />
      <span style={{ color: broken ? 'var(--bad)' : 'var(--accent-3)' }}>{broken ? '↓ new …Svc()' : '↓ notify *'}</span>
      <div className="row gap-8 wrap center" style={{ maxWidth: 460 }}>
        {instances.map((s, i) => <VizBox key={i} title={s.hash} sub={s.createdBy} active={s.hash === activeName} bad={broken} />)}
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : (broken ? 'подписчики зашиты в код' : 'все подписчики уведомлены')}
                 color={broken ? 'var(--bad)' : stepTone(frame)} />
    </div>
  )
}

// Command — history stack of commands + undo indicator.
export function CommandViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  const isUndo = /undo/i.test(frame?.action || '')
  return (
    <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">{broken ? 'direct calls · no history' : 'command stack + undo'}</div>
      <div className="col gap-4 center">
        {instances.map((c, i) => {
          const active = c.hash === activeName
          return <VizBox key={i} title={c.hash} sub={c.createdBy} active={active} bad={broken || (active && isUndo)} minWidth={220} />
        })}
      </div>
      {!broken && <span className="tiny" style={{ color: isUndo ? 'var(--bad)' : 'var(--accent-3)' }}>{isUndo ? '↩ undo last' : '↧ push to history'}</span>}
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : (broken ? 'нечем отменить' : 'история команд → undo')}
                 color={broken ? 'var(--bad)' : stepTone(frame)} />
    </div>
  )
}

// Iterator — cursor moving along a collection.
export function IteratorViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const cursor = frame?.idx ?? -1
  return (
    <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">{broken ? 'client → array[i] (coupled)' : 'cursor over collection'}</div>
      <div className="row gap-6 center">
        {instances.map((e, i) => {
          const active = i === cursor
          return (
            <div key={i} className="col center gap-2">
              <VizBox title={e.hash} sub={broken ? `[${i}]` : ''} active={active} bad={broken && active} minWidth={72} />
              <span style={{ height: 14, color: active ? (broken ? 'var(--bad)' : 'var(--accent-3)') : 'transparent' }}>▲</span>
            </div>
          )
        })}
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : (broken ? 'обход завязан на структуру' : 'обход без знания структуры')}
                 color={broken ? 'var(--bad)' : stepTone(frame)} />
    </div>
  )
}

// Mediator — star through a mediator (good) vs full mesh (bad).
export function MediatorViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  return (
    <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">{broken ? 'everyone ↔ everyone (mesh)' : 'star via mediator'}</div>
      {!broken && <VizBox title="ChatRoom" sub="mediator" active={/room|chat/i.test(activeName || '')} minWidth={150} />}
      {!broken && <span style={{ color: 'var(--accent-3)' }}>↑↓ N связей</span>}
      <div className="row gap-8 wrap center" style={{ maxWidth: 420 }}>
        {instances.map((u, i) => <VizBox key={i} title={u.hash} sub={u.createdBy} active={u.hash === activeName} bad={broken} />)}
      </div>
      {broken && <span className="tiny" style={{ color: 'var(--bad)' }}>каждый соединён с каждым</span>}
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : (broken ? 'N×(N−1) связей' : 'каждый знает только посредника')}
                 color={broken ? 'var(--bad)' : stepTone(frame)} />
    </div>
  )
}

// Memento — ribbon of snapshots; highlights the restored one.
export function MementoViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  return (
    <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">{broken ? 'snapshot shares reference' : 'snapshots ribbon'}</div>
      <div className="row gap-8 center wrap">
        {instances.map((s, i) => {
          const active = s.hash === activeName
          return (
            <Fragment key={i}>
              {i > 0 && <span style={{ color: 'var(--ink-3)' }}>→</span>}
              <div className="pix-frame col center" style={{
                minWidth: 100, padding: 8, gap: 2,
                background: active ? 'var(--accent-3)' : 'var(--paper)', color: active ? 'var(--paper)' : 'var(--ink)',
                border: `var(--px) solid ${broken ? 'var(--bad)' : 'var(--line)'}`,
              }}>
                <span className="small bold" style={{ color: active ? 'var(--paper)' : 'var(--ink)' }}>{s.hash}</span>
                <span className="tiny pix-mono" style={{ color: active ? 'var(--paper)' : 'var(--ink-3)' }}>{s.createdBy}</span>
              </div>
            </Fragment>
          )
        })}
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : (broken ? 'снимок «протёк» вместе с состоянием' : 'restore возвращает точное прошлое')}
                 color={broken ? 'var(--bad)' : stepTone(frame)} />
    </div>
  )
}

// State — states in a row with transitions; highlights the current one.
export function StateViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  return (
    <div className="col gap-12 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">{broken ? 'switch(state)' : 'state machine'}</div>
      <div className="row gap-4 center wrap" style={{ alignItems: 'center' }}>
        {instances.map((s, i) => (
          <Fragment key={i}>
            {i > 0 && <span style={{ color: 'var(--accent-3)' }}>→</span>}
            <VizBox title={s.hash} sub="state" active={s.hash === activeName} bad={broken && s.hash === activeName} minWidth={96} />
          </Fragment>
        ))}
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : (broken ? 'все переходы в одном switch' : 'переходы в классах состояний')}
                 color={broken ? 'var(--bad)' : stepTone(frame)} />
    </div>
  )
}

// Strategy — context with a swappable strategy slot.
export function StrategyViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  return (
    <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">{broken ? 'switch(type)' : 'swappable strategy'}</div>
      <VizBox title="Cart" sub="context" minWidth={130} />
      <span style={{ color: broken ? 'var(--bad)' : 'var(--accent-3)' }}>↧ {broken ? 'switch' : 'setDiscount(...)'}</span>
      <div className="row gap-8 wrap center" style={{ maxWidth: 470 }}>
        {instances.map((s, i) => <VizBox key={i} title={s.hash} sub={s.createdBy} active={s.hash === activeName} bad={broken && s.hash === activeName} />)}
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : (broken ? 'алгоритм зашит в switch' : 'алгоритмы взаимозаменяемы')}
                 color={broken ? 'var(--bad)' : stepTone(frame)} />
    </div>
  )
}

// Template Method — vertical pipeline; hooks accented, fixed steps neutral.
export function TemplateMethodViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  const reveal = frame?.reveal ?? instances.length
  const shown = instances.slice(0, Math.max(1, reveal))
  return (
    <div className="col gap-8 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">{broken ? 'copied algorithm' : 'skeleton + hooks'}</div>
      <div className="col gap-2 center">
        {shown.map((s, i) => {
          const active = s.hash === activeName
          const hook = s.createdBy === 'hook'
          let bg = active ? 'var(--accent-3)' : hook ? 'var(--accent-2)' : 'var(--paper)'
          let fg = (active || hook) ? 'var(--paper)' : 'var(--ink)'
          if (broken) { bg = active ? 'var(--bad)' : 'var(--paper)'; fg = active ? 'var(--paper)' : 'var(--ink)' }
          return (
            <div key={i} className="row center" style={{
              minWidth: 220, padding: '5px 10px', gap: 8, background: bg, color: fg,
              border: `var(--px) solid ${broken ? 'var(--bad)' : 'var(--line)'}`,
            }}>
              <span className="tiny" style={{ opacity: 0.7 }}>{i + 1}</span>
              <span className="small bold" style={{ color: fg }}>{s.hash}</span>
              {!broken && <span className="tiny" style={{ marginLeft: 'auto', color: fg }}>{hook ? 'hook' : 'fixed'}</span>}
            </div>
          )
        })}
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : (broken ? 'шаг потерян при копипасте' : 'скелет общий, хуки варьируются')}
                 color={broken ? 'var(--bad)' : stepTone(frame)} />
    </div>
  )
}

// Visitor — a visitor visiting each element (double dispatch).
export function VisitorViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  return (
    <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">{broken ? 'instanceof ladder' : 'visitor · double dispatch'}</div>
      <span className="pix-tag" style={{ background: broken ? 'var(--bad)' : 'var(--accent-3)', color: 'var(--paper)' }}>{broken ? 'if instanceof …' : 'AreaVisitor'}</span>
      <span style={{ color: broken ? 'var(--bad)' : 'var(--accent-3)' }}>↓ visits</span>
      <div className="row gap-8 wrap center" style={{ maxWidth: 460 }}>
        {instances.map((e, i) => <VizBox key={i} title={e.hash} sub={e.createdBy} active={e.hash === activeName} bad={broken && e.hash === activeName} minWidth={130} />)}
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : (broken ? 'тип через instanceof' : 'новая операция = новый visitor')}
                 color={broken ? 'var(--bad)' : stepTone(frame)} />
    </div>
  )
}

// Interpreter — AST nodes evaluated in order with a running result.
export function InterpreterViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  if (broken) {
    return (
      <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
        <div className="tiny upper text-muted">ad-hoc string parse</div>
        <VizBox title={'"5 + 3 - 2"'} sub="split & loop" active bad minWidth={220} />
        <VizStatus text={frame ? `${frame.action} → ${frame.result}` : 'нет приоритетов и вложенности'} color="var(--bad)" />
      </div>
    )
  }
  return (
    <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">AST · recursive eval</div>
      <div className="row gap-6 wrap center" style={{ maxWidth: 480 }}>
        {instances.map((n, i) => (
          <Fragment key={i}>
            {i > 0 && <span style={{ color: 'var(--ink-3)' }}>·</span>}
            <VizBox title={n.hash} sub={n.createdBy} active={n.hash === activeName} minWidth={92} />
          </Fragment>
        ))}
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : 'грамматика в классах, дерево вычисления'} color={stepTone(frame)} />
    </div>
  )
}

// API Gateway — client → gateway → fan-out (good) vs chatty client (bad).
export function ApiGatewayViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  const client = instances.find(i => i.createdBy === 'client')
  const gateway = instances.find(i => i.createdBy === 'gateway')
  const services = instances.filter(i => i.createdBy === 'service')
  return (
    <div className="col gap-8 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">{broken ? 'chatty client · N round-trips' : 'client → gateway → fan-out'}</div>
      {client && <VizBox title={client.hash} sub="client" active={client.hash === activeName} minWidth={120} />}
      <span style={{ color: broken ? 'var(--bad)' : 'var(--accent-3)' }}>{broken ? '↓ ↓ ↓  (3 round-trips)' : '↓ 1 request'}</span>
      {gateway && (
        <>
          <VizBox title={gateway.hash} sub="gateway" active={gateway.hash === activeName} minWidth={150} />
          <span style={{ color: 'var(--accent-3)' }}>↓ fan-out</span>
        </>
      )}
      <div className="row gap-8 wrap center" style={{ maxWidth: 460 }}>
        {services.map((s, i) => <VizBox key={i} title={s.hash} sub="service" active={s.hash === activeName} bad={broken && s.hash === activeName} />)}
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : (broken ? 'клиент знает все сервисы' : 'агрегация на шлюзе')}
                 color={broken ? 'var(--bad)' : stepTone(frame)} />
    </div>
  )
}

// Backend for Frontend — a tailored BFF per client (good) vs one general API (bad).
export function BffViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  return (
    <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">{broken ? 'one general API · overfetch' : 'a BFF per client'}</div>
      <div className="row gap-12 wrap center" style={{ maxWidth: 500 }}>
        {instances.map((b, i) => <VizBox key={i} title={b.hash} sub={b.createdBy} active={b.hash === activeName} bad={broken && b.hash === activeName} minWidth={150} />)}
      </div>
      {broken && <span className="tiny" style={{ color: 'var(--bad)' }}>мобильный использует 2 из 5 полей</span>}
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : (broken ? 'overfetch по трафику' : 'каждый клиент получает ровно нужное')}
                 color={broken ? 'var(--bad)' : stepTone(frame)} />
    </div>
  )
}

// Strangler Fig — routes migrate one by one (good) vs big-bang switch (bad).
export function StranglerFigViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  if (broken) {
    return (
      <div className="col gap-12 center" style={{ height: '100%', justifyContent: 'center' }}>
        <div className="tiny upper text-muted">big-bang rewrite</div>
        <div className="row gap-8 center" style={{ alignItems: 'center' }}>
          {instances.map((b, i) => (
            <Fragment key={i}>
              {i > 0 && <span style={{ color: 'var(--bad)' }}>⇒ all at once</span>}
              <VizBox title={b.hash} sub={b.createdBy} active={b.hash === activeName} bad minWidth={130} />
            </Fragment>
          ))}
        </div>
        <VizStatus text={frame ? `${frame.action} → ${frame.result}` : 'переключение всего разом — риск'} color="var(--bad)" />
      </div>
    )
  }
  const migrated = frame?.reveal ?? instances.length
  return (
    <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">incremental migration · {migrated}/{instances.length} → new</div>
      <div className="row gap-8 wrap center" style={{ maxWidth: 480 }}>
        {instances.map((rt, i) => {
          const isNew = i < migrated
          const active = rt.hash === activeName
          const fg = (active || isNew) ? 'var(--paper)' : 'var(--ink)'
          return (
            <div key={i} className="pix-frame col center" style={{
              minWidth: 96, padding: '8px 10px', gap: 2,
              background: active ? 'var(--accent-3)' : isNew ? 'var(--good)' : 'var(--paper)',
              color: fg, border: 'var(--px) solid var(--line)',
            }}>
              <span className="small bold" style={{ color: fg }}>{rt.hash}</span>
              <span className="tiny" style={{ color: (active || isNew) ? 'var(--paper)' : 'var(--ink-3)' }}>{isNew ? 'new' : 'legacy'}</span>
            </div>
          )
        })}
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : 'маршруты переключаются по одному'} color={stepTone(frame)} />
    </div>
  )
}

// Service Discovery — registry resolves live instances (good) vs hardcoded host (bad).
export function ServiceDiscoveryViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  if (broken) {
    return (
      <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
        <div className="tiny upper text-muted">hardcoded host:port</div>
        <VizBox title="Client" sub="hardcoded addr" minWidth={130} />
        <span style={{ color: 'var(--bad)' }}>↓ call(10.0.0.1:8080)</span>
        {instances.map((s, i) => <VizBox key={i} title={s.hash} sub={s.createdBy} active bad minWidth={160} />)}
        <VizStatus text={frame ? `${frame.action} → ${frame.result}` : 'переезд инстанса рвёт связь'} color="var(--bad)" />
      </div>
    )
  }
  return (
    <div className="col gap-8 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">registry → resolve → instances</div>
      <VizBox title="Registry" sub="service registry" minWidth={160} />
      <span style={{ color: 'var(--accent-3)' }}>↓ resolve(order-service)</span>
      <div className="row gap-8 wrap center" style={{ maxWidth: 440 }}>
        {instances.map((s, i) => {
          const active = s.hash === activeName
          const down = active && /down/i.test(frame?.result || '')
          return <VizBox key={i} title={s.hash} sub={s.createdBy} active={active && !down} bad={down} minWidth={150} />
        })}
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : 'клиент резолвит живой адрес'} color={stepTone(frame)} />
    </div>
  )
}

// Sidecar — app + sidecar in one pod (good) vs infra mixed into service (bad).
export function SidecarViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  if (broken) {
    const app = instances[0]
    return (
      <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
        <div className="tiny upper text-muted">infra mixed into service</div>
        <div className="pix-frame col center" style={{
          minWidth: 240, padding: 12, gap: 4,
          border: 'var(--px) solid var(--bad)', boxShadow: 'var(--pxr) var(--pxr) 0 0 var(--bad)',
        }}>
          <span className="small bold">{app?.hash || 'OrderApp'}</span>
          <span className="tiny text-muted">biz + TLS + rate-limit + metrics</span>
        </div>
        <VizStatus text={frame ? `${frame.action} → ${frame.result}` : 'инфра-код в каждом сервисе'} color="var(--bad)" />
      </div>
    )
  }
  const app = instances.find(i => i.createdBy === 'business')
  const side = instances.find(i => i.createdBy === 'cross-cutting')
  return (
    <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">pod: app + sidecar</div>
      <div className="pix-frame row gap-8 center" style={{ padding: 10, background: 'var(--bg-2)', alignItems: 'center' }}>
        <VizBox title={app?.hash || 'OrderApp'} sub="business" active={!!app && app.hash === activeName} minWidth={130} />
        <span style={{ color: 'var(--accent-3)' }}>⇄</span>
        <VizBox title={side?.hash || 'Sidecar'} sub="cross-cutting" active={!!side && side.hash === activeName} minWidth={130} />
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : 'сквозное в сайдкаре, бизнес чистый'} color={stepTone(frame)} />
    </div>
  )
}

// Publish-Subscribe — broker fan-out (good) vs sync chain with cascade (bad).
export function PubSubViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  if (broken) {
    return (
      <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
        <div className="tiny upper text-muted">sync chain · cascade fail</div>
        <VizBox title="Publisher" sub="sync calls" minWidth={130} />
        <div className="row gap-4 center wrap" style={{ alignItems: 'center' }}>
          {instances.map((c, i) => {
            const role = c.createdBy // ok | fail | skipped
            const bg = role === 'fail' ? 'var(--bad)' : role === 'ok' ? 'var(--good)' : 'var(--bg-2)'
            const skipped = role === 'skipped'
            const fg = skipped ? 'var(--ink)' : 'var(--paper)'
            const linkBad = i > 0 && (instances[i - 1]?.createdBy === 'fail' || skipped)
            return (
              <Fragment key={i}>
                {i > 0 && <span style={{ color: linkBad ? 'var(--bad)' : 'var(--ink-3)' }}>→</span>}
                <div className="pix-frame col center" style={{
                  minWidth: 120, padding: 8, gap: 2, background: bg, color: fg,
                  border: 'var(--px) solid var(--line)', opacity: skipped ? 0.5 : 1,
                }}>
                  <span className="small bold" style={{ color: fg }}>{c.hash}</span>
                  <span className="tiny" style={{ color: skipped ? 'var(--ink-3)' : 'var(--paper)' }}>{role}</span>
                </div>
              </Fragment>
            )
          })}
        </div>
        <VizStatus text={frame ? `${frame.action} → ${frame.result}` : 'сбой одного обрывает доставку'} color="var(--bad)" />
      </div>
    )
  }
  return (
    <div className="col gap-8 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">broker → fan-out</div>
      <VizBox title="Broker" sub="event bus" active={/broker/i.test(activeName || '')} minWidth={150} />
      <span style={{ color: 'var(--accent-3)' }}>↓ fan-out</span>
      <div className="row gap-8 wrap center" style={{ maxWidth: 460 }}>
        {instances.map((s, i) => <VizBox key={i} title={s.hash} sub="subscriber" active={s.hash === activeName} />)}
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : 'все подписчики получают независимо'} color={stepTone(frame)} />
    </div>
  )
}

// Saga — steps with compensations (good) vs partial commit (bad).
export function SagaViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  return (
    <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">{broken ? 'partial commit · inconsistent' : 'saga + compensations'}</div>
      <div className="row gap-4 center wrap" style={{ alignItems: 'center' }}>
        {instances.map((s, i) => {
          const active = s.hash === activeName
          const role = s.createdBy // committed | compensated | failed
          let bg = 'var(--paper)', fg = 'var(--ink)'
          if (role === 'compensated') { bg = 'var(--good)'; fg = 'var(--paper)' }
          else if (role === 'committed' || role === 'failed') { bg = 'var(--bad)'; fg = 'var(--paper)' }
          if (active) { bg = 'var(--accent-3)'; fg = 'var(--paper)' }
          const tag = role === 'compensated' ? '↩ rolled back' : role === 'committed' ? '⚠ committed' : role === 'failed' ? '✗ failed' : ''
          return (
            <Fragment key={i}>
              {i > 0 && <span style={{ color: 'var(--ink-3)' }}>→</span>}
              <div className="pix-frame col center" style={{ minWidth: 120, padding: '8px 10px', gap: 2, background: bg, color: fg, border: 'var(--px) solid var(--line)' }}>
                <span className="small bold" style={{ color: fg }}>{s.hash}</span>
                <span className="tiny" style={{ color: fg }}>{tag}</span>
              </div>
            </Fragment>
          )
        })}
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : (broken ? 'ранние шаги закоммичены — рассинхрон' : 'компенсации откатили завершённые шаги')}
                 color={broken ? 'var(--bad)' : stepTone(frame)} />
    </div>
  )
}

// CQRS — separate write/read sides (good) vs one model (bad).
export function CqrsViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  if (broken) {
    const m = instances[0]
    return (
      <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
        <div className="tiny upper text-muted">one model · reads + writes</div>
        <VizBox title={m?.hash || 'Model'} sub="reads ⇄ writes" active bad minWidth={200} />
        <span className="tiny" style={{ color: 'var(--bad)' }}>запрос пересчитывает по всему логу</span>
        <VizStatus text={frame ? `${frame.action} → ${frame.result}` : 'чтение конкурирует с записью'} color="var(--bad)" />
      </div>
    )
  }
  const write = instances.find(i => i.createdBy === 'commands')
  const read = instances.find(i => i.createdBy === 'queries')
  return (
    <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">commands | queries</div>
      <div className="row gap-12 center" style={{ alignItems: 'center' }}>
        <VizBox title={write?.hash || 'WriteSide'} sub="commands" active={!!write && write.hash === activeName} minWidth={140} />
        <span style={{ color: 'var(--accent-3)' }}>→ project →</span>
        <VizBox title={read?.hash || 'ReadSide'} sub="queries" active={!!read && read.hash === activeName} minWidth={140} />
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : 'чтение из готовой проекции'} color={stepTone(frame)} />
    </div>
  )
}

// Event Sourcing — append-only event log (good) vs current value only (bad).
export function EventSourcingViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  if (broken) {
    const m = instances[0]
    return (
      <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
        <div className="tiny upper text-muted">current value only · no history</div>
        <VizBox title={m?.hash || 'balance'} sub="mutable, history lost" active bad minWidth={190} />
        <VizStatus text={frame ? `${frame.action} → ${frame.result}` : 'прошлое стёрто — нет replay'} color="var(--bad)" />
      </div>
    )
  }
  const reveal = frame?.reveal ?? instances.length
  const shown = instances.slice(0, Math.max(1, reveal))
  return (
    <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">append-only event log</div>
      <div className="row gap-6 center wrap" style={{ maxWidth: 480 }}>
        {shown.map((e, i) => {
          const active = e.hash === activeName
          const fg = active ? 'var(--paper)' : 'var(--ink)'
          return (
            <Fragment key={i}>
              {i > 0 && <span style={{ color: 'var(--ink-3)' }}>→</span>}
              <div className="pix-frame col center" style={{ minWidth: 110, padding: 8, gap: 2, background: active ? 'var(--accent-3)' : 'var(--paper)', color: fg, border: 'var(--px) solid var(--line)' }}>
                <span className="small bold" style={{ color: fg }}>{e.hash}</span>
                <span className="tiny" style={{ color: active ? 'var(--paper)' : 'var(--accent)' }}>{e.createdBy}</span>
              </div>
            </Fragment>
          )
        })}
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : 'состояние = свёртка событий'} color={stepTone(frame)} />
    </div>
  )
}

// Transactional Outbox — atomic db+outbox → relay → broker (good) vs dual write (bad).
export function OutboxViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  return (
    <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">{broken ? 'dual write · message lost' : 'db+outbox → relay → broker'}</div>
      <div className="row gap-4 center wrap" style={{ alignItems: 'center' }}>
        {instances.map((b, i) => {
          const active = b.hash === activeName
          const isBroker = /broker/i.test(b.hash)
          return (
            <Fragment key={i}>
              {i > 0 && <span style={{ color: broken ? 'var(--bad)' : 'var(--accent-3)' }}>{broken && i === 1 ? '✗ crash' : '→'}</span>}
              <VizBox title={b.hash} sub={b.createdBy} active={active} bad={broken && isBroker} minWidth={130} />
            </Fragment>
          )
        })}
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : (broken ? 'сообщение потеряно при падении' : 'сообщение доставлено надёжно')}
                 color={broken ? 'var(--bad)' : stepTone(frame)} />
    </div>
  )
}

// Circuit Breaker — state machine CLOSED/OPEN/HALF_OPEN (good) vs no breaker (bad).
export function CircuitBreakerViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  if (broken) {
    const d = instances[0]
    return (
      <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
        <div className="tiny upper text-muted">no breaker · every call hits</div>
        <VizBox title={d?.hash || 'Downstream'} sub="failing · timeouts" active bad minWidth={190} />
        <VizStatus text={frame ? `${frame.action} → ${frame.result}` : 'каскад таймаутов'} color="var(--bad)" />
      </div>
    )
  }
  return (
    <div className="col gap-12 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">circuit breaker · state machine</div>
      <div className="row gap-4 center wrap" style={{ alignItems: 'center' }}>
        {instances.map((s, i) => (
          <Fragment key={i}>
            {i > 0 && <span style={{ color: 'var(--accent-3)' }}>→</span>}
            <VizBox title={s.hash} sub="state" active={s.hash === activeName} minWidth={110} />
          </Fragment>
        ))}
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : 'OPEN → fast-fail, HALF_OPEN → проба'} color={stepTone(frame)} />
    </div>
  )
}

// Retry — attempts with growing backoff (good) vs single attempt (bad).
export function RetryViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  return (
    <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">{broken ? 'no retry · transient fails' : 'retry with exponential backoff'}</div>
      <div className="row gap-3 center wrap" style={{ alignItems: 'center' }}>
        {instances.map((a, i) => {
          const ok = /ok/i.test(a.createdBy)
          const active = a.hash === activeName
          const bg = active ? 'var(--accent-3)' : ok ? 'var(--good)' : 'var(--bad)'
          return (
            <Fragment key={i}>
              {i > 0 && <span className="tiny text-muted">⟳</span>}
              <div className="pix-frame col center" style={{ minWidth: 110, padding: '8px 10px', gap: 2, background: bg, color: 'var(--paper)', border: 'var(--px) solid var(--line)' }}>
                <span className="small bold" style={{ color: 'var(--paper)' }}>{a.hash}</span>
                <span className="tiny" style={{ color: 'var(--paper)' }}>{a.createdBy}</span>
              </div>
            </Fragment>
          )
        })}
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : (broken ? 'транзиентный сбой роняет запрос' : 'паузы растут: 0 → 100 → 200ms')}
                 color={broken ? 'var(--bad)' : stepTone(frame)} />
    </div>
  )
}

// Bulkhead — isolated pools (good) vs one shared pool starvation (bad).
export function BulkheadViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  return (
    <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">{broken ? 'shared pool · starvation' : 'isolated pools (bulkheads)'}</div>
      <div className="row gap-12 wrap center" style={{ maxWidth: 480 }}>
        {instances.map((p, i) => {
          const full = /full|reject/i.test(p.createdBy)
          const active = p.hash === activeName
          const bg = active ? 'var(--accent-3)' : broken ? 'var(--bad)' : full ? 'var(--accent-2)' : 'var(--good)'
          return (
            <div key={i} className="pix-frame col center" style={{ minWidth: 160, padding: '10px 12px', gap: 2, background: bg, color: 'var(--paper)', border: 'var(--px) solid var(--line)' }}>
              <span className="small bold" style={{ color: 'var(--paper)' }}>{p.hash}</span>
              <span className="tiny" style={{ color: 'var(--paper)' }}>{p.createdBy}</span>
            </div>
          )
        })}
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : (broken ? 'медленная зависимость исчерпала пул' : 'насыщение одной не задевает другие')}
                 color={broken ? 'var(--bad)' : stepTone(frame)} />
    </div>
  )
}

// Rate Limiter — token bucket allows N, rejects excess (good) vs no limit (bad).
export function RateLimiterViz({ result, frame }) {
  const instances = result?.instances || []
  const broken = result?.broken
  const activeName = frame?.actor
  return (
    <div className="col gap-10 center" style={{ height: '100%', justifyContent: 'center' }}>
      <div className="tiny upper text-muted">{broken ? 'no limit · overload' : 'token bucket · 3/window'}</div>
      <div className="row gap-3 center wrap" style={{ maxWidth: 480 }}>
        {instances.map((q, i) => {
          const allowed = /allowed/i.test(q.createdBy)
          const active = q.hash === activeName
          const bg = active ? 'var(--accent-3)' : broken ? 'var(--bad)' : allowed ? 'var(--good)' : 'var(--bad)'
          return (
            <div key={i} className="pix-frame col center" style={{ minWidth: 84, padding: '6px 8px', gap: 2, background: bg, color: 'var(--paper)', border: 'var(--px) solid var(--line)' }}>
              <span className="small bold" style={{ color: 'var(--paper)' }}>{q.hash}</span>
              <span className="tiny" style={{ color: 'var(--paper)' }}>{broken ? 'passed' : (allowed ? 'allowed' : '429')}</span>
            </div>
          )
        })}
      </div>
      <VizStatus text={frame ? `${frame.action} → ${frame.result}` : (broken ? 'все запросы прошли — перегрузка' : 'лишние запросы → 429')}
                 color={broken ? 'var(--bad)' : stepTone(frame)} />
    </div>
  )
}

// ─── Viz registry: kind → render fn (uniform props {result, frame, step, cmd}) ───
export const VIZ = {
  observer: (p) => <ObserverLiveViz {...p} />,
  singleton: ({ result, frame }) =>
    <SingletonViz instances={result?.instances || []} revealed={frame?.viz ?? 0} broken={result?.broken} />,
  chain: ({ result, frame }) =>
    <ChainViz handlers={result?.handlers || []} activeIndex={frame?.activeIndex ?? -1}
              decision={frame?.decision || 'enter'} broken={result?.broken} />,
  'factory-method': (p) => <FactoryMethodViz {...p} />,
  'abstract-factory': (p) => <AbstractFactoryViz {...p} />,
  builder: (p) => <BuilderViz {...p} />,
  prototype: (p) => <PrototypeViz {...p} />,
  adapter: (p) => <AdapterViz {...p} />,
  bridge: (p) => <BridgeViz {...p} />,
  composite: (p) => <CompositeViz {...p} />,
  decorator: (p) => <DecoratorViz {...p} />,
  facade: (p) => <FacadeViz {...p} />,
  flyweight: (p) => <FlyweightViz {...p} />,
  proxy: (p) => <ProxyViz {...p} />,
  command: (p) => <CommandViz {...p} />,
  iterator: (p) => <IteratorViz {...p} />,
  mediator: (p) => <MediatorViz {...p} />,
  memento: (p) => <MementoViz {...p} />,
  state: (p) => <StateViz {...p} />,
  strategy: (p) => <StrategyViz {...p} />,
  'template-method': (p) => <TemplateMethodViz {...p} />,
  visitor: (p) => <VisitorViz {...p} />,
  interpreter: (p) => <InterpreterViz {...p} />,
  'api-gateway': (p) => <ApiGatewayViz {...p} />,
  'backend-for-frontend': (p) => <BffViz {...p} />,
  'strangler-fig': (p) => <StranglerFigViz {...p} />,
  'service-discovery': (p) => <ServiceDiscoveryViz {...p} />,
  sidecar: (p) => <SidecarViz {...p} />,
  'publish-subscribe': (p) => <PubSubViz {...p} />,
  saga: (p) => <SagaViz {...p} />,
  cqrs: (p) => <CqrsViz {...p} />,
  'event-sourcing': (p) => <EventSourcingViz {...p} />,
  'transactional-outbox': (p) => <OutboxViz {...p} />,
  'circuit-breaker': (p) => <CircuitBreakerViz {...p} />,
  retry: (p) => <RetryViz {...p} />,
  bulkhead: (p) => <BulkheadViz {...p} />,
  'rate-limiter': (p) => <RateLimiterViz {...p} />,
}

export function renderViz(kind, props) {
  const fn = VIZ[kind]
  return fn ? fn(props) : VIZ.singleton(props)
}
