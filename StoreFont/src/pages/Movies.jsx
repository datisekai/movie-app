import ReactPaginate from "react-paginate";
import { useEffect, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import API_URL from "../url";
import Swal from "sweetalert2";
import ClipLoader from "react-spinners/ClipLoader";
import { FaPencilAlt } from "react-icons/fa";
import { AiFillDelete } from "react-icons/ai";

export default function Movies() {
  const [loading, setLoading] = useState(false);
  const location = useLocation();
  const navigate = useNavigate();
  const [movies, setMovies] = useState([]);
  const itemsPerPage = 10;
  // boilerplate for pagination
  const searchParams = new URLSearchParams(location.search);
  const pageParam = searchParams.get("page");
  const [page, setPage] = useState(pageParam ? parseInt(pageParam) : 1);
  const [totalEntries, setTotalEntries] = useState(0);
  // Simulate fetching items from another resources.
  // (This could be items from props; or items loaded in a local state
  // from an API endpoint with useEffect and useState)
  const pageCount = Math.ceil(totalEntries / itemsPerPage);

  // Invoke when user click to request another page.
  const handlePageClick = (event) => {
    const newOffset = (event.selected * itemsPerPage) % totalEntries;
    console.log(
      `User requested page number ${
        event.selected + 1
      }, which is offset ${newOffset}`
    );
    setPage(event.selected + 1);
    navigate(`/movies?page=${event.selected + 1}`);
  };

  //  end boilerplate

  useEffect(() => {
    setLoading(true);
    axios
      .get(`${API_URL}.film?page=${page}`)
      .then((res) => {
        console.log(res.data.data);
        setMovies(res.data.data);
        setTotalEntries(res.data.totalEntries);
        console.log("new");
      })
      .catch((err) => {
        console.log(err);
      })
      .finally(() => {
        setLoading(false);
      });
  }, [page]);

  function deleteFilm(id) {
    console.log("delete", id);
    Swal.fire({
      title: "Do you want to delete this film?",
      showDenyButton: true,
      confirmButtonText: "Yes",
      denyButtonText: "No",
    }).then((result) => {
      if (result.isConfirmed) {
        axios
          .delete(`${API_URL}.film/${id}`, {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
            },
          })
          .then((res) => {
            console.log(res.data);
            Swal.fire("Deleted!", "", "success");
            setMovies(movies.filter((movie) => movie.id !== id));
          })
          .catch((err) => {
            console.log(err);
          });
      }
    });
  }
  return (
    <div>
      <button
        onClick={() => navigate("create")}
        type="button"
        className="focus:outline-none text-white bg-purple-700 hover:bg-purple-800 focus:ring-4 focus:ring-purple-300 font-medium rounded-lg text-sm px-5 py-2.5 mb-2 dark:bg-purple-600 dark:hover:bg-purple-700 dark:focus:ring-purple-900"
      >
        Tạo phim mới
      </button>

      <div className="relative overflow-x-auto">
        {loading ? (
          <div className="flex items-center justify-center">
            <ClipLoader
              color={"f"}
              size="2rem"
              loading={loading}
              aria-label="Loading Spinner"
              data-testid="loader"
            />
          </div>
        ) : (
          <table className="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
            <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
              <tr>
                <th scope="col" className="px-6 py-3">
                  ID
                </th>
                <th scope="col" className="px-6 py-3">
                  Tiêu đề
                </th>
                <th scope="col" className="px-6 py-3">
                  Loại phim
                </th>
                <th scope="col" className="px-6 py-3">
                  Lượt xem
                </th>
                <th scope="col" className="px-6 py-3">
                  Tình trạng
                </th>
                <th scope="col" className="px-6 py-3">
                  Hiển thị
                </th>
                <th scope="col" className="px-6 py-3">
                  Hành động
                </th>
              </tr>
            </thead>
            <tbody>
              {movies.map((movie) => (
                <tr key={movie._id} className="">
                  <th
                    scope="row"
                    className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap "
                  >
                    {movie.id}
                  </th>
                  <td className="px-6 py-4">{movie.title}</td>
                  <td className="px-6 py-4">
                    {movie.type == "movie" ? "Phim ngắn" : "Dài tập"}
                  </td>
                  <td className="px-6 py-4">{movie.view}</td>
                  <td className="px-6 py-4">
                    {movie.status == "full"
                      ? "Đã hoàn thành"
                      : "Đang tiến hành"}
                  </td>
                  <td className="px-6 py-4">
                    {movie.is_active ? "Hoạt động" : "Ngừng"}
                  </td>
                  <td>
                    <div className="flex items-center gap-2">
                      <Link
                        type="button"
                        className="focus:outline-none text-white bg-purple-700 hover:bg-purple-800 focus:ring-4 focus:ring-purple-300 font-medium rounded-lg text-sm px-5 py-2.5 mb-2 dark:bg-purple-600 dark:hover:bg-purple-700 dark:focus:ring-purple-900"
                        to={`/movies/${movie.id}`}
                        state={movie}
                      >
                        <FaPencilAlt size={18} />
                      </Link>
                      <button
                        type="button"
                        className="focus:outline-none text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-900"
                        onClick={() => deleteFilm(movie.id)}
                      >
                        <AiFillDelete size={18} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}

        {/* pagination */}
        <ReactPaginate
          activeClassName="flex items-center justify-center px-4 h-10 text-blue-600 border border-gray-300 bg-blue-50 hover:bg-blue-100 hover:text-blue-700 dark:border-gray-700 dark:bg-gray-700 dark:text-white"
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
          forcePage={pageParam ? parseInt(pageParam) - 1 : 0}
          className="flex  w-full justify-center items-center py-2"
        />
      </div>
    </div>
  );
}
