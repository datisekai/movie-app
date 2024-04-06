import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useState, useRef } from "react";
import { useForm } from "react-hook-form";
import SunEditor from "suneditor-react";
import Swal from "sweetalert2";
import axios from "axios";
import API_URL from "../url";
import "suneditor/dist/css/suneditor.min.css";
import videojs from "video.js";
import Video from "../components/Video";
import ClipLoader from "react-spinners/ClipLoader";
function EpisodeDetail() {
  const [loading, setLoading] = useState(false);
  const { register, handleSubmit } = useForm();
  const location = useLocation();
  const playerRef = useRef(null);
  const episode = location.state;
  const initialImg = episode.thumbnail; // Initial image
  const initialVid = episode.url;
  const [description, setDescription] = useState(episode.description);
  const [videoUrl, setVideoUrl] = useState(initialVid);
  const [imgUrl, setImgUrl] = useState(initialImg);
  const navigate = useNavigate();
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
      },
    ],
  };
  const handlePlayerReady = (player) => {
    playerRef.current = player;
    // You can handle player events here, for example:
    player.on("waiting", () => {
      videojs.log("player is waiting");
    });

    player.on("dispose", () => {
      videojs.log("player will dispose");
    });
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
        return;
      }
    } else {
      data.thumbnail = initialImg;
    }
    // upload video
    if (videoUrl != initialVid) {
      const res = await axios.post(
        `${API_URL}.upload/video`,
        {
          file: data.videoFile[0],
        },
        {
          headers: {
            "Content-Type": "multipart/form-data",
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
        }
      );
      if (res.status == 201) {
        data.url = res.data.url;
        setVideoUrl(res.data.url)
      } else {
        Swal.fire({
          title: "Error",
          text: res,
          icon: "error",
        });
        return;
      }
    } 
    else{
      data.url = initialVid
    }
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
    console.log(data.url);
    axios
      .put(`${API_URL}.episode/${episode.id}`, data, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        console.log(res);
        Swal.fire({
          title: "Success",
          text: "Episode updated successfully",
          icon: "success",
        }).then((result) => {
          if (result.isConfirmed) {
            navigate(-1)
          }
        })
        setLoading(false);
      })
      .catch((err) => {
        console.log(err);
        setLoading(false);
        Swal.fire({
          title: "Error",
          text: err.response.data.message,
          icon: "error",
        });
      });
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
  return (
    <div className="w-full">
      <h1 className="text-2xl font-bold pl-2">{episode.title}</h1>
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
                {...register("title")}
                defaultValue={episode.title}
              />
            </div>
            <div className="flex flex-col">
              <label htmlFor="slug">Slug:</label>
              <input
                type="text"
                name="slug"
                className="rounded p-2 border border-gray-600 max-w-[250px]"
                {...register("slug")}
                defaultValue={episode.slug}
              />
            </div>
            <div className="flex flex-col">
              <label htmlFor="position">Position:</label>
              <input
                type="text"
                name="position"
                className="rounded p-2 border border-gray-600 max-w-[250px]"
                {...register("position")}
                defaultValue={episode.position}
              />
            </div>

            <div className="flex flex-col">
              <label htmlFor="title">View:</label>
              <p className="rounded p-2 border border-gray-600  max-w-[250px]">
                {episode.view}
              </p>
            </div>
          </div>
          <div className="flex flex-col">
            <label htmlFor="description">Description:</label>
            <SunEditor
              name="description"
              setContents={description}
              onChange={(content) => setDescription(content)}
              defaultValue={episode.description}
            />
          </div>
          <div className="flex flex-col gap-2">
            <label htmlFor="description">Video:</label>
            <Video options={videoJSOptions} onReady={handlePlayerReady} />
            <input
              type="file"
              multiple={false}
              {...register("videoFile", { onChange: handleVideoFileChange })}
            />
            <input type="hidden"  {...register("url")}/>
          </div>
          {/* grid */}
          <div className="grid grid-cols-2">
            <div className="flex flex-col">
              <label htmlFor="is_active">Is Active:</label>
              <select
                name="is_active"
                id=""
                className="rounded p-2 border border-gray-600  max-w-[250px]"
                defaultValue={episode.is_active}
                {...register("is_active")}
              >
                <option value="true">True</option>
                <option value="false">False</option>
              </select>
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
                "Save"
              )}
            </span>
          </button>
          </div>
        </div>
      </form>
    </div>
  );
}

export default EpisodeDetail;
