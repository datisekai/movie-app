package com.example.movieapp.ui.fragment

import com.example.movieapp.adapter.CustomAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.GridSpacingItemDecoration
import com.example.movieapp.R
import com.example.movieapp.model.Movie

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

    private fun generateDataList(): List<Movie> {
        val dataList: MutableList<Movie> = ArrayList()
        dataList.add(Movie(R.drawable.anime1, "Chú thuật hồi chiến", "2022"))
        dataList.add(Movie(R.drawable.anime2, "abc 2", "2023"))
        dataList.add(Movie(R.drawable.anime3, "abc 3", "2024"))
        dataList.add(Movie(R.drawable.anime1, "Chú thuật hồi chiến", "2022"))
        dataList.add(Movie(R.drawable.anime2, "abc 2", "2023"))
        dataList.add(Movie(R.drawable.anime3, "abc 3", "2024"))
        dataList.add(Movie(R.drawable.anime1, "Chú thuật hồi chiến", "2022"))
        dataList.add(Movie(R.drawable.anime2, "abc 2", "2023"))
        dataList.add(Movie(R.drawable.anime3, "abc 3", "2024"))
        // Thêm các phần tử khác vào danh sách dữ liệu
        return dataList
    }

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

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val spacing = 24
        recyclerView.addItemDecoration(GridSpacingItemDecoration(2, spacing, false))

        recyclerView.layoutManager = GridLayoutManager(view.context, 2)

        val dataList: List<Movie>? = generateDataList() // Tạo danh sách dữ liệu

        val adapter = dataList?.let { CustomAdapter(it, R.layout.card, 480, 480, true) } ?: CustomAdapter(emptyList(),
            R.layout.card, 480, 480, true)
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
}