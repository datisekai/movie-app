import { useLocation } from "react-router-dom";
import { useForm } from "react-hook-form";
import { useEffect, useState } from "react";
import SunEditor from "suneditor-react";
import 'suneditor/dist/css/suneditor.min.css';
function CategoryDetail() {
  const [description,setDescription] = useState("");
  const location = useLocation();
  const category = location.state;
  const { register, handleSubmit, setValue } = useForm();

  // Handle form submission
  const onSubmit = (data) => {
    data.description = description;

    console.log(data);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="flex justify-between">
      {/* left side */}
      <div className="w-1/3">
        <h3>Thumbnail</h3>
        <img
          src={
            "https://images.unsplash.com/photo-1481349518771-20055b2a7b24?q=80&w=1000&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nnx8cmFuZG9tfGVufDB8fDB8fHww"
          }
          alt="article image"
          className=" w-96 h-96"
        />
        <input type="file" className="mt-2" />
        <div>
          <button
            type="submit"
            className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 my-2 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800 max-w-20"
          >
            Save
          </button>
        </div>
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
            {...register("id")}
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
            setContents={description}
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
        <div className="flex flex-col">
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
      </div>
    </form>
  );
}

export default CategoryDetail;
