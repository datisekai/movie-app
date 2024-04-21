import {
  HiOutlineViewGrid,
  HiOutlineCube,
  HiOutlineUsers,
  HiOutlineDocumentText,
  HiOutlineQuestionMarkCircle,
  HiOutlineCog,
} from "react-icons/hi";
import { BiCategory } from "react-icons/bi";
import { FaMoneyBill } from "react-icons/fa";
export const DASHBOARD_SIDEBAR_LINKS = [
  {
    key: "dashboard",
    label: "Trang chủ",
    path: "/",
    icon: <HiOutlineViewGrid />,
  },
  {
    key: "movies",
    label: "Quản lý phim",
    path: "/movies",
    icon: <HiOutlineCube />,
  },
  {
    key: "users",
    label: "Quản lý người dùng",
    path: "/users",
    icon: <HiOutlineUsers />,
  },
  {
    key: "categories",
    label: "Quản lý danh mục",
    path: "/categories",
    icon: <BiCategory />,
  },
  {
    key: "articles",
    label: "Quản lý bài viết",
    path: "/articles",
    icon: <HiOutlineDocumentText />,
  },
  {
    key: "payments",
    label: "Quản lý thanh toán",
    path: "/payments",
    icon: <FaMoneyBill />,
  },
];

export const DASHBOARD_SIDEBAR_BOTTOM_LINKS = [
  {
    key: "settings",
    label: "Settings",
    path: "/settings",
    icon: <HiOutlineCog />,
  },
  {
    key: "support",
    label: "Help & Support",
    path: "/support",
    icon: <HiOutlineQuestionMarkCircle />,
  },
];
