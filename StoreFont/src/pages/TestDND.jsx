import { useState } from "react";
import { closestCenter, DndContext } from "@dnd-kit/core";
import {
  arrayMove,
  SortableContext,
  useSortable,
  verticalListSortingStrategy,
} from "@dnd-kit/sortable";
import { CSS } from "@dnd-kit/utilities";
import ReactPaginate from "react-paginate";
import { useLocation, useNavigate,Link } from "react-router-dom";
const dummy = [
  {
    id: 1,
    slug: "episode-1",
    title: "Episode 1: The Beginning",
    title_search: "episode beginning",
    description: "<p>This is the first episode of our series.</p>",
    view: 1000,
    thumbnail: "https://example.com/thumbnails/episode1.jpg",
    url: "https://example.com/episodes/episode-1",
    duration: "00:25:00",
    position: 1,
    film_id: 1,
    is_active: true,
    is_deleted: false,
    created_at: "2023-01-15",
    updated_at: "2023-01-20",
  },
  {
    id: 2,
    slug: "episode-2",
    title: "Episode 2: The Encounter",
    title_search: "episode encounter",
    description: "<p>This is the second episode of our series.</p>",
    view: 800,
    thumbnail: "https://example.com/thumbnails/episode2.jpg",
    url: "https://example.com/episodes/episode-2",
    duration: "00:30:00",
    position: 2,
    film_id: 1,
    is_active: true,
    is_deleted: false,
    created_at: "2023-02-01",
    updated_at: "2023-02-05",
  },
  {
    id: 3,
    slug: "episode-3",
    title: "Episode 3: The Discovery",
    title_search: "episode discovery",
    description: "<p>This is the third episode of our series.</p>",
    view: 1200,
    thumbnail: "https://example.com/thumbnails/episode3.jpg",
    url: "https://example.com/episodes/episode-3",
    duration: "00:35:00",
    position: 3,
    film_id: 1,
    is_active: true,
    is_deleted: false,
    created_at: "2023-02-15",
    updated_at: "2023-02-20",
  },
  {
    id: 4,
    slug: "episode-4",
    title: "Episode 4: The Journey Continues",
    title_search: "episode journey continues",
    description: "<p>This is the fourth episode of our series.</p>",
    view: 900,
    thumbnail: "https://example.com/thumbnails/episode4.jpg",
    url: "https://example.com/episodes/episode-4",
    duration: "00:28:00",
    position: 4,
    film_id: 1,
    is_active: true,
    is_deleted: false,
    created_at: "2023-03-01",
    updated_at: "2023-03-05",
  },
  {
    id: 5,
    slug: "episode-5",
    title: "Episode 5: The Revelation",
    title_search: "episode revelation",
    description: "<p>This is the fifth episode of our series.</p>",
    view: 1100,
    thumbnail: "https://example.com/thumbnails/episode5.jpg",
    url: "https://example.com/episodes/episode-5",
    duration: "00:32:00",
    position: 5,
    film_id: 1,
    is_active: true,
    is_deleted: false,
    created_at: "2023-03-15",
    updated_at: "2023-03-20",
  },
  {
    id: 6,
    slug: "episode-6",
    title: "Episode 6: The Climax",
    title_search: "episode climax",
    description: "<p>This is the sixth episode of our series.</p>",
    view: 1500,
    thumbnail: "https://example.com/thumbnails/episode6.jpg",
    url: "https://example.com/episodes/episode-6",
    duration: "00:40:00",
    position: 6,
    film_id: 1,
    is_active: true,
    is_deleted: false,
    created_at: "2023-04-01",
    updated_at: "2023-04-05",
  },
  {
    id: 7,
    slug: "episode-7",
    title: "Episode 7: The Resolution",
    title_search: "episode resolution",
    description: "<p>This is the seventh episode of our series.</p>",
    view: 1300,
    thumbnail: "https://example.com/thumbnails/episode7.jpg",
    url: "https://example.com/episodes/episode-7",
    duration: "00:38:00",
    position: 7,
    film_id: 1,
    is_active: true,
    is_deleted: false,
    created_at: "2023-04-15",
    updated_at: "2023-04-20",
  },
  {
    id: 8,
    slug: "episode-8",
    title: "Episode 8: The Conclusion",
    title_search: "episode conclusion",
    description: "<p>This is the eighth episode of our series.</p>",
    view: 1400,
    thumbnail: "https://example.com/thumbnails/episode8.jpg",
    url: "https://example.com/episodes/episode-8",
    duration: "00:42:00",
    position: 8,
    film_id: 1,
    is_active: true,
    is_deleted: false,
    created_at: "2023-05-01",
    updated_at: "2023-05-05",
  },
];
const SortableEpisode = ({ episode }) => {
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
      {...attributes}
      {...listeners}
    >
      <td className="px-6 py-4">{episode.position}</td>
      <td className="px-6 py-4">{episode.title}</td>
      <td className="px-6 py-4">{episode.is_active.toString()}</td>
      <td className="px-6 py-4">{episode.updated_at}</td>
      <td className="space-x-2 flex justify-start">
        <Link to={`${episode.position}`}
          state={episode}
          className="focus:outline-none text-white bg-purple-700 hover:bg-purple-800 focus:ring-4 focus:ring-purple-300 font-medium rounded-lg text-sm px-5 py-2.5 mb-2 dark:bg-purple-600 dark:hover:bg-purple-700 dark:focus:ring-purple-900">
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

const Episodes = () => {
  const itemsPerPage = 5;
  // boilerplate for pagination
  const [itemOffset, setItemOffset] = useState(0);
  const location = useLocation();
  const navigate = useNavigate();

  // Simulate fetching items from another resources.
  // (This could be items from props; or items loaded in a local state
  // from an API endpoint with useEffect and useState)
  const endOffset = itemOffset + itemsPerPage;
  console.log(`Loading items from ${itemOffset} to ${endOffset}`);
  const currentItems = dummy.slice(itemOffset, endOffset);
  const pageCount = Math.ceil(dummy.length / itemsPerPage);

  // Invoke when user click to request another page.
  const handlePageClick = (event) => {
    const newOffset = (event.selected * itemsPerPage) % dummy.length;
    console.log(
      `User requested page number ${event.selected}, which is offset ${newOffset}`
    );
    setItemOffset(newOffset);
  };

  //  end boilerplate
  const [episodes, setEpisodes] = useState(dummy);
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

  return (
    <div >
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
                  <SortableEpisode key={user.id} episode={user} />
                ))}
              </SortableContext>
            </DndContext>
          </tbody>
        </table>
      </div>
      <ReactPaginate
        activeClassName="bg-red-600 hover:bg-red-700 transiton-colors duration-300 text-white 	"
        previousClassName="border  border-black py-1 md:px-5  hover:text-black transition-all duration-300 md:text-xl text-xs px-2"
        nextClassName="border  border-black py-1 md:px-5  hover:text-black transition-all duration-300 md:text-xl text-xs px-2"
        disabledClassName="bg-gray-400  md:text-lg text-sm px-2 text-white"
        pageClassName="border md:text-xl text-xs  border-black p-1 md:px-4  hover:text-black transition-all duration-300 px-2"
        breakLabel="..."
        nextLabel="Next >"
        onPageChange={handlePageClick}
        pageRangeDisplayed={5}
        pageCount={pageCount}
        previousLabel="< Previous"
        renderOnZeroPageCount={null}
        className="flex gap-2  w-full justify-center items-center py-2"
      />
    </div>
  );
};
export default Episodes;
