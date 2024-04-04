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
import {  Link } from "react-router-dom";
import { useState } from "react";

function History() {
  const itemsPerPage = 10;
  const [itemOffset, setItemOffset] = useState(0);
  const [page,setPage] = useState(1);
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
    console.log(event.selected+1)
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
          className="flex  w-full justify-center items-center py-2"
        />
    </div>
  );
}

export default History;
