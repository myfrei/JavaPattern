import { useEffect, useState } from 'react'

/**
 * Переключатель светлой/тёмной темы.
 * Тема хранится в localStorage и проставляется в data-theme на <html>,
 * откуда её подхватывают CSS-переменные в pixel.css.
 */
const STORAGE_KEY = 'jp-theme'

function getInitialTheme() {
  const saved = localStorage.getItem(STORAGE_KEY)
  return saved === 'light' || saved === 'dark' ? saved : 'dark'
}

export default function ThemeToggle() {
  const [theme, setTheme] = useState(getInitialTheme)

  useEffect(() => {
    document.documentElement.setAttribute('data-theme', theme)
    localStorage.setItem(STORAGE_KEY, theme)
  }, [theme])

  return (
    <button
      className="btn theme-toggle"
      onClick={() => setTheme(t => (t === 'dark' ? 'light' : 'dark'))}
    >
      {theme === 'dark' ? '☀ СВЕТЛАЯ ТЕМА' : '☾ ТЁМНАЯ ТЕМА'}
    </button>
  )
}
