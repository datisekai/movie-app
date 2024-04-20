import { useEffect, useState } from "react";
import { format } from "date-fns";
import { Link } from "react-router-dom";
import { getOrderStatus } from "../lib/helpers";
import axios from "axios";
import API_URL from "../url";
import ClipLoader from "react-spinners/ClipLoader";
export default function RecentOrders() {
  const [loading, setLoading] = useState(false);
  const [articles, setArticles] = useState([]);
  useEffect(() => {
    setLoading(true);
    axios
      .get(`${API_URL}.article?page=1`)
      .then((res) => {
        console.log(res.data.data);
        setArticles(res.data.data);
      })
      .catch((err) => {
        console.log(err);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);
  return (
    <div className="bg-white px-4 pt-3 pb-4 rounded-sm border border-gray-200 flex-1">
      <strong className="text-gray-700 font-medium">Recent Orders</strong>
      {loading ? (
        <div className="flex justify-center items-center">
          <ClipLoader
            color={"f"}
            size="2rem"
            loading={loading}
            aria-label="Loading Spinner"
            data-testid="loader"
          />
        </div>
      ) : (
        <div className="border-x border-gray-200 rounded-sm mt-3">
          <table className="w-full text-gray-700">
            <thead>
              <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Is Active</th>
                <th>Updated at</th>
              </tr>
            </thead>
            <tbody>
              {articles.map((article) => (
                <tr key={article.id} className="text-center">
                  <td>{article.id}</td>
                  <td>{article.title}</td>
                  <td>{article.is_active.toString()}</td>
                  <td>{article.updated_at}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
