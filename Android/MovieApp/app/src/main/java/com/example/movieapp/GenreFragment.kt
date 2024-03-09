package com.example.movieapp

import MyAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

    private fun generateDataList(): List<GenreItem> {
        val dataList: MutableList<GenreItem> = ArrayList()
        dataList.add(GenreItem(1, "hành động"))
        dataList.add(GenreItem(2, "tình cảm"))
        dataList.add(GenreItem(3, "hành động tình cảm"))
        dataList.add(GenreItem(4, "hành động"))
        dataList.add(GenreItem(5, "hành động"))
        dataList.add(GenreItem(6, "hành động"))
        dataList.add(GenreItem(7, "hành động"))
        dataList.add(GenreItem(8, "hành động"))
        dataList.add(GenreItem(9, "hành động"))
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
        val view = inflater.inflate(R.layout.fragment_genre, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

        val spacing = 24
        recyclerView.addItemDecoration(GridSpacingItemDecoration(2, spacing, false))
        recyclerView.layoutManager = GridLayoutManager(view.context, 2)

        val dataList: List<GenreItem>? = generateDataList() // Tạo danh sách dữ liệu

        val adapter = dataList?.let { MyAdapter(it, R.layout.genre) } ?: MyAdapter(emptyList(),R.layout.genre)
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
}