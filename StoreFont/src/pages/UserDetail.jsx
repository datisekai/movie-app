import { useLocation, Link, useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import API_URL from "../url";
import axios from "axios";
import Swal from "sweetalert2";
import { useState } from "react";
import ClipLoader from "react-spinners/ClipLoader";
function UserDetail() {
  const location = useLocation();
  const user = location.state;
  const { register, handleSubmit } = useForm();
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  // Handle form submission
  const onSubmit = (data) => {
    setLoading(true);
    const token = localStorage.getItem("accessToken");
    const roles = [data.roles];
    data = {
      ...data,
      roles,
    };
    console.log(data);
    axios
      .put(`${API_URL}.user/${user.id}`, data, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        console.log(res);
        Swal.fire({
          title: "Success",
          text: "User updated successfully",
          icon: "success",
          confirmButtonText: "OK",
          showCancelButton: false,
        }).then((result) => {
          if (result.isConfirmed) {
            navigate(-1);
          }
        });
      })
      .catch((err) => {
        Swal.fire({
          title: "Error",
          text: err.response.data.message,
          icon: "error",
        });
      })
      .finally(() => {
        setLoading(false);
      });
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-y-2">
        <div className="flex flex-col">
          <label htmlFor="email">Email:</label>
          <input
            type="text"
            name="email"
            className="rounded p-2 border border-gray-600  max-w-[250px]"
            defaultValue={user.email}
            {...register("email")}
          />
        </div>
        <div className="flex flex-col">
          <label htmlFor="fullname">Full Name:</label>
          <input
            type="text"
            name="fullname"
            className="rounded p-2 border border-gray-600  max-w-[250px]"
            defaultValue={user.fullname}
            {...register("fullname")}
          />
        </div>
        <div className="flex flex-col">
          <label htmlFor="password">Password:</label>
          <input
            type="password"
            name="password"
            className="rounded p-2 border border-gray-600  max-w-[250px]"
            defaultValue={user.password}
            {...register("password")}
          />
        </div>

        <div className="flex flex-col col-span-3">
          <label htmlFor="role">Role:</label>
          <select
            name="role"
            id=""
            className="rounded p-2 border border-gray-600  max-w-[250px]"
            defaultValue={user.role}
            {...register("role")}
          >
            <option value="free_user">Free User</option>
            <option value="premium_user">Premium User</option>
            <option value="admin">Admin</option>
          </select>
        </div>
        <div className="flex flex-col justify-center">
          <label className="inline-flex items-center cursor-pointer">
            <input
              type="checkbox"
              value=""
              className="sr-only peer"
              {...register("is_active")}
              defaultChecked={true}
              defaultValue={user.is_active}
            />
            <div className="relative w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 dark:peer-focus:ring-blue-800 rounded-full peer dark:bg-gray-700 peer-checked:after:translate-x-full rtl:peer-checked:after:-translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:start-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all dark:border-gray-600 peer-checked:bg-blue-600"></div>
            <span className="ms-3 text-sm font-medium text-gray-900 dark:text-gray-300">
              Is Active
            </span>
          </label>
        </div>
      </div>

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
      <Link
        to={`/history/${user.id}`}
        className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 my-2 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800 ml-2"
      >
        History
      </Link>
    </form>
  );
}

export default UserDetail;
