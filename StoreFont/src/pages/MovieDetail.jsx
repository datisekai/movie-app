import { Link, useLocation } from "react-router-dom";
import { useState, useEffect, useRef } from "react";
import { useForm } from "react-hook-form";
import SunEditor from "suneditor-react";
import "suneditor/dist/css/suneditor.min.css";
import axios from "axios";
import API_URL from "../url";
import Swal from "sweetalert2";
function MovieDetail() {
  const [loading, setLoading] = useState(false);
  const { register, handleSubmit } = useForm();
  const location = useLocation();
  const movie = location.state;
  const [description, setDescription] = useState(movie.description);
  const addGenreRef = useRef(null);
  const [currentGenre, setCurrentGenre] = useState([]);
  const [allGenre, setAllGenres] = useState([]);
  const initialImg = movie.thumbnail; // Initial image
  const [imgUrl, setImgUrl] = useState(initialImg);

  const handleFileChange = (event) => {
    const newImage = event.target.files[0];

    if (newImage && newImage.type.match(/^image\//)) {
      // Check if the selected file is an image
      const reader = new FileReader();

      reader.onload = (e) => setImgUrl(e.target.result); // Update image src on read
      reader.readAsDataURL(newImage);
    } else {
      // Handle non-image files (optional)
      console.warn("Please select an image file.");
    }
  };

  // Handle form submission
  const onSubmit = async (data) => {
    setLoading(true);
    if (imgUrl != initialImg) {
      const res = await axios.post(
        `${API_URL}.upload/image`,
        {
          file: data.thumbnail[0],
        },
        {
          headers: {
            "Content-Type": "multipart/form-data",
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
        }
      );
      if (res.status == 201) {
        data.thumbnail = res.data.url;
      } else {
        Swal.fire({
          title: "Error",
          text: res,
          icon: "error",
        });
      }
    }
    else{
      data.thumbnail = initialImg;
    }
    const token = localStorage.getItem("accessToken");
    data.description = description;
    data.categoryIds = currentGenre.map((genre) => {
      return genre.id;
    });
    axios
      .put(`${API_URL}.film/${movie.id}`, data, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        console.log(res);
        setLoading(false)
        Swal.fire({
          title: "Success",
          text: "Film updated successfully",
          icon: "success",
        });
      })
      .catch((err) => {
        console.log(err);
        Swal.fire({
          title: "Error",
          text: err.response.data.message,
          icon: "error",
        });
      });
  };
  function addGenre() {
    const newGenreId = addGenreRef.current.value;
    const [selectedGenre] = allGenre.filter((genre) => {
      return genre.id == newGenreId;
    });
    if (selectedGenre) {
      if (currentGenre.length > 0) {
        if (!currentGenre.includes(selectedGenre)) {
          setCurrentGenre([...currentGenre, selectedGenre]);
          setAllGenres(
            allGenre.filter((genre) => {
              return genre.id != newGenreId;
            })
          );
        }
      } else {
        setCurrentGenre([selectedGenre]);
        setAllGenres(
          allGenre.filter((genre) => {
            return genre.id != newGenreId;
          })
        );
      }
    }
  }
  function removeGenre(genre) {
    setCurrentGenre(currentGenre.filter((item) => item.id != genre.id));
    setAllGenres([...allGenre, genre]);
  }
  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    axios
      .get(`${API_URL}.category`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        setAllGenres(res.data.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);
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
          <img src={imgUrl} alt={`film_poster`} className="w-full h-full" />
          <input
            type="file"
            multiple={false}
            {...register("thumbnail", { onChange: handleFileChange })}
          />
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
              <select
                type="text"
                name="location"
                className="rounded p-2 border border-gray-600 max-w-[250px]"
                {...register("is_required_premium")}
              >
                <option value="false">False</option>
                <option value="true">True</option>
              </select>
            </div>
            <div className="flex flex-col">
              <label htmlFor="is_required_premium">Film type:</label>
              <select
                type="text"
                name="location"
                className="rounded p-2 border border-gray-600 max-w-[250px]"
                {...register("type")}
              >
                <option value="movie">Movie</option>
                <option value="series">Series</option>
              </select>
            </div>
            <div className="flex flex-col">
              <label htmlFor="is_required_premium">Status:</label>
              <select
                type="text"
                name="location"
                className="rounded p-2 border border-gray-600 max-w-[250px]"
                {...register("status")}
              >
                <option value="updating">Updating</option>
                <option value="full">Full</option>
              </select>
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
                      {genre.title}
                    </span>
                    <button
                      type="button"
                      onClick={() => removeGenre(genre)}
                      className="p-2 bg-red-400 rounded text-white group-hover:opacity-100 opacity-0 w-full text-center absolute left-0 transition-all duration-300"
                    >
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
              ref={addGenreRef}
            >
              {allGenre.map((genre) => (
                <option value={genre.id} key={genre.id}>
                  {genre.title}
                </option>
              ))}
            </select>
            <button
              type="button"
              onClick={addGenre}
              className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800 w-fit"
            >
              Add Genre
            </button>
          </div>
          <div className="flex flex-col">
            <label htmlFor="description">Description:</label>
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
                <option value="false">False</option>
                <option value="true">True</option>
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
              className={`text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800 ${
                loading ? "disabled opacity-50 cursor-not-allowed" : ""
              }`} // Conditional classNames for loading state
              disabled={loading} // Use disabled prop for accessibility
            >
              {loading ? (
                <span className="animate-spin mr-2">Updating...</span>
              ) : (
                "Save"
              )}
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
