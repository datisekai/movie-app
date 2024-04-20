package com.example.movieapp.ui.fragment

import com.example.movieapp.adapter.CustomAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.GridSpacingItemDecoration
import com.example.movieapp.R
import com.example.movieapp.adapter.model.Movie
import com.example.movieapp.service.FavoriteViewModel
import com.example.movieapp.service.GenreMovieViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavoriteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoriteFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        dataList.clear()

        val progressbar: ProgressBar = view.findViewById(R.id.progressBar)
        val viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val spacing = 24
        recyclerView.addItemDecoration(GridSpacingItemDecoration(2, spacing, false))

        recyclerView.layoutManager = GridLayoutManager(view.context, 2)

        callAPI(viewModel, view,  progressbar)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                val itemsPerPage = 10

                val totalPages = totalEntries / itemsPerPage + if (totalEntries % itemsPerPage == 0) 0 else 1
                val isLastPage = currentPage == totalPages

                if (isLastPage) {
                    return
                }

                // Kiểm tra nếu người dùng đang cuộn xuống dưới (dy > 0)
                if (dy > 0 && visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    // Đã cuộn đến cuối danh sách, gọi hàm loadMoreData để tải dữ liệu trang tiếp theo
                    currentPage++
                    callAPI(viewModel, view ,  progressbar)
                }
            }
        })


        adapter = dataList?.let { CustomAdapter(this.requireActivity(),it, R.layout.card, 0, 0, true) }!!
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
         * @return A new instance of fragment FavoriteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoriteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    fun callAPI(viewModel: FavoriteViewModel,view: View,  progressbar: ProgressBar){
        viewModel.getListGenreMovie(currentPage).observe(viewLifecycleOwner) { films ->

            totalEntries= films.totalEntries

            for (o in films.data){
                dataList.add(Movie(o.film.id, o.film.thumbnail, o.film.title, o.film.description.toString(), o.film.isRequiredPremium))
            }

            progressbar.visibility = View.GONE
            if(totalEntries ==0 ){
                val viewNoItem: TextView = view.findViewById(R.id.viewNoItem)
                viewNoItem.visibility = View.VISIBLE
            }
            adapter?.notifyDataSetChanged()
        }
    }
}