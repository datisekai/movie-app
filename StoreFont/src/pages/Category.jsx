import ReactPaginate from "react-paginate";
import { useLocation, useNavigate, Link } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import Swal from "sweetalert2";
import API_URL from "../url";
import ClipLoader from "react-spinners/ClipLoader";
function Category() {
  const location = useLocation();
  const navigate = useNavigate();
  const [categories, setCategories] = useState([])
  const itemsPerPage = 10;
  const searchParams = new URLSearchParams(location.search);
  const pageParam = searchParams.get("page");
  const [page,setPage] = useState(pageParam ? parseInt(pageParam) : 1);
  const [totalEntries,setTotalEntries] = useState(0)
  const [loading,setLoading] = useState(false)
  // Simulate fetching items from another resources.
  // (This could be items from props; or items loaded in a local state
  // from an API endpoint with useEffect and useState)
  const pageCount = Math.ceil(totalEntries / itemsPerPage);

  // Invoke when user click to request another page.
  const handlePageClick = (event) => {
    const newOffset = (event.selected * itemsPerPage) % totalEntries;
    console.log(
      `User requested page number ${event.selected + 1}, which is offset ${newOffset}`
    );
    setPage(event.selected+1);
    navigate(`/categories?page=${event.selected+1}`)
  };
  useEffect(() => {
    setLoading(true)
    axios
      .get(`${API_URL}.category?page=${page}`)
      .then((res) => {
        console.log(res.data.data);
        setCategories(res.data.data);
        setTotalEntries(res.data.totalEntries)
      })
      .catch((err) => {
        console.log(err);
      })
      .finally(()=>{
        setLoading(false)
      })
  }, [page]);
  function deleteCategory(id) {
    console.log("delete", id);
    Swal.fire({
      title: "Do you want to delete this category?",
      showDenyButton: true,
      confirmButtonText: "Yes",
      denyButtonText: "No",
    }).then((result) => {
      if (result.isConfirmed) {
        axios
          .delete(`${API_URL}.category/${id}`,{
            headers: {
              Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
            }
          })
          .then((res) => {
            console.log(res.data);
            Swal.fire("Deleted!", "", "success");
            setCategories(categories.filter((movie)=>movie.id!==id))
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
        <Link to={`create`}>Create new Category</Link>
      </button>
      {loading ? (
        <div className="flex items-center justify-center">
        <ClipLoader
          color={"f"}
          size="15rem"
          loading={loading}
          aria-label="Loading Spinner"
          data-testid="loader"
        />
        </div>
      ):(<table className="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
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
            Created At
          </th>
        </tr>
      </thead>
      <tbody>
        {categories.map((category) => (
          <tr key={category.id} className="">
            <td className="px-6 py-4">{category.id}</td>
            <td className="px-6 py-4">{category.title}</td>
            <td className="px-6 py-4">{category.is_active.toString()}</td>
            <td className="px-6 py-4">{category.updated_at}</td>
            <td className="px-6 py-4">{category.created_at}</td>
            <td className="space-x-2 flex justify-start">
              <Link
                type="button"
                className="focus:outline-none text-white bg-purple-700 hover:bg-purple-800 focus:ring-4 focus:ring-purple-300 font-medium rounded-lg text-sm px-5 py-2.5 mb-2 dark:bg-purple-600 dark:hover:bg-purple-700 dark:focus:ring-purple-900"
                to={`/categories/${category.id}`}
                state={category}
              >
                Details
              </Link>
              <button
                type="button"
                className="focus:outline-none text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-900"
                onClick={() => deleteCategory(category.id)}
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
          pageClassName="flex items-center justify-center px-4 h-10 leading-tight bg-white border border-gray-300 hover:bg-gray-100 hover:text-gray-700 dark:bg-gray-800 dark:border-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white"
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

export default Category;
