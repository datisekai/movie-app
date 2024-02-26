import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import Layout from './components/shared/Layout'
import Register from './pages/Register'
import Dashboard from './pages/Dashboard'
import Movies from './pages/Movies'
import MovieDetail from './pages/MovieDetail'
import Users from './pages/Users'

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Layout />}>
                    <Route index element={<Dashboard />} />
                    <Route path="movies" element={<Movies />}/>
                    <Route path="movies/:id" element={<MovieDetail />}/>
                    <Route path="users" element={<Users />}/>
                    
                </Route>
                <Route path="/register" element={<Register />} />
            </Routes>
        </Router>
    )
}

export default App
