package com.example.movieapp.ui.fragment

import com.example.movieapp.adapter.CustomAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.GridSpacingItemDecoration
import com.example.movieapp.R
import com.example.movieapp.adapter.model.Genre
import com.example.movieapp.adapter.model.PaymentHistory
import com.example.movieapp.service.GenreViewModel
import com.example.movieapp.service.PaymentViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GenreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GenreFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var currentPage = 1
    private var totalEntries = 0
    private var dataList: MutableList<Genre> = ArrayList()
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
        val view = inflater.inflate(R.layout.fragment_genre, container, false)

        dataList.clear()

        val progressbar: ProgressBar = view.findViewById(R.id.progressBar)
        val viewModel = ViewModelProvider(this).get(GenreViewModel::class.java)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

        val spacing = 24
        recyclerView.addItemDecoration(GridSpacingItemDecoration(2, spacing, false))
        recyclerView.layoutManager = GridLayoutManager(view.context, 2)

        callAPI(viewModel,  progressbar)

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
                    callAPI(viewModel, progressbar)
                }
            }
        })


        adapter = dataList?.let { CustomAdapter(it, R.layout.genre, 0, 0, true) }!!
        recyclerView.adapter = adapter

        return view
        // Inflate the layout for this fragment
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GenreFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GenreFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    fun callAPI(viewModel: GenreViewModel, progressbar: ProgressBar){
        viewModel.getListGenre(currentPage).observe(viewLifecycleOwner) { payments ->

            totalEntries= payments.totalEntries

            for (o in payments.data){
                dataList.add(Genre(o.id , o.title))
            }

            progressbar.visibility = View.GONE

            adapter?.notifyDataSetChanged()
        }
    }
}