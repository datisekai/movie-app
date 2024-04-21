import ReactPaginate from "react-paginate";
import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import API_URL from "../url";
import ClipLoader from "react-spinners/ClipLoader";
import { formatDate } from "../utils";
function Payment() {
  const [payments, setPayments] = useState([]);
  const itemsPerPage = 10;
  const location = useLocation();
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const searchParams = new URLSearchParams(location.search);
  const pageParam = searchParams.get("page");
  const [page, setPage] = useState(pageParam ? parseInt(pageParam) : 1);
  const [totalEntries, setTotalEntries] = useState(0);
  const [filter, setFilter] = useState({ from: "", to: "" });
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
    navigate(`/payments?page=${event.selected + 1}`);
  };
  useEffect(() => {
    setLoading(true);
    const params = { page };
    if (filter.from) {
      params.from = filter.from;
    }

    if (filter.to) {
      params.to = filter.to;
    }
    axios
      .get(`${API_URL}.order`, {
        params,
      })
      .then((res) => {
        setPayments(res.data.data);
        setTotalEntries(res.data.totalEntries);
      })
      .catch((err) => {
        console.log(err);
      })
      .finally(() => setLoading(false));
  }, [page]);

  const handleFilter = () => {
    if (filter.from || filter.to) {
      setLoading(true);
      axios
        .get(`${API_URL}.order`, {
          params: { from: filter.from, to: filter.to, page: page },
        })
        .then((res) => {
          setPayments(res.data.data);
          setTotalEntries(res.data.totalEntries);
        })
        .catch((err) => {
          console.log(err);
        })
        .finally(() => setLoading(false));
    }
  };

  return (
    <div className="relative overflow-x-auto">
      <div className="flex items-center gap-4 mb-2">
        <div>
          <label htmlFor="">From</label>
          <div className="relative max-w-sm">
            <input
              type="date"
              onChange={(e) => setFilter({ ...filter, from: e.target.value })}
              className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full ps-10 p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
              placeholder="Select date"
            />
          </div>
        </div>
        <div>
          <label htmlFor="">To</label>
          <div className="relative max-w-sm">
            <input
              type="date"
              onChange={(e) => setFilter({ ...filter, to: e.target.value })}
              className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full ps-10 p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
              placeholder="Select date"
            />
          </div>
        </div>
        <div className="">
          <label>Hành động</label>
          <button
            onClick={handleFilter}
            type="button"
            className="block focus:outline-none text-white bg-purple-700 hover:bg-purple-800 focus:ring-4 focus:ring-purple-300 font-medium rounded-lg text-sm px-5 py-2.5 dark:bg-purple-600 dark:hover:bg-purple-700 dark:focus:ring-purple-900"
          >
            Lọc giao dịch
          </button>
        </div>
      </div>
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
        <div>
          <table className="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
            <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
              <tr>
                <th scope="col" className="px-6 py-3">
                  Id
                </th>
                <th scope="col" className="px-6 py-3">
                  Số lượng
                </th>
                <th scope="col" className="px-6 py-3">
                  Tình trạng
                </th>
                <th scope="col" className="px-6 py-3">
                  Loại
                </th>
                <th scope="col" className="px-6 py-3">
                  Trans_ID
                </th>
                <th scope="col" className="px-6 py-3">
                  Ngày tạo
                </th>
              </tr>
            </thead>
            <tbody>
              {payments.map((history) => (
                <tr key={history.id} className="">
                  <td className="px-6 py-4">{history.id}</td>
                  <td className="px-6 py-4">
                    {history.amount.toLocaleString("it-IT", {
                      style: "currency",
                      currency: "VND",
                    })}
                  </td>
                  <td className="px-6 py-4">{history.order_status}</td>
                  <td className="px-6 py-4">{history.order_type}</td>
                  <td className="px-6 py-4">{history.zalo_trans_id}</td>
                  <td className="px-6 py-4">
                    {formatDate(history.created_at)}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

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
        forcePage={pageParam ? parseInt(pageParam) - 1 : 0}
        className="flex  w-full justify-center items-center py-2"
      />
    </div>
  );
}

export default Payment;
