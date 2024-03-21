import { Link, useLocation } from "react-router-dom";
import { useState } from "react";
import { useForm } from "react-hook-form";
import SunEditor from "suneditor-react";
import 'suneditor/dist/css/suneditor.min.css';
function MovieDetail() {
  const { register, handleSubmit, control, setValue, getValues } = useForm();
  const location = useLocation();
  const movie = location.state;
  const [description,setDescription] = useState(movie.description)
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
  // Handle form submission
  const onSubmit = (data) => {
    data.description = description;
    console.log(data);
  };
  return (
    <div className="w-full">
      <h1 className="text-2xl font-bold pl-2">{movie.title}</h1>
      <form
        action=""
        className="flex justify-between"
        onSubmit={handleSubmit(onSubmit)}
      >
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
          {/* grid */}
          <div className="grid grid-cols-2">
            <div className="flex flex-col">
              <label htmlFor="title">Title:</label>
              <input
                type="text"
                name="title"
                className="rounded p-2 border border-gray-600  max-w-[250px]"
                {...register("title")}
                defaultValue={movie.title}
              />
            </div>
            <div className="flex flex-col">
              <label htmlFor="title_search">Title_Search:</label>
              <input
                type="text"
                name="title_search"
                className="rounded p-2 border border-gray-600 max-w-[250px]"
                {...register("title_search")}
                defaultValue={movie.title_search}
              />
            </div>
            <div className="flex flex-col">
              <label htmlFor="director">Director:</label>
              <input
                type="text"
                name="director"
                className="rounded p-2 border border-gray-600 max-w-[250px]"
                {...register("director")}
                defaultValue={movie.director}
              />
            </div>
            <div className="flex flex-col">
              <label htmlFor="location">Location:</label>
              <input
                type="text"
                name="location"
                className="rounded p-2 border border-gray-600 max-w-[250px]"
                {...register("location")}
                defaultValue={movie.location}
              />
            </div>
         
          <div className="flex flex-col">
            <label htmlFor="title">View:</label>
            <p className="rounded p-2 border border-gray-600  max-w-[250px]">
              {movie.view}
            </p>
          </div>
          <div className="flex flex-col">
            <label htmlFor="is_required_premium">Is Required Premium:</label>
            <input
                type="text"
                name="location"
                className="rounded p-2 border border-gray-600 max-w-[250px]"
                {...register("location")}
                defaultValue={movie.location}
              />
          </div>
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
                <option value={genre.id} key={genre.id}>
                  {genre.name}
                </option>
              ))}
            </select>
            <button className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800 w-fit">
              Add Genre
            </button>
          </div>
          <div className="flex flex-col">
            <label htmlFor="description">Description:</label>
            {/* <textarea
              name="description"
              id=""
              className="rounded p-2 border border-gray-600 w-full"
              defaultValue={movie.description}
              {...register("description")}
            /> */}
            <SunEditor
              defaultValue={movie.description}
              setContents={description}
              onChange={(content) => setDescription(content)}
            />
          </div>
          {/* grid */}
          <div className="grid grid-cols-2">
          <div className="flex flex-col">
            <label htmlFor="is_active">Is Active:</label>
            <select
              name="is_active"
              id=""
              className="rounded p-2 border border-gray-600  max-w-[250px]"
              defaultValue={movie.is_active}
              {...register("is_active")}
            >
              <option value="true">True</option>
              <option value="false">False</option>
            </select>
          </div>
          <div className="flex flex-col">
            <label htmlFor="is_deleted">Is Deleted:</label>
            <select
              name="is_deleted"
              id=""
              className="rounded p-2 border border-gray-600  max-w-[250px]"
              defaultValue={movie.is_deleted}
              {...register("is_deleted")}
            >
              <option value="true">True</option>
              <option value="false">False</option>
            </select>
          </div>
          <div className="flex flex-col">
            <label htmlFor="created_at">Created At:</label>
            <input
              type="text"
              name="created_at"
              className="rounded p-2 border border-gray-600  max-w-[250px]"
              readOnly
              defaultValue={movie.created_at}
            />
          </div>
          <div className="flex flex-col">
            <label htmlFor="updated_at">Updated At:</label>
            <input
              type="text"
              name="updated_at"
              className="rounded p-2 border border-gray-600  max-w-[250px]"
              readOnly
              defaultValue={movie.updated_at}
              {...register("updated_at")}
            />
          </div>
          </div>

          {/*Save button  */}
          <div className="flex">
            <button
              type="submit"
              className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800"
            >
              Save
            </button>
            <Link
              to={`episodes`}
              className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800"
            >
              Episodes
            </Link>
          </div>
        </div>
      </form>
    </div>
  );
}

export default MovieDetail;
