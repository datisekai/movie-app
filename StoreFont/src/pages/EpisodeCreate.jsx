import { Link, useLocation, useNavigate } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import { useForm } from "react-hook-form";
import SunEditor from "suneditor-react";
import "suneditor/dist/css/suneditor.min.css";
import axios from "axios";
import API_URL from "../url";
import Swal from "sweetalert2";
import Video from "../components/Video";
import videojs from "video.js";

const initialImg =
  "https://image.tmdb.org/t/p/w500//A4j8S6moJS2zNtRR8oWF08gRnL5.jpg"; // Initial image
const initialVid =
  "https://videos.pexels.com/video-files/20576968/20576968-hd_1920_1080_25fps.mp4";

function EpisodeCreate() {
  const [loading, setLoading] = useState(false);
  const playerRef = useRef(null);
  const navigate = useNavigate();
  const { register, handleSubmit} = useForm();
  const location = useLocation();
  const [description, setDescription] = useState("");

  const [imgUrl, setImgUrl] = useState(initialImg);
  const [videoUrl, setVideoUrl] = useState(initialVid);

  const videoJSOptions = {
    autoplay: false,
    controls: true,
    responsive: true,
    fluid: true,
    userActions: { hotkeys: true },
    playbackRates: [0.5, 1, 1.5, 2],
    sources: [
      {
        src: videoUrl,
        type: "video/mp4",
      }
    ]
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

  const handleVideoFileChange = (event) => {
    const newFile = event.target.files[0];
    const blobURL = URL.createObjectURL(newFile);
    setVideoUrl(blobURL);
    
  };

  // Handle form submission
  const onSubmit = async (data) => {
    // Upload img
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
        return
      }
    }
    else{
      data.thumbnail = initialImg
    }
    if (videoUrl != initialVid) {
      const res = await axios.post(
        `${API_URL}.upload/video`,
        {
          file: data.url[0],
        },
        {
          headers: {
            "Content-Type": "multipart/form-data",
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
        }
      );
      if (res.status == 201) {
        console.log(res);
        data.url = res.data.url;
      } else {
        Swal.fire({
          title: "Error",
          text: res,
          icon: "error",
        });
        return
      }

    }
    else{
      data.url = initialVid
    }

    setLoading(false);
    const token = localStorage.getItem("accessToken");
    data.description = description;
    data.film_id = parseInt(location.state);
    const is_active = data.is_active === "true";
    const is_deleted = data.is_deleted === "true";
    data.is_active = is_active;
    data.is_deleted = is_deleted;
    data.position = parseInt(data.position);
    data.updated_at = new Date();
    if (!data.duration) {
      data.duration = "100";
    }
    axios
      .post(`${API_URL}.episode`, data, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        console.log(res);
        Swal.fire({
          title: "Success",
          text: "Episode created successfully",
          icon: "success",
        });
        setTimeout(() => {
          navigate(-1);
        }, 2500);
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

  const handlePlayerReady = (player) => {
    playerRef.current = player;
    // You can handle player events here, for example:
    player.on('waiting', () => {
      videojs.log('player is waiting');
    });

    player.on('dispose', () => {
      videojs.log('player will dispose');
    });
  };
  return (
    <div className="w-full">
      <h1 className="text-2xl font-bold pl-2">Create new Episode</h1>
      <form
        action=""
        className="flex justify-between"
        onSubmit={handleSubmit(onSubmit)}
      >
        {/* left side */}
        <div className="p-2 space-y-2 w-1/3">
          <img
            src={imgUrl}
            alt={`film_poster`}
            className="w-full h-full object-cover"
          />
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
                {...register("title",{required: true})}
              />
            </div>
            <div className="flex flex-col">
              <label htmlFor="title_search">Title_Search:</label>
              <input
                type="text"
                name="title_search"
                className="rounded p-2 border border-gray-600 max-w-[250px]"
                {...register("title_search")}
              />
            </div>
            <div className="flex flex-col">
              <label htmlFor="slug">Slug:</label>
              <input
                type="text"
                name="slug"
                className="rounded p-2 border border-gray-600 max-w-[250px]"
                {...register("slug")}
              />
            </div>
            <div className="flex flex-col">
              <label htmlFor="position">Position:</label>
              <input
                type="number"
                name="position"
                className="rounded p-2 border border-gray-600 max-w-[250px]"
                {...register("position",{required: true})}
              />
            </div>
          </div>
          <div className="flex flex-col">
            <label htmlFor="description">Description:</label>
            <SunEditor
              name="description"
              setContents={description}
              onChange={(content) => setDescription(content)}
            />
          </div>
          <div className="flex flex-col gap-2">
            <label htmlFor="description">Video:</label>
            <Video
              options={videoJSOptions} onReady={handlePlayerReady} 
            />
            <input
              type="file"
              multiple={false}
              {...register("url", { onChange: handleVideoFileChange })}
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
              />
            </div>
            <div className="flex flex-col">
              <label htmlFor="updated_at">Updated At:</label>
              <input
                type="text"
                name="updated_at"
                className="rounded p-2 border border-gray-600  max-w-[250px]"
                readOnly
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
                <span className="animate-spin mr-2">Uploading...</span>
              ) : (
                "Save"
              )}
            </button>
          </div>
        </div>
      </form>
    </div>
  );
}

export default EpisodeCreate;
