import { useEffect } from "react";
import { Outlet, useNavigate } from "react-router-dom";
import Sidebar from "./Sidebar";
import Header from "./Header";
import axios from "axios";
import API_URL from "../../url";
export default function Layout() {
  const navigate = useNavigate();

  const getInformation = async (token) => {
    if (!token) {
      navigate("/login");
    }

    try {
      const result = await axios.get(`${API_URL}.auth/profile`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      localStorage.setItem("user", JSON.stringify(result.data));
    } catch (error) {
      navigate("/login");
    }

    return false;
  };

  useEffect(() => {
    getInformation(localStorage.getItem("accessToken"));
  }, []);
  return (
    <div className="bg-neutral-100 h-screen w-screen overflow-hidden flex flex-row">
      <Sidebar />
      <div className="flex flex-col flex-1">
        <Header />
        <div className="flex-1 p-4 min-h-0 overflow-auto">
          <Outlet />
        </div>
      </div>
    </div>
  );
}
