import { useForm } from "react-hook-form";
import SunEditor from "suneditor-react";
import { useState, useRef, useEffect } from "react";
import axios from "axios";
import Swal from "sweetalert2";
import API_URL from "../url";
import ClipLoader from "react-spinners/ClipLoader";
import { useNavigate } from "react-router-dom";
function ArticleCreate() {
  const navigate = useNavigate()
  const [loading, setLoading] = useState(false);
  const { register, handleSubmit } = useForm();
  const [description, setDescription] = useState("");
  const initialImg =
    "https://images.unsplash.com/photo-1481349518771-20055b2a7b24?q=80&w=1000&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nnx8cmFuZG9tfGVufDB8fDB8fHww";
  const [imgUrl, setImgUrl] = useState(initialImg);
  const addGenreRef = useRef(null);
  const [currentGenre, setCurrentGenre] = useState([]);
  const [allGenre, setAllGenres] = useState([]);
  
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
    } else {
      data.thumbnail = initialImg;
    }

    const token = localStorage.getItem("accessToken");
    const is_active = data.is_active === "true";
    data.is_active = is_active;
    data.content = description;
    data.updated_at = new Date();
    data.categoryIds = currentGenre.map((genre) => {
      return genre.id;
    });
    axios
      .post(`${API_URL}.article`, data, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        console.log(res);
        Swal.fire({
          title: "Success",
          text: "Article created successfully",
          icon: "success",
        })
        .then((result) => {
          if (result.isConfirmed) {
            navigate(-1)
          }
        })
      })
      .catch((err) => {
        console.log(err);
        Swal.fire({
          title: "Error",
          text: err.response.data.message,
          icon: "error",
        });
      })
      .finally(setLoading(false));
  };
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
        console.log(currentGenre);
        console.log(res.data.data);
        setAllGenres(
          res.data.data.filter(
            (item) =>
              !currentGenre.some((currentItem) => currentItem.id === item.id)
          )
        );
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);
  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className="w-full flex justify-between gap-5"
    >
      {/* left side */}
      <div className="flex flex-col gap-4 w-1/3">
        <h3>Thumbnail</h3>
        <img src={imgUrl} alt={`film_poster`} className="w-full h-full" />
        <input
          type="file"
          multiple={false}
          {...register("thumbnail", { onChange: handleFileChange })}
        />
      </div>
      {/* right side */}
      <div className=" flex flex-col w-2/3 mt-5">
        <div className="w-full grid grid-cols-2 gap-2">
          <div className="flex flex-col h-fit">
            <label htmlFor="title">Title:</label>
            <input
              type="text"
              name="title"
              className="rounded p-2 border border-gray-600  max-w-[250px]"
              {...register("title")}
            />
          </div>
        </div>
        <div className="flex flex-col">
          <label htmlFor="desc">Description:</label>
          <SunEditor
            setContents={description}
            onChange={(content) => setDescription(content)}
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
        <div className="flex flex-col h-fit">
          <label htmlFor="is_active">Is Active:</label>
          <select
            name="is_active"
            id=""
            className="rounded p-2 border border-gray-600  max-w-[250px]"
            {...register("is_active")}
          >
            <option value="true">True</option>
            <option value="false">False</option>
          </select>
        </div>
        <div>
          <button
            type="submit"
            className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 my-2 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800 max-w-20"
          >
            <span>
              {loading ? (
                <ClipLoader
                  color={"f"}
                  size={20}
                  loading={loading}
                  aria-label="Loading Spinner"
                  data-testid="loader"
                />
              ) : (
                "Save"
              )}
            </span>
          </button>
        </div>
      </div>
    </form>
  );
}

export default ArticleCreate;
