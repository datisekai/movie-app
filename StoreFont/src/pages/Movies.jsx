const dummy = [
  {
    id: 1,
    slug: "film-1",
    title: "Film 1",
    title_search: "film one",
    description: "<p>This is the description for Film 1.</p>",
    view: 1000,
    thumbnail: "film1.jpg",
    type: "tv-show",
    status: "Đang tiến hành",
    is_required_premium: true,
    director: "Director One",
    location: "TPHCM",
    is_active: true,
    is_deleted: false,
    created_at: new Date(),
    updated_at: new Date()
  },
  {
    id: 2,
    slug: "film-2",
    title: "Film 2",
    title_search: "film two",
    description: "<p>This is the description for Film 2.</p>",
    view: 500,
    thumbnail: "film2.jpg",
    type: "movie",
    status: "Full",
    is_required_premium: false,
    director: "Director Two",
    location: "Hanoi",
    is_active: true,
    is_deleted: false,
    created_at: new Date(),
    updated_at: new Date()
  }
];


import ReactPaginate from "react-paginate";
import { useState } from "react";
import { Link, useLocation,useNavigate } from "react-router-dom";
export default function Movies() {
  const itemsPerPage = 5;
  // boilerplate for pagination
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
  
  //  end boilerplate

  return (
    <div>
      <h1>Movies</h1>

      <div className="relative overflow-x-auto">
        <table className="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
          <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
            <tr>
            <th scope="col" className="px-6 py-3">
                ID
              </th>
              <th scope="col" className="px-6 py-3">
                Title
              </th>
              <th scope="col" className="px-6 py-3">
                Type
              </th>
              <th scope="col" className="px-6 py-3">
                Status
              </th>
              <th scope="col" className="px-6 py-3">
                Is Active
              </th>
              <th scope="col" className="px-6 py-3">
                Action
              </th>
            </tr>
          </thead>
          <tbody>
            {dummy.map((movie) => (
              <tr key={movie._id} className="">
                <th
                  scope="row"
                  className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white"
                >
                  {movie.id}
                </th>
                <td className="px-6 py-4">{movie.title}</td>
                <td className="px-6 py-4">{movie.type}</td>
                <td className="px-6 py-4">{movie.status}</td>
                <td className="px-6 py-4">{movie.is_active.toString()}</td>
                <td className="space-x-2 flex justify-start">
                  <Link
                    type="button"
                    className="focus:outline-none text-white bg-purple-700 hover:bg-purple-800 focus:ring-4 focus:ring-purple-300 font-medium rounded-lg text-sm px-5 py-2.5 mb-2 dark:bg-purple-600 dark:hover:bg-purple-700 dark:focus:ring-purple-900"
                    to={`/movies/${movie.id}`}
                    state={movie}
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
          activeClassName="bg-red-600 hover:bg-red-700 transiton-colors duration-300 text-white 	"
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
    </div>
  );
}