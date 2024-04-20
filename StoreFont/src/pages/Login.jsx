import { useState } from "react";
import Header from "../components/AuthHeader";
import { loginFields } from "../lib/constants/authFields";
import AuthInput from "../components/AuthInput";
import API_URL from "../url";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";
import ClipLoader from "react-spinners/ClipLoader";
const fields = loginFields;
let fieldsState = {};
fields.forEach((field) => (fieldsState[field.id] = ""));

function Login() {
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const [loginState, setLoginState] = useState(fieldsState);
  const handleChange = (e) => {
    setLoginState({ ...loginState, [e.target.id]: e.target.value });
  };
  const handleSubmit = (e) => {
    e.preventDefault();
    //  test acc
    // datly030102@gmail.com
    // datisekai
    setLoading(true);
    axios
      .post(`${API_URL}.auth/login`, loginState)
      .then((res) => {
        const user = res.data.data.user;
        if (user.roles.includes("admin")) {
          console.log(user.roles.includes("admin"))
          localStorage.setItem("accessToken", res.data.data.accessToken);
          localStorage.setItem("user", JSON.stringify(res.data.data.user));
          Swal.fire({
            title: "Success!",
            text: "Login Successfully!",
            icon: "success",
          }).then((result) => {
            if (result.isConfirmed) {
              navigate("/");
            }
          });
        }
        else{
          Swal.fire({
            title: "Error!",
            text: "Unauthorized!",
            icon: "error",
          });
        }
      })
      .catch((err) => {
        console.log(err);
        Swal.fire({
          title: "Error!",
          text: err.response.data.message,
          icon: "error",
        });
        
      })
      .finally(() => {
        setLoading(false);
      })
  };
  return (
    <div className="h-screen justify-center flex-col items-center flex">
      <Header heading="Login to your account" />
      <div className="flex flex-col items-center w-1/3">
        <form className="space-y-6 grow w-full">
          <div className="-space-y-px">
            {fields.map((field) => (
              <AuthInput
                key={field.id}
                handleChange={handleChange}
                value={loginState[field.id]}
                labelText={field.labelText}
                labelFor={field.labelFor}
                id={field.id}
                name={field.name}
                type={field.type}
                isRequired={field.isRequired}
                placeholder={field.placeholder}
              />
            ))}
          </div>

          <button
            type="submit"
            className="group relative flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-purple-600 hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-purple-500 mt-5 w-full"
            onClick={handleSubmit}
          >
            {loading ? (
              <ClipLoader
                color={"f"}
                size="1rem"
                loading={loading}
                aria-label="Loading Spinner"
                data-testid="loader"
              />
            ) : (
              "Login"
            )}
          </button>
        </form>
      </div>
    </div>
  );
}

export default Login;
