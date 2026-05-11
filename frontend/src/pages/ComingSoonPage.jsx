import { Link } from 'react-router-dom'

export default function ComingSoonPage() {
  return (
    <div className="pixel-box accent">
      <h1>COMING SOON</h1>
      <p>Этот паттерн пока в очереди на реализацию.</p>
      <p>Готов только Singleton — он показывает, как устроена связка фронт↔бэк.</p>
      <p><Link className="btn good" to="/design/creational/singleton">Открыть Singleton</Link></p>
    </div>
  )
}
