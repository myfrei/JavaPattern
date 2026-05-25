import { Routes, Route, Navigate } from 'react-router-dom'
import SectionPick from './pages/SectionPick.jsx'
import Categories from './pages/Categories.jsx'
import PatternList from './pages/PatternList.jsx'
import PatternDetail from './pages/PatternDetail.jsx'
import Sandbox from './pages/Sandbox.jsx'
import ComingSoon from './pages/ComingSoon.jsx'

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<SectionPick />} />
      <Route path="/patterns" element={<Navigate to="/patterns/design" replace />} />
      <Route path="/patterns/:section" element={<Categories />} />
      <Route path="/patterns/:section/:category" element={<PatternList />} />
      <Route path="/patterns/:section/:category/:patternId" element={<PatternDetail />} />
      <Route path="/patterns/:section/:category/:patternId/sandbox" element={<Sandbox />} />
      <Route path="*" element={<ComingSoon title="404" sub="Такой страницы нет." />} />
    </Routes>
  )
}
