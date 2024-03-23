import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import Layout from './components/shared/Layout'
import Register from './pages/Register'
import Dashboard from './pages/Dashboard'
import Movies from './pages/Movies'
import MovieDetail from './pages/MovieDetail'
import Users from './pages/Users'
import UserDetail from './pages/UserDetail'
import Article from './pages/Article'
import ArticleDetail from './pages/ArticleDetail'
import Category from './pages/Category'
import CategoryDetail from './pages/CategoryDetail'
import History from './pages/History'
import Episodes from './pages/Episodes'
import EpisodeDetail from './pages/EpisodeDetail'
import TestDND from "./pages/TestDND";
function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Layout />}>
                    <Route index element={<Dashboard />} />
                    <Route path="movies" element={<Movies />}/>
                    <Route path="movies/:id" element={<MovieDetail />}/>
                    <Route path="movies/:id/episodes" element={<Episodes />}/>
                    <Route path="movies/:id/episodes/:pos" element={<EpisodeDetail />}/>
                    <Route path="history/:id" element={<History />}/>
                    <Route path="users" element={<Users />}/>
                    <Route path="users/:id" element={<UserDetail />}/>
                    <Route path='categories' element={<Category />}></Route>
                    <Route path="categories/:id" element={<CategoryDetail />}></Route>
                    <Route path="articles" element={<Article/>}/>
                    <Route path="test" element={<TestDND/>}/>
                    <Route path="articles/:id" element={<ArticleDetail/>}/>
                    <Route path="*" element={<h1>404</h1>}/>  
                </Route>
                <Route path="/register" element={<Register />} />
            </Routes>
        </Router>
    )
}

export default App
