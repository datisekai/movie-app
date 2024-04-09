import {
	HiOutlineViewGrid,
	HiOutlineCube,
	HiOutlineUsers,
	HiOutlineDocumentText,
	HiOutlineQuestionMarkCircle,
	HiOutlineCog
} from 'react-icons/hi'
import { BiCategory } from "react-icons/bi";
import { FaMoneyBill } from "react-icons/fa";
export const DASHBOARD_SIDEBAR_LINKS = [
	{
		key: 'dashboard',
		label: 'Dashboard',
		path: '/',
		icon: <HiOutlineViewGrid />
	},
	{
		key: 'movies',
		label: 'Movies',
		path: '/movies',
		icon: <HiOutlineCube />
	},
	{
		key: 'users',
		label: 'Users',
		path: '/users',
		icon: <HiOutlineUsers />
	},
	{
		key: 'categories',
		label: 'Categories',
		path: '/categories',
		icon: <BiCategory />,
	},
	{
		key: 'articles',
		label: 'Articles',
		path: '/articles',
		icon: <HiOutlineDocumentText />
	},
	{
		key: 'payments',
		label: 'Payments',
		path: '/payments',
		icon: <FaMoneyBill />
	}
]

export const DASHBOARD_SIDEBAR_BOTTOM_LINKS = [
	{
		key: 'settings',
		label: 'Settings',
		path: '/settings',
		icon: <HiOutlineCog />
	},
	{
		key: 'support',
		label: 'Help & Support',
		path: '/support',
		icon: <HiOutlineQuestionMarkCircle />
	}
]
