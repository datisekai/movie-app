import React from "react";
import { Link, useLocation } from "react-router-dom";
import { useState } from "react";
import { useForm } from "react-hook-form";
import SunEditor from "suneditor-react";
import "suneditor/dist/css/suneditor.min.css";
function EpisodeDetail() {
  const { register, handleSubmit, control, setValue, getValues } = useForm();
  const location = useLocation();
  const episode = location.state;
  const [description, setDescription] = useState(episode.description);

  // Handle form submission
  const onSubmit = (data) => {
    data.description = description;
    console.log(data);
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
            src={
              "https://image.tmdb.org/t/p/w500//A4j8S6moJS2zNtRR8oWF08gRnL5.jpg"
            }
            alt={`${episode} thumbnail`}
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
                defaultValue={episode.title}
              />
            </div>
            <div className="flex flex-col">
              <label htmlFor="title_search">Title_Search:</label>
              <input
                type="text"
                name="title_search"
                className="rounded p-2 border border-gray-600 max-w-[250px]"
                {...register("title_search")}
                defaultValue={episode.title_search}
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
            <div className="flex flex-col">
              <label htmlFor="url">URL:</label>
              <input
                name="url"
                type="text"
                className="rounded p-2 border border-gray-600  max-w-[250px]"
                defaultValue={episode.url}
                {...register("url")}
              >
        
              </input>
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
            <div className="flex flex-col">
              <label htmlFor="is_deleted">Is Deleted:</label>
              <select
                name="is_deleted"
                id=""
                className="rounded p-2 border border-gray-600  max-w-[250px]"
                defaultValue={episode.is_deleted}
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
                defaultValue={episode.created_at}
              />
            </div>
            <div className="flex flex-col">
              <label htmlFor="updated_at">Updated At:</label>
              <input
                type="text"
                name="updated_at"
                className="rounded p-2 border border-gray-600  max-w-[250px]"
                readOnly
                defaultValue={episode.updated_at}
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
          </div>
        </div>
      </form>
    </div>
  );
}

export default EpisodeDetail;
