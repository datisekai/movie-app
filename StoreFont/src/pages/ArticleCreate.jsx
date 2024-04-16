import { useForm } from "react-hook-form";
import SunEditor from "suneditor-react";
import { useState, useEffect } from "react";
import axios from "axios";
import Swal from "sweetalert2";
import API_URL from "../url";
import ClipLoader from "react-spinners/ClipLoader";
import { useNavigate } from "react-router-dom";
import { MultiSelect } from "react-multi-select-component";
function ArticleCreate() {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const { register, handleSubmit } = useForm();
  const [content, setContent] = useState("");
  const initialImg =
    "https://images.unsplash.com/photo-1481349518771-20055b2a7b24?q=80&w=1000&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nnx8cmFuZG9tfGVufDB8fDB8fHww";
  const [imgUrl, setImgUrl] = useState(initialImg);
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
    data.content = content;
    data.updated_at = new Date();
    data.categoryIds = currentGenre.map((genre) => {
      return genre.value;
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
          res.data.data.map((genre) => ({
            label: genre.title,
            value: genre.id,
          }))
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
      <div className=" flex flex-col w-2/3 mt-5">
        <div className="w-full grid grid-cols-2 gap-2">
          <div className="flex flex-col h-fit">
            <label htmlFor="title">Title:</label>
            <input
              type="text"
              name="title"
              placeholder="Enter your title"
              className="rounded p-2 border border-gray-600  max-w-[250px]"
              {...register("title")}
            />
          </div>
        </div>
        <div className="py-5 z-10 ">
          <label htmlFor="">Genres:</label>
          <MultiSelect
            value={currentGenre}
            onChange={setCurrentGenre}
            options={allGenre}
          />
        </div>
        <div className="flex flex-col">
          <label htmlFor="desc">Content:</label>
          <SunEditor
            setContents={content}
            onChange={(content) => setContent(content)}
            height="10rem"
            placeholder="Enter the content..."
          />
        </div>

        <div className="flex flex-col pt-4">
          <label
            htmlFor="des"
          >
            Description:
          </label>
          <textarea
            id="des"
            rows="4"
            className="block p-2.5 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
            placeholder="Write your description here..."
            {...register("description")}
          ></textarea>
        </div>

        <div className="flex flex-col py-2">
          <label className="inline-flex items-center cursor-pointer">
            <input
              type="checkbox"
              value=""
              className="sr-only peer"
              {...register("is_active")}
              defaultChecked={true}
            />
            <div className="relative w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 dark:peer-focus:ring-blue-800 rounded-full peer dark:bg-gray-700 peer-checked:after:translate-x-full rtl:peer-checked:after:-translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:start-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all dark:border-gray-600 peer-checked:bg-blue-600"></div>
            <span className="ms-3 text-sm font-medium text-gray-900 dark:text-gray-300">
              Is Active
            </span>
          </label>
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
