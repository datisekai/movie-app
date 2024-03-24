package com.example.movieapp.ui.fragment

import com.example.movieapp.adapter.CustomAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.GridSpacingItemDecoration
import com.example.movieapp.R
import com.example.movieapp.ui.activity.SearchActivity
import com.example.movieapp.adapter.model.CardHome
import com.example.movieapp.adapter.model.Movie
import com.example.movieapp.data.model.Film
import com.example.movieapp.data.model.Film1
import com.example.movieapp.data.model.FilmDTO
import com.example.movieapp.ui.activity.HomePage_Activity

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
    private fun generateDataList(): List<CardHome> {
        val dataList: MutableList<CardHome> = ArrayList()
        dataList.add(CardHome(R.id.recycler_view1,480, 480, false, generateDataListMovie1()))
        dataList.add(CardHome(R.id.recycler_view2,640, 320, false, generateDataListMovie()))
        dataList.add(CardHome(R.id.recycler_view3, 480, 720, false, generateDataListMovie()))
        dataList.add(CardHome(R.id.recycler_view4, 480, 480, true , generateDataListMovie()))
        // Thêm các phần tử khác vào danh sách dữ liệu
        return dataList
    }
    private fun generateDataListMovie(): List<Movie> {
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

    private fun generateDataListMovie1() : List<Movie>{
        Log.e("DATA","run")
        val dataList: MutableList<Movie> = ArrayList()
        val tmp :  MutableList<FilmDTO> = mutableListOf()
        tmp.addAll(HomePage_Activity.filmData.listFilm)
        for (o in tmp){
            Log.e("DATA",o.title.toString())
           dataList.add(Movie(R.drawable.anime1, o.title.toString(),"2023"))
        }
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

            val dataList: List<Movie>? = data.movieItem // Tạo danh sách dữ liệu

            val adapter = dataList?.let { CustomAdapter(it,
                R.layout.card, data.widthCard, data.heightCard, data.isBorderImage) } ?: CustomAdapter(emptyList(),
                R.layout.card, data.widthCard, data.heightCard, data.isBorderImage)
            recyclerView1.adapter = adapter
        }

        val editText = view.findViewById<EditText>(R.id.editTextSearch)
        editText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                val intent = Intent(view.context, SearchActivity::class.java)
                intent.putExtra("q", editText.text.toString())
                startActivity(intent)
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