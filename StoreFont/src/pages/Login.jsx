import { useState } from "react";
import Header from "../components/AuthHeader";
import { loginFields } from "../lib/constants/authFields";
import AuthInput from "../components/AuthInput";
const fields = loginFields;
let fieldsState = {};
fields.forEach((field) => (fieldsState[field.id] = ""));

function Login() {
  const [loginState, setLoginState] = useState(fieldsState);
  const handleChange = (e) => {
    setLoginState({ ...loginState, [e.target.id]: e.target.value });
  };
  const handleSubmit = (e) => {
    e.preventDefault();
    console.log(loginState);
    setLoginState(fieldsState);
  }
  return (
    <div className="">
      <Header
        heading="Login to your account"
        paragraph="Don't have an account yet? "
        linkName="Signup"
        linkUrl="/signup"
      />
      <div className="flex flex-col items-center">
        <form className="mt-8 space-y-6 grow w-1/3">
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
        </form>

        {/* extra */}
        <div className="flex items-center justify-between w-1/3 py-2 mt-5 ">
          <div className="flex items-center">
            <input
              id="remember-me"
              name="remember-me"
              type="checkbox"
              className="h-4 w-4 text-purple-600 focus:ring-purple-500 border-gray-300 rounded"
            />
            <label
              htmlFor="remember-me"
              className="ml-2 block text-sm text-gray-900"
            >
              Remember me
            </label>
          </div>

          <div className="text-sm">
            <a
              href="#"
              className="font-medium text-purple-600 hover:text-purple-500"
            >
              Forgot your password?
            </a>
          </div>
        </div>
        <button
          type="button"
          className="group relative flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-purple-600 hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-purple-500 mt-5 w-1/3"
          onClick={handleSubmit}
        >
          Login
        </button>
      </div>
    </div>
  );
}

export default Login;
