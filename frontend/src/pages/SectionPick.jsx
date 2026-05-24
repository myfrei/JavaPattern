import { useNavigate } from 'react-router-dom'
import { AppTopBar, PixelIcon } from '../components/Pixel.jsx'
import { SECTIONS } from '../data/patterns.js'

export default function SectionPick() {
  const navigate = useNavigate()
  return (
    <div className="screen">
      <AppTopBar active="Patterns" />
      <div className="screen__body p-24 gap-24" style={{ alignItems: 'center', justifyContent: 'center' }}>
        <div className="col gap-8 center-text" style={{ alignItems: 'center' }}>
          <div className="pix-tag">Pattern Library · v1.0</div>
          <div className="huge bold" style={{ fontSize: 32 }}>Что изучаем сегодня?</div>
          <div className="small text-muted">Выбери раздел — он определит навигацию и набор примеров</div>
        </div>

        <div className="row gap-32 resp-stack maxw" style={{ justifyContent: 'center', padding: '0 8px' }}>
          {SECTIONS.map((c) => (
            <div key={c.id} className="pix-frame pix-frame--hover col p-24 gap-16 clickable"
                 style={{ flex: 1, maxWidth: 440, minHeight: 320 }}
                 onClick={() => navigate(c.to)}>
              <div className="between">
                <span className="pix-tag" style={{ background: c.accent, color: 'var(--paper)' }}>{c.tag}</span>
                <PixelIcon kind="chev" size={20} />
              </div>
              <div className="row gap-12" style={{ alignItems: 'flex-start' }}>
                <div style={{ width: 64, height: 64, background: c.accent, border: 'var(--px) solid var(--line)' }} className="center">
                  <PixelIcon kind={c.icon} size={36} color="var(--paper)" />
                </div>
                <div className="col gap-4">
                  <div className="bold" style={{ fontSize: 22 }}>{c.title}</div>
                  <div className="text-muted small">{c.sub}</div>
                </div>
              </div>
              <hr className="pix-hr" />
              <div className="small text-ink2">{c.desc}</div>
              <div className="row gap-6 wrap">
                {c.preview.map(t => <span key={t} className="pix-tag">{t}</span>)}
              </div>
              <div className="f1" />
              <button className="pix-btn pix-btn--primary" style={{ alignSelf: 'flex-start', background: c.accent }}
                      onClick={(e) => { e.stopPropagation(); navigate(c.to) }}>
                Open library <PixelIcon kind="chev" size={14} color="var(--paper)" />
              </button>
            </div>
          ))}
        </div>

        <div className="row gap-16 small text-muted center" style={{ flexWrap: 'wrap' }}>
          <span>Не уверен с чего начать?</span>
          <a className="clickable" style={{ color: 'var(--accent)', textDecoration: 'underline' }}>Пройди quiz →</a>
        </div>
      </div>
    </div>
  )
}
