import { useLocation } from "react-router-dom";
import { useForm } from "react-hook-form";
import { useEffect } from "react";
function ArticleDetail() {
  const location = useLocation();
  const article = location.state;
  const { register, handleSubmit, setValue } = useForm();

  // Handle form submission
  const onSubmit = (data) => {
    console.log(data);
  };

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className="w-full flex justify-between"
    >
      {/* left side */}
      <div className="flex flex-col gap-4 w-1/3">
        <h3>Thumbnail</h3>
        <img
          src={
            "https://images.unsplash.com/photo-1481349518771-20055b2a7b24?q=80&w=1000&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nnx8cmFuZG9tfGVufDB8fDB8fHww"
          }
          alt="article image"
          className=" w-96 h-96"
        />
        <input type="file" />
        <div className="flex flex-col">
          <label htmlFor="desc">Description:</label>
          <textarea
            type="text"
            name="fullname_search"
            className="rounded p-2 border border-gray-600  "
            defaultValue={article.description}
            {...register("description")}
          />
        </div>
        <button
        type="submit"
        className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 my-2 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800 max-w-20"
      >
        Save
      </button>
      </div>
      {/* right side */}
      <div className=" flex flex-col w-2/3">
        <div className="w-full grid grid-cols-2 gap-2">
          <div className="flex flex-col h-fit">
            <label htmlFor="id">Id:</label>
            <input
              type="text"
              name="id"
              className="rounded p-2 border border-gray-600 max-w-[250px] "
              defaultValue={article.id}
              {...register("id")}
              readOnly
            />
          </div>
          <div className="flex flex-col h-fit">
            <label htmlFor="title">Title:</label>
            <input
              type="text"
              name="title"
              className="rounded p-2 border border-gray-600  max-w-[250px]"
              defaultValue={article.title}
              {...register("title")}
            />
          </div>
        </div>
        <div className="grid grid-cols-2 gap-2">
          <div className="flex flex-col h-fit">
            <label htmlFor="title_search">Title_Search:</label>
            <input
              type="text"
              name="title_search"
              className="rounded p-2 border border-gray-600  max-w-[250px]"
              defaultValue={article.title_search}
              {...register("title_search")}
            />
          </div>

          <div className="flex flex-col h-fit">
            <label htmlFor="is_active">Is Active:</label>
            <select
              name="is_active"
              id=""
              className="rounded p-2 border border-gray-600  max-w-[250px]"
              defaultValue={article.is_active}
              {...register("is_active")}
            >
              <option value="true">True</option>
              <option value="false">False</option>
            </select>
          </div>
        </div>
        <div className="grid grid-cols-2 gap-2">
          <div className="flex flex-col">
            <label htmlFor="is_deleted">Is Deleted:</label>
            <select
              name="is_deleted"
              id=""
              className="rounded p-2 border border-gray-600  max-w-[250px]"
              defaultValue={article.is_deleted}
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
              defaultValue={article.created_at}
            />
          </div>
        </div>
        <div className="flex flex-col">
          <label htmlFor="updated_at">Updated At:</label>
          <input
            type="text"
            name="updated_at"
            className="rounded p-2 border border-gray-600  max-w-[250px]"
            readOnly
            defaultValue={article.updated_at}
            {...register("updated_at")}
          />
        </div>
      </div>

      
    </form>
  );
}

export default ArticleDetail;
