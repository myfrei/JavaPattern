import { useNavigate } from 'react-router-dom'
import { AppTopBar, PixelIcon } from '../components/Pixel.jsx'

export default function ComingSoon({ title = 'Скоро', sub = 'Этот раздел ещё в работе.' }) {
  const navigate = useNavigate()
  return (
    <div className="screen">
      <AppTopBar active="Patterns" />
      <div className="screen__body center p-24">
        <div className="pix-frame p-24 col gap-12 center-text" style={{ maxWidth: 460, alignItems: 'center' }}>
          <PixelIcon kind="micro" size={48} color="var(--accent-2)" />
          <div className="bold" style={{ fontSize: 22 }}>{title}</div>
          <div className="small text-muted">{sub}</div>
          <button className="pix-btn pix-btn--primary" onClick={() => navigate('/patterns/design')}>
            ← К паттернам проектирования
          </button>
        </div>
      </div>
    </div>
  )
}
