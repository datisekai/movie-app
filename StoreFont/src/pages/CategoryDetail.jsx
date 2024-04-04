import { useLocation } from "react-router-dom";
import { useForm } from "react-hook-form";
import { useEffect, useState } from "react";
import SunEditor from "suneditor-react";
import "suneditor/dist/css/suneditor.min.css";
import axios from "axios";
import API_URL from "../url";
import Swal from "sweetalert2";
function CategoryDetail() {
  const [loading, setLoading] = useState(false);
  const [description, setDescription] = useState("");
  const location = useLocation();
  const category = location.state;
  const { register, handleSubmit, setValue } = useForm();
  const initialImg = category.thumbnail; // Initial image
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
    const is_active = data.is_active === "true";
    const is_deleted = data.is_deleted === "true";
    data.is_active = is_active;
    data.is_deleted = is_deleted;
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
        <h3>Thumbnail</h3>
        <img src={imgUrl} alt={`film_poster`} className="w-full h-full" />
        <input
          type="file"
          multiple={false}
          {...register("thumbnail", { onChange: handleFileChange })}
        />
      </div>
      {/* right side */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-y-2 w-2/3">
        <div className="flex flex-col">
          <label htmlFor="id">Id:</label>
          <input
            type="text"
            name="id"
            className="rounded p-2 border border-gray-600  max-w-[250px]"
            defaultValue={category.id}
            readOnly
          />
        </div>
        <div className="flex flex-col">
          <label htmlFor="title">Title:</label>
          <input
            type="text"
            name="title"
            className="rounded p-2 border border-gray-600  max-w-[250px]"
            defaultValue={category.title}
            {...register("title")}
          />
        </div>
        <div className="flex flex-col">
          <label htmlFor="title_search">Title_Search:</label>
          <input
            type="text"
            name="title_search"
            className="rounded p-2 border border-gray-600  max-w-[250px]"
            defaultValue={category.title_search}
            {...register("title_search")}
          />
        </div>
        <div className="flex flex-col md:col-span-3 w-full">
          <label htmlFor="description">Description:</label>
          <SunEditor
            defaultValue={category.description}
            onChange={(content) => setDescription(content)}
          />
        </div>
        <div className="flex flex-col">
          <label htmlFor="is_active">Is Active:</label>
          <select
            name="is_active"
            id=""
            className="rounded p-2 border border-gray-600  max-w-[250px]"
            defaultValue={category.is_active}
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
            defaultValue={category.is_deleted}
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
            defaultValue={category.created_at}
          />
        </div>
        <div className="flex flex-col col-span-3">
          <label htmlFor="updated_at">Updated At:</label>
          <input
            type="text"
            name="updated_at"
            className="rounded p-2 border border-gray-600  max-w-[250px]"
            readOnly
            defaultValue={category.updated_at}
            {...register("updated_at")}
          />
        </div>
        <div>
          <button
            type="submit"
            className={`text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800 ${
              loading ? "disabled opacity-50 cursor-not-allowed" : ""
            }`} // Conditional classNames for loading state
            disabled={loading} // Use disabled prop for accessibility
          >
            {loading ? (
              <span className="animate-spin mr-2">Uploading Image...</span>
            ) : (
              "Save"
            )}
          </button>
        </div>
      </div>
    </form>
  );
}

export default CategoryDetail;
