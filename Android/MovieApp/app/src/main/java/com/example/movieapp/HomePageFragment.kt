package com.example.movieapp

import MyAdapter
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomePageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
data class cardSize(val widthCard: Int, val heightCard: Int,val isBorderImage: Boolean)
class HomePageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    private fun generateDataList(): List<CardHomeItem> {
        val dataList: MutableList<CardHomeItem> = ArrayList()
        dataList.add(CardHomeItem(R.id.recycler_view1,480, 480, false, generateDataListMovie()))
        dataList.add(CardHomeItem(R.id.recycler_view2,640, 320, false, generateDataListMovie()))
        dataList.add(CardHomeItem(R.id.recycler_view3, 480, 720, false, generateDataListMovie()))
        dataList.add(CardHomeItem(R.id.recycler_view4, 480, 480, true , generateDataListMovie()))
        // Thêm các phần tử khác vào danh sách dữ liệu
        return dataList
    }
    private fun generateDataListMovie(): List<MovieItem> {
        val dataList: MutableList<MovieItem> = ArrayList()
        dataList.add(MovieItem(R.drawable.anime1, "Chú thuật hồi chiến", "2022"))
        dataList.add(MovieItem(R.drawable.anime2, "abc 2", "2023"))
        dataList.add(MovieItem(R.drawable.anime3, "abc 3", "2024"))
        dataList.add(MovieItem(R.drawable.anime1, "Chú thuật hồi chiến", "2022"))
        dataList.add(MovieItem(R.drawable.anime2, "abc 2", "2023"))
        dataList.add(MovieItem(R.drawable.anime3, "abc 3", "2024"))
        dataList.add(MovieItem(R.drawable.anime1, "Chú thuật hồi chiến", "2022"))
        dataList.add(MovieItem(R.drawable.anime2, "abc 2", "2023"))
        dataList.add(MovieItem(R.drawable.anime3, "abc 3", "2024"))
        // Thêm các phần tử khác vào danh sách dữ liệu
        return dataList
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)

        val spacing = 24
        for (data in generateDataList()){
            val recyclerView1 = view.findViewById<RecyclerView>(data.view)
            recyclerView1.addItemDecoration(GridSpacingItemDecoration(data.movieItem.size, spacing, false))
            recyclerView1.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)

            val dataList: List<MovieItem>? = data.movieItem // Tạo danh sách dữ liệu

            val adapter = dataList?.let { MyAdapter(it, R.layout.card, data.widthCard, data.heightCard, data.isBorderImage) } ?: MyAdapter(emptyList(), R.layout.card, data.widthCard, data.heightCard, data.isBorderImage)
            recyclerView1.adapter = adapter
        }



        val editText = view.findViewById<EditText>(R.id.editTextSearch)
        editText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                // Xử lý sự kiện khi nhấn phím Enter
//                performSearch(editText.text.toString())
//                val searchFragment = SearchFragment()
//                val fragmentManager = requireActivity().supportFragmentManager
//                fragmentManager.beginTransaction()
//                    .add(R.id.navHostFragment, searchFragment)
//                    .addToBackStack(null)
//                    .commit()
//                val navController = Navigation.findNavController(view)
//                navController.navigate(R.id.menu_Search)
                val navigationview = inflater.inflate(R.layout.fragment_home_page, container, false).findViewById<NavigationView>(R.id.navigationView)
                navigationview.itemIconTintList = null

                val navController = Navigation.findNavController(requireView())
                NavigationUI.setupWithNavController(navigationview, navController)
                navController.navigate(R.id.searchFragment)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomePageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomePageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}