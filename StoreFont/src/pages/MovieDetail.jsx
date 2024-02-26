import { useLocation } from "react-router-dom";
import { useState } from "react";
function MovieDetail() {
  const location = useLocation();
  const movie = location.state;
  const [currentGenre, setCurrentGenre] = useState([
    { id: 1, name: "Action" },
    { id: 2, name: "Adventure" },
    { id: 3, name: "Comedy" },
    { id: 4, name: "Drama" },
    { id: 5, name: "Fantasy" },
  ]);
  const [allGenre, setAllGenres] = useState([
    { id: 1, name: "Action" },
    { id: 2, name: "Adventure" },
    { id: 3, name: "Comedy" },
    { id: 4, name: "Drama" },
    { id: 5, name: "Fantasy" },
    { id: 6, name: "Science Fiction" },
    { id: 7, name: "Horror" },
    { id: 8, name: "Romance" },
    { id: 9, name: "Thriller" },
    { id: 10, name: "Animation" },
  ]);
  return (
    <div className="w-full">
      <h1 className="text-2xl font-bold pl-2">{movie.title}</h1>
      <form action="" className="flex justify-between">
        {/* left side */}
        <div className="p-2 space-y-2 w-1/3">
          <img
            src={
              "https://image.tmdb.org/t/p/w500//A4j8S6moJS2zNtRR8oWF08gRnL5.jpg"
            }
            alt={`${movie}_poster`}
            className="w-full h-full"
          />
          <input type="file" name="" id="" />
        </div>
        {/* right side */}
        <div className="p-2 w-2/3 flex flex-col gap-3">
          <div className="flex flex-col">
            <label htmlFor="title">Title:</label>
            <input
              type="text"
              name="title"
              className="rounded p-2 border border-gray-600  max-w-[250px]"
            />
          </div>
          <div className="flex flex-col">
            <label htmlFor="released_date">Released_Date:</label>
            <input
              type="date"
              name="released_date"
              className="rounded p-2 border border-gray-600 max-w-[250px]"
            />
          </div>
          <div className="flex flex-col">
            <label htmlFor="released_date">Current Genre:</label>
            <div>
              {/* genre group */}
              <div className="py-2 space-x-2 flex gap-2 flex-wrap ">
                {currentGenre.map((genre) => (
                  <div
                    className="flex cursor-pointer group w-fit relative"
                    key={genre.id}
                  >
                    <span className="p-2 bg-gray-400 rounded text-white group-hover:opacity-0 opacity-100 w-full text-center transition-all duration-300">
                      {genre.name}
                    </span>
                    <button className="p-2 bg-red-400 rounded text-white group-hover:opacity-100 opacity-0 w-full text-center absolute left-0 transition-all duration-300">
                      X
                    </button>
                  </div>
                ))}
              </div>
            </div>
          </div>
          <div className="flex flex-col gap-2">
            <label htmlFor="genre">Add New Genre:</label>
            <select
              name=""
              id=""
              className="rounded p-2 border border-gray-600 max-w-[250px]"
            >
              {allGenre.map((genre) => (
                <option value={genre.id} key={genre.id}>{genre.name}</option>
              ))}
            </select>
            <button className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800 w-fit">
              Add Genre
            </button>
          </div>
          <div className="flex flex-col">
            <label htmlFor="description">Description:</label>
            <textarea
              name=""
              id=""
              cols="20"
              rows="5"
              className="p-2 resize-none border-gray-600 border"
            ></textarea>
          </div>

          <div className="flex">
            <button
              type="button"
              className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800"
            >
              Save
            </button>
          </div>
        </div>
      </form>
    </div>
  );
}

export default MovieDetail;
