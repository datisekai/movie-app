import { useLocation, useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import { useState } from "react";
import SunEditor from "suneditor-react";
import "suneditor/dist/css/suneditor.min.css";
import axios from "axios";
import API_URL from "../url";
import Swal from "sweetalert2";
import ClipLoader from "react-spinners/ClipLoader";
function CategoryDetail() {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const location = useLocation();
  const category = location.state;
  const { register, handleSubmit } = useForm();
  const initialImg = category.thumbnail; // Initial image
  const [imgUrl, setImgUrl] = useState(initialImg);
  const [description, setDescription] = useState(category.description || "");

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
    } else {
      data.thumbnail = initialImg;
    }
    setLoading(false);
    const token = localStorage.getItem("accessToken");
    const is_active = data.is_active === "true";
    data.is_active = is_active;
    data.description = description;
    data.updated_at = new Date();
    axios
      .put(`${API_URL}.category/${category.id}`, data, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        console.log(res);
        Swal.fire({
          title: "Success",
          text: "Category updated successfully",
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

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className="flex justify-between gap-5"
    >
      {/* left side */}
      <div className="w-1/3">
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
      <div className="grid grid-cols-1 md:grid-cols-3 gap-y-2 w-2/3">
        <div className="flex flex-col">
          <label htmlFor="title">Title:</label>
          <input
            type="text"
            name="title"
            className="rounded p-2 border border-gray-600  max-w-[250px]"
            {...register("title")}
            defaultValue={category.title}
          />
        </div>

        <div className="flex flex-col md:col-span-3 w-full">
          <label htmlFor="description">Description:</label>
          <SunEditor
            setContents={description}
            onChange={(content) => setDescription(content)}
            defaultValue={category.description}
            height="10rem"
          />
        </div>
        <div className="flex flex-col col-span-3">
          <label className="inline-flex items-center cursor-pointer">
            <input
              type="checkbox"
              value=""
              className="sr-only peer"
              {...register("is_active")}
              defaultChecked={category.is_active}
              defaultValue={category.is_active}
            />
            <div className="relative w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 dark:peer-focus:ring-blue-800 rounded-full peer dark:bg-gray-700 peer-checked:after:translate-x-full rtl:peer-checked:after:-translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:start-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all dark:border-gray-600 peer-checked:bg-blue-600"></div>
            <span className="ms-3 text-sm font-medium text-gray-900 dark:text-gray-300">
              Is Active
            </span>
          </label>
        </div>

        <div className="">
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

export default CategoryDetail;
