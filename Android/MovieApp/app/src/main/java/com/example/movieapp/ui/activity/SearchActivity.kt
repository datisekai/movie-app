package com.example.movieapp.ui.activity

import com.example.movieapp.adapter.CustomAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.GridSpacingItemDecoration
import com.example.movieapp.R
import com.example.movieapp.adapter.model.Movie

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val receivedValue = intent.getStringExtra("q")
        val textSearch= findViewById<EditText>(R.id.textSearch)
        val textResultSearch= findViewById<TextView>(R.id.textResultSearch)
        textSearch.setText(receivedValue)
        textResultSearch.text="Search Results: " + receivedValue.toString()

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val spacing = 24
        recyclerView.addItemDecoration(GridSpacingItemDecoration(2, spacing, false))

        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val dataList: List<Movie>? = generateDataList() // Tạo danh sách dữ liệu

        val adapter = dataList?.let { CustomAdapter(it, R.layout.card, 480, 480, true) } ?: CustomAdapter(emptyList(),
            R.layout.card, 480, 480, true)
        recyclerView.adapter = adapter

        val imageView = findViewById<ImageView>(R.id.back)

        imageView.setOnClickListener {
            finish()
        }
    }
    private fun generateDataList(): List<Movie> {
        val dataList: MutableList<Movie> = ArrayList()
        dataList.add(Movie(R.drawable.anime1, "Chú thuật hồi chiến", "2022",1))
        dataList.add(Movie(R.drawable.anime2, "abc 2", "2023",1))
        dataList.add(Movie(R.drawable.anime3, "abc 3", "2024",1))
        dataList.add(Movie(R.drawable.anime1, "Chú thuật hồi chiến", "2022",1))
        dataList.add(Movie(R.drawable.anime2, "abc 2", "2023",1))
        dataList.add(Movie(R.drawable.anime3, "abc 3", "2024",1))
        dataList.add(Movie(R.drawable.anime1, "Chú thuật hồi chiến", "2022",1))
        dataList.add(Movie(R.drawable.anime2, "abc 2", "2023",1))
        dataList.add(Movie(R.drawable.anime3, "abc 3", "2024",1))
        // Thêm các phần tử khác vào danh sách dữ liệu
        return dataList
    }

}