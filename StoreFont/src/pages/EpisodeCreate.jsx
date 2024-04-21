import { useLocation, useNavigate } from "react-router-dom";
import { useRef, useState } from "react";
import { useForm } from "react-hook-form";
import SunEditor from "suneditor-react";
import "suneditor/dist/css/suneditor.min.css";
import axios from "axios";
import API_URL from "../url";
import Swal from "sweetalert2";
import Video from "../components/Video";
import videojs from "video.js";
import ClipLoader from "react-spinners/ClipLoader";
import { generateSlug } from "../utils";
const initialImg =
  "https://image.tmdb.org/t/p/w500//A4j8S6moJS2zNtRR8oWF08gRnL5.jpg"; // Initial image
const initialVid =
  "https://videos.pexels.com/video-files/20576968/20576968-hd_1920_1080_25fps.mp4";

function EpisodeCreate() {
  const [loading, setLoading] = useState(false);
  const playerRef = useRef(null);
  const navigate = useNavigate();
  const { register, handleSubmit, setValue, getValues } = useForm();
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
      },
    ],
  };

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
        return;
      }
    } else {
      data.thumbnail = initialImg;
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
        return;
      }
    } else {
      data.url = initialVid;
    }

    setLoading(false);
    const token = localStorage.getItem("accessToken");
    data.description = description;
    data.film_id = parseInt(location.state);
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
          <div className="flex items-center justify-center w-full h-full">
            <label className="flex flex-col items-center justify-center w-full h-full border-2 border-gray-300 border-dashed rounded-lg cursor-pointer bg-gray-50 dark:hover:bg-bray-800 dark:bg-gray-700 hover:bg-gray-100 dark:border-gray-600 dark:hover:border-gray-500 dark:hover:bg-gray-600">
              <img
                src={imgUrl}
                alt="Preview"
                className="w-full h-full object-contain"
              />
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
                placeholder="Enter title..."
                name="title"
                className="rounded p-2 border border-gray-600  max-w-[250px]"
                {...register("title", { required: true })}
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
              <button
                className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 my-2 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800 w-fit"
                type="button"
                onClick={handleCreateSlug}
              >
                Generate Slug
              </button>
            </div>
          </div>
          <div className="flex flex-col">
            <label htmlFor="description">Description:</label>
            <SunEditor
              name="description"
              setContents={description}
              onChange={(content) => setDescription(content)}
              height="10rem"
              placeholder="Enter description..."
            />
          </div>
          <div className="flex flex-col gap-2">
            <label htmlFor="description">Video:</label>
            <Video options={videoJSOptions} onReady={handlePlayerReady} />
            <input
              type="file"
              multiple={false}
              {...register("url", { onChange: handleVideoFileChange })}
            />
          </div>
          {/* grid */}
          <div className="grid grid-cols-2">
            <div className="flex flex-col">
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

export default EpisodeCreate;
