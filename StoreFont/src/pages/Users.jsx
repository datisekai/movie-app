import ReactPaginate from "react-paginate";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import API_URL from "../url";
import Swal from "sweetalert2";
import ClipLoader from "react-spinners/ClipLoader";
function Users() {
  const itemsPerPage = 10;
  const navigate = useNavigate()
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const pageParam = searchParams.get("page");
  const [page,setPage] = useState(pageParam ? parseInt(pageParam) : 1);
  const [totalEntries,setTotalEntries] = useState(0)
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
    navigate(`/users?page=${event.selected+1}`)
  };
  useEffect(() => {
    setLoading(true)
    // If the pageParam changes, update the page state accordingly
    if (pageParam) {
      setPage(parseInt(pageParam));
    }
    axios
      .get(`${API_URL}.user?page=${page}`)
      .then((res) => {
        setUsers(res.data.data);
        console.log("data", res.data);
        setTotalEntries(res.data.totalEntries)
      })
      .catch((err) => {
        console.log(err);
      }).finally(()=>{
        setLoading(false)
      })
  }, [page,pageParam]);
  function deleteUser(id){
    Swal.fire({
      title: "Do you want to delete this user?",
      showDenyButton: true,
      confirmButtonText: "Yes",
      denyButtonText: "No",
    }).then((result) => {
      if (result.isConfirmed) {
        axios
          .delete(`${API_URL}.user/${id}`,{
            headers: {
              Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
            }
          })
          .then((res) => {
            console.log(res.data);
            Swal.fire("Deleted!", "", "success");
            setUsers(users.filter((movie)=>movie.id!==id))
          })
          .catch((err) => {
            console.log(err);
          });
      }
    });
  }
  return (
    <div className=" overflow-x-auto">
     <button
        onClick={()=>navigate("create")}
        type="button"
        className="focus:outline-none text-white bg-purple-700 hover:bg-purple-800 focus:ring-4 focus:ring-purple-300 font-medium rounded-lg text-sm px-5 py-2.5 mb-2 dark:bg-purple-600 dark:hover:bg-purple-700 dark:focus:ring-purple-900"
      >
        Create new User
      </button>
      
      {loading ? (<div className="flex items-center justify-center">
          <ClipLoader
            color={"f"}
            size="15rem"
            loading={loading}
            aria-label="Loading Spinner"
            data-testid="loader"
          />
          </div>) :(<table className="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400 mt-5">
        <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
          <tr>
            <th scope="col" className="px-6 py-3">
              Id
            </th>
            <th scope="col" className="px-6 py-3">
              Email
            </th>
            <th scope="col" className="px-6 py-3">
              FullName
            </th>
            <th scope="col" className="px-6 py-3">
              isActive
            </th>
            <th scope="col" className="px-6 py-3">
              Action
            </th>
          </tr>
        </thead>
        <tbody>
          {users.map((user) => (
            <tr key={user.id} className="">
              <td className="px-6 py-4">{user.id}</td>
              <td className="px-6 py-4">{user.email}</td>
              <td className="px-6 py-4">{user.fullname}</td>
              <td className="px-6 py-4">{user.is_active.toString()}</td>
              <td className="space-x-2 flex justify-start">
                <Link
                  type="button"
                  className="focus:outline-none text-white bg-purple-700 hover:bg-purple-800 focus:ring-4 focus:ring-purple-300 font-medium rounded-lg text-sm px-5 py-2.5 mb-2 dark:bg-purple-600 dark:hover:bg-purple-700 dark:focus:ring-purple-900"
                  to={`/users/${user.id}`}
                  state={user}
                >
                  Details
                </Link>
                <button
                  type="button"
                  className="focus:outline-none text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-900"
                  onClick={()=>deleteUser(user.id)}
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
          className="flex  w-full justify-center items-center py-2"
          forcePage={pageParam ? parseInt(pageParam)-1 : 0}
        />
    </div>
  );
}

export default Users;
