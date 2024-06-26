import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import "suneditor/dist/css/suneditor.min.css";
import API_URL from "../url";
import axios from "axios";
import Swal from "sweetalert2";
import ClipLoader from "react-spinners/ClipLoader";
import { MultiSelect } from "react-multi-select-component";
import { generateSlug } from "../utils";
function MovieCreate() {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const { register, handleSubmit, setValue, getValues } = useForm();
  const [currentGenre, setCurrentGenre] = useState([]);
  const [allGenre, setAllGenres] = useState([]);
  const initialImg =
    "https://image.tmdb.org/t/p/w500//A4j8S6moJS2zNtRR8oWF08gRnL5.jpg"; // Initial image
  const [imgUrl, setImgUrl] = useState(initialImg);
  const handleCreateSlug = () => {
    const title = getValues("title");
    // Create a slug using a function (can be customized)
    const slug = createSlug(title);

    // Update the slug field using setValue
    setValue("slug", slug, { shouldDirty: true }); // Mark slug as dirty for validation
  };

  // Function to create a slug (replace with your preferred logic)
  const createSlug = (title) => {
    return generateSlug(title);
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

  // Handle form submission
  const onSubmit = async (data) => {
    if (imgUrl != initialImg) {
      setLoading(true);
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
    setLoading(false);
    const token = localStorage.getItem("accessToken");
    data.updated_at = new Date();
    data.categoryIds = currentGenre.map((genre) => {
      return genre.value;
    });
    console.log(data.categoryIds);
    axios
      .post(`${API_URL}.film`, data, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        console.log(res);
        Swal.fire({
          title: "Success",
          text: "Film created successfully",
          icon: "success",
        }).then((result) => {
          if (result.isConfirmed) {
            navigate(-1);
          }
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

  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    axios
      .get(`${API_URL}.category`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        setAllGenres(
          res.data.data.map((genre) => ({
            label: genre.title,
            value: genre.id,
          }))
        );
        console.log(res.data.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);
  return (
    <div className="w-full">
      <h1 className="text-2xl font-bold pl-2">Tạo phim mới</h1>
      <form
        action=""
        className="flex justify-between"
        onSubmit={handleSubmit(onSubmit)}
      >
        {/* left side */}
        <div className="p-2 space-y-2 w-1/3">
          {/* img */}
          <div className="flex items-center justify-center w-full h-full">
            <label className="flex flex-col items-center justify-center w-full h-full border-2 border-gray-300 border-dashed rounded-lg cursor-pointer bg-gray-50 dark:hover:bg-bray-800 dark:bg-gray-700 hover:bg-gray-100 dark:border-gray-600 dark:hover:border-gray-500 dark:hover:bg-gray-600">
              <img src={imgUrl} alt="Preview" className="w-full h-full" />
              <div className="flex flex-col items-center justify-center pt-5 pb-6">
                <svg
                  className="w-8 h-8 mb-4 text-gray-500 dark:text-gray-400"
                  aria-hidden="true"
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 20 16"
                >
                  <path
                    stroke="currentColor"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth="2"
                    d="M13 13h3a3 3 0 0 0 0-6h-.025A5.56 5.56 0 0 0 16 6.5 5.5 5.5 0 0 0 5.207 5.021C5.137 5.017 5.071 5 5 5a4 4 0 0 0 0 8h2.167M10 15V6m0 0L8 8m2-2 2 2"
                  />
                </svg>
                <p className="mb-2 text-sm text-gray-500 dark:text-gray-400">
                  <span className="font-semibold">Click to upload</span> or drag
                  and drop
                </p>
                <p className="text-xs text-gray-500 dark:text-gray-400">
                  SVG, PNG, JPG or GIF (MAX. 800x400px)
                </p>
              </div>
              <input
                id="dropzone-file"
                type="file"
                className="hidden"
                {...register("thumbnail", { onChange: handleFileChange })}
              />
            </label>
          </div>
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
                placeholder="Enter your title..."
                className="rounded p-2 border border-gray-600  max-w-[250px]"
                {...register("title", { defaultValue: "" })}
              />
            </div>

            <div className="flex flex-col">
              <label htmlFor="director">Tác giả:</label>
              <input
                type="text"
                name="director"
                placeholder="Enter your director..."
                className="rounded p-2 border border-gray-600 max-w-[250px]"
                {...register("director")}
              />
            </div>
            <div className="flex flex-col">
              <label htmlFor="location">Vị trí:</label>
              <input
                type="text"
                placeholder="Enter your location..."
                name="location"
                className="rounded p-2 border border-gray-600 max-w-[250px]"
                {...register("location")}
              />
            </div>
            <div className="flex flex-col">
              <label htmlFor="location">Slug:</label>
              <input
                type="text"
                placeholder="Enter your location..."
                name="slug"
                className="rounded p-2 border border-gray-600 max-w-[250px]"
                {...register("slug")}
              />
              <button
                className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 my-2 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800 w-fit"
                type="button"
                onClick={handleCreateSlug}
              >
                Generate Slug
              </button>
            </div>

            <div className="flex flex-col">
              <label htmlFor="is_required_premium">Loại phim:</label>
              <select
                type="text"
                name="location"
                className="rounded p-2 border border-gray-600 max-w-[250px]"
                {...register("type")}
              >
                <option value="movie">Phim ngắn</option>
                <option value="series">Dài tập</option>
              </select>
            </div>
            <div className="flex flex-col">
              <label htmlFor="is_required_premium">Tiến độ:</label>
              <select
                type="text"
                name="status"
                className="rounded p-2 border border-gray-600 max-w-[250px]"
                {...register("status")}
              >
                <option value="updating">Updating</option>
                <option value="full">Full</option>
              </select>
            </div>
          </div>
          <div className="z-10 ">
            <label htmlFor="">Thể loại:</label>
            <MultiSelect
              value={currentGenre}
              onChange={setCurrentGenre}
              options={allGenre}
            />
          </div>
          <div className="flex flex-col pt-4">
            <label htmlFor="des">Description:</label>
            <textarea
              id="des"
              rows="4"
              className="block p-2.5 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500 dark:focus:ring-blue-500 dark:focus:border-blue-500"
              placeholder="Write your description here..."
              {...register("description")}
            ></textarea>
          </div>
          {/* grid */}
          <div className="grid grid-cols-2 w-fit gap-2">
            <div className="flex flex-col justify-center w-fit">
              <label className="inline-flex items-center cursor-pointer">
                <input
                  type="checkbox"
                  className="sr-only peer"
                  {...register("is_required_premium")}
                />
                <div className="relative w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 dark:peer-focus:ring-blue-800 rounded-full peer dark:bg-gray-700 peer-checked:after:translate-x-full rtl:peer-checked:after:-translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:start-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all dark:border-gray-600 peer-checked:bg-blue-600"></div>
                <span className="ms-3 text-sm font-medium text-gray-900 dark:text-gray-300">
                  Premium
                </span>
              </label>
            </div>
            <div className="flex flex-col w-fit">
              <label className="inline-flex items-center cursor-pointer">
                <input
                  type="checkbox"
                  className="sr-only peer"
                  {...register("is_active")}
                  defaultChecked={true}
                />
                <div className="relative w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 dark:peer-focus:ring-blue-800 rounded-full peer dark:bg-gray-700 peer-checked:after:translate-x-full rtl:peer-checked:after:-translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:start-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all dark:border-gray-600 peer-checked:bg-blue-600"></div>
                <span className="ms-3 text-sm font-medium text-gray-900 dark:text-gray-300">
                  Trạng thái
                </span>
              </label>
            </div>
          </div>

          {/*Save button  */}
          <div className="flex">
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
                  "Lưu"
                )}
              </span>
            </button>
          </div>
        </div>
      </form>
    </div>
  );
}

export default MovieCreate;
