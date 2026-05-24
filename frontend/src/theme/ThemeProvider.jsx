import { createContext, useContext, useEffect, useState } from 'react'

const ThemeContext = createContext({ theme: 'light', toggle: () => {} })

const STORAGE_KEY = 'jp-theme'

function readInitial() {
  const saved = localStorage.getItem(STORAGE_KEY)
  if (saved === 'light' || saved === 'dark') return saved
  return 'light'
}

export function ThemeProvider({ children }) {
  const [theme, setTheme] = useState(readInitial)

  useEffect(() => {
    document.body.classList.remove('theme-light', 'theme-dark')
    document.body.classList.add(theme === 'dark' ? 'theme-dark' : 'theme-light')
    localStorage.setItem(STORAGE_KEY, theme)
  }, [theme])

  const toggle = () => setTheme(t => (t === 'dark' ? 'light' : 'dark'))

  return (
    <ThemeContext.Provider value={{ theme, toggle }}>
      {children}
    </ThemeContext.Provider>
  )
}

export const useTheme = () => useContext(ThemeContext)
