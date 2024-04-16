import { useState } from "react";
import classNames from "classnames";
import { useEffect } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import API_URL from "../url";
import ClipLoader from "react-spinners/ClipLoader";

function Popularmovies() {
  const [loading, setLoading] = useState(false);
  const [movies, setMovies] = useState([]);
  useEffect(() => {
    setLoading(true);
    axios
      .get(`${API_URL}.film?page=1`)
      .then((res) => {
        console.log(res.data.data);
        setMovies(res.data.data);
        console.log("new");
      })
      .catch((err) => {
        console.log(err);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);
  return (
    <div className="w-[20rem] bg-white p-4 rounded-sm border border-gray-200">
      <strong className="text-gray-700 font-medium">Popular Movies</strong>
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
        <div className="mt-4 flex flex-col gap-3">
          {movies.map((movie) => (
            <Link
              key={movies.id}
              to={`/movies/${movie.id}`}
              className="flex items-start hover:no-underline"
            >
              <div className="w-10 h-10 min-w-[2.5rem] bg-gray-200 rounded-sm">
                <img
                  className="w-full h-full object-cover rounded-sm"
                  src={movie.thumbnail}
                  alt={movie.title}
                />
              </div>
              <div className="ml-4 flex-1">
                <p className="text-sm text-gray-800">{movie.title}</p>
                <span
                  className={classNames(
                    movie.movie_stock === 0
                      ? "text-red-500"
                      : movie.movie_stock > 50
                      ? "text-green-500"
                      : "text-orange-500",
                    "text-xs font-medium"
                  )}
                >
                  {movie.view + " view"}
                </span>
              </div>
              <div className="text-xs text-gray-400 pl-1.5">
                {movie.movie_price}
              </div>
            </Link>
          ))}
        </div>
      )}
    </div>
  );
}

export default Popularmovies;
