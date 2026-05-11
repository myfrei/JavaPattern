import { Routes, Route, Navigate } from 'react-router-dom'
import Sidebar from './components/Sidebar.jsx'
import Home from './pages/Home.jsx'
import GroupPage from './pages/GroupPage.jsx'
import MicroservicesPage from './pages/MicroservicesPage.jsx'
import SingletonPage from './pages/SingletonPage.jsx'
import ComingSoonPage from './pages/ComingSoonPage.jsx'

export default function App() {
  return (
    <div className="app">
      <Sidebar />
      <main className="main">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/design" element={<Navigate to="/design/creational" replace />} />
          <Route path="/design/:group" element={<GroupPage />} />
          <Route path="/design/creational/singleton" element={<SingletonPage />} />
          <Route path="/microservices" element={<MicroservicesPage />} />
          <Route path="*" element={<ComingSoonPage />} />
        </Routes>
      </main>
    </div>
  )
}
