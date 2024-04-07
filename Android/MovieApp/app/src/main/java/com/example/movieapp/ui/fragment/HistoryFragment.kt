package com.example.movieapp.ui.fragment

import com.example.movieapp.adapter.CustomAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.GridSpacingItemDecoration
import com.example.movieapp.R
import com.example.movieapp.adapter.model.Movie
import com.example.movieapp.data.model.Film1
import com.example.movieapp.data.model.FilmDTO
import com.example.movieapp.service.FavoriteViewModel


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class HistoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var currentPage = 1
    private var totalEntries = 0
    private var dataList: MutableList<Movie> = ArrayList()
    private lateinit var adapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        dataList.clear()

        val progressbar: ProgressBar = view.findViewById(R.id.progressBar)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val spacing = 24
        recyclerView.addItemDecoration(GridSpacingItemDecoration(2, spacing, false))

        recyclerView.layoutManager = GridLayoutManager(view.context, 2)

        callAPI(progressbar)

        val adapter = dataList?.let { CustomAdapter(it, R.layout.card, 480, 480, true) }
        recyclerView.adapter = adapter


        return view

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    fun callAPI( progressbar: ProgressBar){

            for (o in initData()){
                dataList.add(Movie(o.id, o.thumbnail, o.title, o.description.toString(), o.isRequiredPremium))
            }

            progressbar.visibility = View.GONE

    }
    fun initData() : List<FilmDTO>{
        val dataList: MutableList<FilmDTO> = ArrayList()
        dataList.add(
            FilmDTO(
                1,
                "film-1",
                "Film 1",
                "Film 1",
                "Description 1",
                100,
                "thumbnail-1.jpg",
                "Action",
                "Released",
                false,
                "Director 1",
                "Location 1",
                true
            )
        )
        dataList.add(
            FilmDTO(
                2,
                "film-2",
                "Film 2",
                "Film 2",
                "Description 2",
                200,
                "thumbnail-2.jpg",
                "Drama",
                "Released",
                true,
                "Director 2",
                "Location 2",
                true
            )
        )
        dataList.add(
            FilmDTO(
                3,
                "film-3",
                "Film 3",
                "Film 3",
                "Description 3",
                150,
                "thumbnail-3.jpg",
                "Comedy",
                "Released",
                false,
                "Director 3",
                "Location 3",
                false
            )
        )
        return dataList
    }
}