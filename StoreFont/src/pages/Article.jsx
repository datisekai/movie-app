import { useNavigate,useLocation } from 'react-router-dom';
import ReactPaginate from "react-paginate";
import { useEffect, useState } from "react";
import {  Link } from "react-router-dom";
import Swal from "sweetalert2";
import API_URL from "../url";
import axios from "axios";
import ClipLoader from 'react-spinners/ClipLoader';
function Article() {
  const location = useLocation();
  const navigate = useNavigate();
  const [loading,setLoading] = useState(false)
  const itemsPerPage = 10;
  const [articles, setArticles] = useState([]);
  const searchParams = new URLSearchParams(location.search);
  const pageParam = searchParams.get("page");
  const [page,setPage] = useState(pageParam ? parseInt(pageParam) : 1);
  const [totalEntries, setTotalEntries] = useState(0);
  // Simulate fetching items from another resources.
  // (This could be items from props; or items loaded in a local state
  // from an API endpoint with useEffect and useState)
  const pageCount = Math.ceil(articles.length / itemsPerPage);

  // Invoke when user click to request another page.
  const handlePageClick = (event) => {
    const newOffset = (event.selected * itemsPerPage) % totalEntries;
    console.log(
      `User requested page number ${
        event.selected + 1
      }, which is offset ${newOffset}`
    );
    setPage(event.selected + 1);
    navigate(`/articles?page=${event.selected + 1}`)
  };
  useEffect(() => {
    setLoading(true)
    axios
      .get(`${API_URL}.article?page=${page}`)
      .then((res) => {
        console.log(res.data.data);
        setArticles(res.data.data);
        setTotalEntries(res.data.totalEntries)
      })
      .catch((err) => {
        console.log(err);
      }).finally(()=>{
        setLoading(false)
      })
  }, [page]);
  function deleteArticle(id) {
    console.log("delete", id);
    Swal.fire({
      title: "Do you want to delete this article?",
      showDenyButton: true,
      confirmButtonText: "Yes",
      denyButtonText: "No",
    }).then((result) => {
      if (result.isConfirmed) {
        axios
          .delete(`${API_URL}.article/${id}`, {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
            },
          })
          .then((res) => {
            console.log(res.data);
            Swal.fire("Deleted!", "", "success");
            setArticles(articles.filter((movie) => movie.id !== id));
          })
          .catch((err) => {
            console.log(err);
          });
      }
    });
  }
  return (
    <div className="relative overflow-x-auto">
      <button
        type="button"
        className="focus:outline-none text-white bg-purple-700 hover:bg-purple-800 focus:ring-4 focus:ring-purple-300 font-medium rounded-lg text-sm px-5 py-2.5 mb-2 dark:bg-purple-600 dark:hover:bg-purple-700 dark:focus:ring-purple-900"
      >
        <Link to={`create`}>Create new Article</Link>
      </button>
      {loading ? (<div className="flex items-center justify-center">
          <ClipLoader
            color={"f"}
            size="15rem"
            loading={loading}
            aria-label="Loading Spinner"
            data-testid="loader"
          /></div>):(<table className="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
          <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
            <tr>
              <th scope="col" className="px-6 py-3">
                Id
              </th>
              <th scope="col" className="px-6 py-3">
                Title
              </th>
              <th scope="col" className="px-6 py-3">
                Is Active
              </th>
              <th scope="col" className="px-6 py-3">
                Updated At
              </th>
              <th scope="col" className="px-6 py-3">
                Action
              </th>
            </tr>
          </thead>
          <tbody>
            {articles.map((article) => (
              <tr key={article.id} className="">
                <td className="px-6 py-4">{article.id}</td>
                <td className="px-6 py-4">{article.title}</td>
                <td className="px-6 py-4">{article.is_active.toString()}</td>
                <td className="px-6 py-4">{article.updated_at.toString()}</td>
  
                <td className="space-x-2 flex justify-start">
                  <Link
                    type="button"
                    className="focus:outline-none text-white bg-purple-700 hover:bg-purple-800 focus:ring-4 focus:ring-purple-300 font-medium rounded-lg text-sm px-5 py-2.5 mb-2 dark:bg-purple-600 dark:hover:bg-purple-700 dark:focus:ring-purple-900"
                    to={`/articles/${article.id}`}
                    state={article}
                  >
                    Details
                  </Link>
                  <button
                    type="button"
                    className="focus:outline-none text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-900"
                    onClick={() => deleteArticle(article.id)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>)}
      
      {/* pagination */}
      <ReactPaginate
        activeClassName="z-10 flex items-center justify-center px-4 h-10 leading-tight text-blue-600 border border-blue-300 bg-blue-50 hover:bg-blue-100 hover:text-blue-700 dark:border-gray-700 dark:bg-gray-700 dark:text-white"
        previousClassName="flex items-center justify-center px-4 h-10 ms-0 leading-tight text-gray-500 bg-white border border-e-0 border-gray-300 rounded-s-lg hover:bg-gray-100 hover:text-gray-700 dark:bg-gray-800 dark:border-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white"
        nextClassName="flex items-center justify-center px-4 h-10 leading-tight text-gray-500 bg-white border border-gray-300 rounded-e-lg hover:bg-gray-100 hover:text-gray-700 dark:bg-gray-800 dark:border-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white"
        pageClassName="flex items-center justify-center px-4 h-10 leading-tight  bg-white border border-gray-300 hover:bg-gray-100 hover:text-gray-700 dark:bg-gray-800 dark:border-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white"
        breakLabel="..."
        nextLabel=">"
        onPageChange={handlePageClick}
        pageRangeDisplayed={5}
        pageCount={pageCount}
        previousLabel="<"
        renderOnZeroPageCount={null}
        forcePage={pageParam ? parseInt(pageParam)-1 : 0}
        className="flex  w-full justify-center items-center py-2"
      />
    </div>
  );
}

export default Article;
