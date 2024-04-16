package com.example.movieapp.ui.fragment

import com.example.movieapp.adapter.CustomAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.Api.MyViewModel
import com.example.movieapp.DBHelper
import com.example.movieapp.GridSpacingItemDecoration
import com.example.movieapp.Helper
import com.example.movieapp.R
import com.example.movieapp.adapter.model.Movie
import com.example.movieapp.data.model.EpisodeHistoryDTO
import com.example.movieapp.data.model.Film1
import com.example.movieapp.data.model.FilmDTO
import com.example.movieapp.service.FavoriteViewModel
import com.example.movieapp.service.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

    private var ListFilmHistory: MutableList<FilmDTO> = ArrayList()

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

        val viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        //Check Database Has History
        val checkUserId = Helper.TokenManager.getId(requireContext())
        val db = DBHelper(requireContext())
        if(checkUserId != null){
            val listEpHistory = db.getListId(checkUserId)
            if(!listEpHistory.isNullOrEmpty()){
                viewModel.getListHistory().observe(viewLifecycleOwner){ newData ->
                    callAPI(progressbar, newData)
                    val adapter = dataList?.let { CustomAdapter(this.requireActivity(),it, R.layout.card, 480, 480, true) }
                    recyclerView.adapter = adapter
                    adapter?.notifyDataSetChanged()
                }
                if(viewModel.getListHistory().value == null){
                    viewModel.CallGetHistory(requireContext())
                }
            }
            else{
                val viewNoItem: TextView = view.findViewById(R.id.viewNoItem)
                viewNoItem.visibility = View.VISIBLE
                progressbar.visibility = View.GONE
            }
        }
        //callAPI(progressbar)




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
    fun callAPI( progressbar: ProgressBar, list: List<FilmDTO>){
        Log.e("CALL HISTORY API12",list.toString())
        for (o in list){
            dataList.add(Movie(o.id, o.thumbnail, o.title, o.description.toString(), o.isRequiredPremium))
        }

        progressbar.visibility = View.GONE

    }
}

data class EpisodeIdsWrapper(val episode_ids: List<Int>)