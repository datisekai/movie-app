import React from 'react'
import { Link } from 'react-router-dom';
import {
    arrayMove,
    SortableContext,
    useSortable,
    verticalListSortingStrategy,
  } from "@dnd-kit/sortable";
import { CSS } from "@dnd-kit/utilities";
const SortableEpisode = ({ episode,onDetailsClick }) => {
    const { attributes, listeners, setNodeRef, transform, transition } =
      useSortable({ id: episode.id });
    const style = {
      transition,
      transform: CSS.Transform.toString(transform),
    };
  
    return (
      <tr
        key={episode.id}
        style={style}
        ref={setNodeRef}
      >
        <td className="px-6 py-4" {...attributes}
        {...listeners}>{episode.position}</td>
        <td className="px-6 py-4" {...attributes}
        {...listeners}>{episode.title}</td>
        <td className="px-6 py-4" {...attributes}
        {...listeners}>{episode.is_active.toString()}</td>
        <td className="px-6 py-4" {...attributes}
        {...listeners}>{episode.updated_at}</td>
        <td className="space-x-2 flex justify-start">
         
          <Link
            type="button"
            to={`${episode.position}`}
            state={episode}
            onClick={()=>onDetailsClick}
            className="focus:outline-none text-white bg-purple-700 hover:bg-purple-800 focus:ring-4 focus:ring-purple-300 font-medium rounded-lg text-sm px-5 py-2.5 mb-2 dark:bg-purple-600 dark:hover:bg-purple-700 dark:focus:ring-purple-900"
          >
            Details
          </Link>
          
          <button
            type="button"
            className="focus:outline-none text-white bg-red-700 hover:bg-red-800 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-900"
          >
            Delete
          </button>
        </td>
      </tr>
    );
  };

export default SortableEpisode