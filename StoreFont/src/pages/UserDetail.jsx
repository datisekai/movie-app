import { useLocation,Link, useNavigate } from "react-router-dom";
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
  const [loading,setLoading] = useState(false)
  const navigate = useNavigate();
  // Handle form submission
  const onSubmit = (data) => {
    setLoading(true)
    const token = localStorage.getItem("accessToken");
    const is_active = data.is_active === "true";
    const roles = ["admin"];
    data = {
      ...data,
      is_active,
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
          confirmButtonText: 'OK',
          showCancelButton: false,  
        }).then((result) => {
          if (result.isConfirmed) {
            navigate(-1)
          }
        })
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
      })
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
            
          />
        </div>
        <div className="flex flex-col">
          <label htmlFor="plan">Plan:</label>
          <select
            name="plan"
            id=""
            className="rounded p-2 border border-gray-600  max-w-[250px]"
            defaultValue={user.plan}
            {...register("plan")}
          >
            <option value="free">Free</option>
            <option value="Premium"> Premium</option>
          </select>
        </div>
        <div className="flex flex-col">
          <label htmlFor="role">Role:</label>
          <select
            name="role"
            id=""
            className="rounded p-2 border border-gray-600  max-w-[250px]"
            defaultValue={user.role}
            {...register("role")}
          >
            <option value="user">User</option>
            <option value="admin">Admin</option>
          </select>
        </div>
        <div className="flex flex-col">
          <label htmlFor="is_active">Is Active:</label>
          <select
            name="is_active"
            id=""
            className="rounded p-2 border border-gray-600  max-w-[250px]"
            defaultValue={user.is_active}
            {...register("is_active")}
          >
            <option value="true">True</option>
            <option value="false">False</option>
          </select>
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
