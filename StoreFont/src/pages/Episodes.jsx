/* eslint react/prop-types: 0 */
import { useEffect, useState } from "react";
import { closestCenter, DndContext } from "@dnd-kit/core";
import {
  arrayMove,
  SortableContext,
  verticalListSortingStrategy,
} from "@dnd-kit/sortable";
import { useLocation, useNavigate, Link } from "react-router-dom";
import SortableEpisode from "../components/SortableEpisode";
import API_URL from "../url";
import axios from "axios";
import Swal from "sweetalert2";

const Episodes = () => {
  const navigate = useNavigate();
  const location = useLocation();

  //Logic for drag and drop
  const [episodes, setEpisodes] = useState([]);
  const onDragEnd = (event) => {
    const { active, over } = event;
    if (active.id === over.id) {
      return;
    }
    setEpisodes((episodes) => {
      const oldIndex = episodes.findIndex((user) => user.id === active.id);
      const newIndex = episodes.findIndex((user) => user.id === over.id);
      const positionDifference = newIndex - oldIndex;
      console.log("oldIndex", oldIndex);
      console.log("newIndex", newIndex);
      episodes[oldIndex].position = episodes[newIndex].position;
      if (positionDifference > 0) {
        console.log("if > 0");
        for (let i = oldIndex + 1; i <= newIndex; i++) {
          console.log("before", episodes[i].position);
          episodes[i].position -= 1;
          console.log(episodes[i].title, episodes[i].position);
        }
      } else {
        console.log("if < 0");
        for (let i = newIndex; i < oldIndex; i++) {
          console.log("before", episodes[i].position);
          episodes[i].position += 1;
          console.log(episodes[i].title, episodes[i].position);
        }
      }

      return arrayMove(episodes, oldIndex, newIndex);
    });
  };
  const handleDetailsClick = (episode) => {
    console.log("clicked");
    navigate(`${episode.position}`, { state: { episode } }); // Pass episode data using useNavigate
  };
  useEffect(() => {
    const film_id = location.pathname.split("/")[2];
    axios
      .get(`${API_URL}.episode/film/${film_id}`)
      .then((res) => {
        console.log(res.data);
        setEpisodes(res.data.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);
  function deleteEpisode(id) {
    console.log("delete", id);
    Swal.fire({
      title: "Do you want to delete this episode?",
      showDenyButton: true,
      confirmButtonText: "Yes",
      denyButtonText: "No",
    }).then((result) => {
      if (result.isConfirmed) {
        axios
          .delete(`${API_URL}.episode/${id}`,{
            headers: {
              Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
            }
          })
          .then((res) => {
            console.log(res.data);
            Swal.fire("Deleted!", "", "success");
            setEpisodes(episodes.filter((episode)=>episode.id!==id))
          })
          .catch((err) => {
            console.log(err);
          });
      }
    });
  }
  return (
    <div>
      <button
        type="button"
        className="focus:outline-none text-white bg-purple-700 hover:bg-purple-800 focus:ring-4 focus:ring-purple-300 font-medium rounded-lg text-sm px-5 py-2.5 mb-2 dark:bg-purple-600 dark:hover:bg-purple-700 dark:focus:ring-purple-900"
      >
        <Link to={`create`} state={location.pathname.split("/")[2]}>
          Create new Episode
        </Link>
      </button>
      <div>Total: {episodes.length}</div>
      <div>
        <table className="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
          <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
            <tr>
              <th scope="col" className="px-6 py-3">
                Posistion
              </th>
              <th scope="col" className="px-6 py-3">
                Title
              </th>
              <th scope="col" className="px-6 py-3">
                Is Active
              </th>
              <th scope="col" className="px-6 py-3">
                Updated At
              </th>
              <th scope="col" className="px-6 py-3">
                Action
              </th>
            </tr>
          </thead>
          <tbody>
            <DndContext
              collisionDetection={closestCenter}
              onDragEnd={onDragEnd}
            >
              <SortableContext
                items={episodes}
                strategy={verticalListSortingStrategy}
              >
                {episodes.map((user) => (
                  <SortableEpisode
                    key={user.id}
                    episode={user}
                    onDetailsClick={handleDetailsClick}
                    onDeleteClick={deleteEpisode}
                  />
                ))}
              </SortableContext>
            </DndContext>
          </tbody>
        </table>
      </div>
    </div>
  );
};
export default Episodes;
