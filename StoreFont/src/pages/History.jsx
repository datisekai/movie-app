const dummy = [
  {
    id: 1,
    type: "post",
    type_id: "1",
    action: "create",
    title: "First Post",
    user_id: 1,
  },
  {
    id: 2,
    type: "comment",
    type_id: "5",
    action: "update",
    title: "Updated Comment",
    user_id: 2,
  },
  {
    id: 3,
    type: "post",
    type_id: "2",
    action: "delete",
    title: "Second Post",
    user_id: 1,
  },
  // Add more dummy data objects as needed
];

import ReactPaginate from "react-paginate";
import { useLocation, useNavigate, Link } from "react-router-dom";
import { useState } from "react";

function History() {
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
              Type
            </th>
            <th scope="col" className="px-6 py-3">
              Action
            </th>
            <th scope="col" className="px-6 py-3">
              Title
            </th>
            <th scope="col" className="px-6 py-3">
              Action
            </th>
          </tr>
        </thead>
        <tbody>
          {dummy.map((history) => (
            <tr key={history.id} className="">
              <td className="px-6 py-4">{history.id}</td>
              <td className="px-6 py-4">{history.type}</td>
              <td className="px-6 py-4">{history.action}</td>
              <td className="px-6 py-4">{history.title}</td>
              <td className="space-x-2 flex justify-start">
               
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

export default History;
