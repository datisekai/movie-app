import React from "react";
import ReactPaginate from "react-paginate";
import { useLocation, useNavigate } from "react-router-dom";
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
        <tbody></tbody>
      </table>
      {/* pagination */}
      <ReactPaginate
        activeClassName="bg-red-600 hover:bg-red-700 transiton-colors duration-300 	"
        previousClassName="border  border-black py-1 md:px-5  hover:text-black transition-all duration-300 md:text-xl text-xs px-2"
        nextClassName="border  border-black py-1 md:px-5  hover:text-black transition-all duration-300 md:text-xl text-xs px-2"
        disabledClassName="bg-gray-400  md:text-lg text-sm px-2 "
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
