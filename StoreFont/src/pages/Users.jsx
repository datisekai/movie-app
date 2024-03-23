import ReactPaginate from "react-paginate";
import { useLocation, useNavigate,Link } from "react-router-dom";
import { useState } from "react";
const dummy = [
  {
    id: 1,
    email: "user1@example.com",
    fullname: "User One",
    fullname_search: "user one",
    password: "password123",
    plan: "free",
    role: "user",
    is_active: true,
    is_deleted: false,
    created_at: "2024-02-26",
    updated_at: "2024-02-26",
  },
  {
    id: 2,
    email: "user2@example.com",
    fullname: "User Two",
    fullname_search: "user two",
    password: "password456",
    plan: "premium",
    role: "user",
    is_active: true,
    is_deleted: false,
    created_at: "2024-02-26",
    updated_at: "2024-02-26",
  },
  {
    id: 3,
    email: "admin@example.com",
    fullname: "Admin",
    fullname_search: "admin",
    password: "admin123",
    plan: "premium",
    role: "admin",
    is_active: true,
    is_deleted: false,
    created_at: "2024-02-26",
    updated_at: "2024-02-26",
  },
];
function Users() {
  const itemsPerPage = 10;
  const [itemOffset, setItemOffset] = useState(0);
  const location = useLocation();
  const navigate = useNavigate();

  // Simulate fetching items from another resources.
  // (This could be items from props; or items loaded in a local state
  // from an API endpoint with useEffect and useState)
  const endOffset = itemOffset + itemsPerPage;
  console.log(`Loading items from ${itemOffset} to ${endOffset}`);
  const currentItems = dummy.slice(itemOffset, endOffset);
  const pageCount = Math.ceil(dummy.length / itemsPerPage);

  // Invoke when user click to request another page.
  const handlePageClick = (event) => {
    const newOffset = (event.selected * itemsPerPage) % dummy.length;
    console.log(
      `User requested page number ${event.selected}, which is offset ${newOffset}`
    );
    setItemOffset(newOffset);
  };
  return (
    <div className="relative overflow-x-auto">
      <table className="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
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
          {dummy.map((user) => (
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
                  >
                    Delete
                  </button>
                </td>
            </tr>
          ))}
        </tbody>
      </table>
      {/* pagination */}
      <ReactPaginate
        activeClassName="bg-red-600 hover:bg-red-700 transiton-colors duration-300 text-white	"
        previousClassName="border  border-black py-1 md:px-5  hover:text-black transition-all duration-300 md:text-xl text-xs px-2"
        nextClassName="border  border-black py-1 md:px-5  hover:text-black transition-all duration-300 md:text-xl text-xs px-2"
        disabledClassName="bg-gray-400  md:text-lg text-sm px-2 text-white"
        pageClassName="border md:text-xl text-xs  border-black p-1 md:px-4  hover:text-black transition-all duration-300 px-2"
        breakLabel="..."
        nextLabel="Next >"
        onPageChange={handlePageClick}
        pageRangeDisplayed={5}
        pageCount={pageCount}
        previousLabel="< Previous"
        renderOnZeroPageCount={null}
        className="flex gap-2  w-full justify-center items-center py-2"
      />
    </div>
  );
}

export default Users;
