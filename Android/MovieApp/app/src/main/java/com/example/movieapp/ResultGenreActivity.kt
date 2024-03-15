package com.example.movieapp

import MyAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ResultGenreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_genre)

        val bundle = intent.extras
        if (bundle != null) {
            val id = bundle.getInt("id")
            val name = bundle.getString("name")
            val textViewResultGenre= findViewById<TextView>(R.id.textViewResultGenre)
            textViewResultGenre.text ="Genre: " +name.toString()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val spacing = 24
        recyclerView.addItemDecoration(GridSpacingItemDecoration(2, spacing, false))

        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val dataList: List<MovieItem>? = generateDataList()

        val adapter = dataList?.let { MyAdapter(it, R.layout.card, 480, 480, true) } ?: MyAdapter(emptyList(), R.layout.card, 480, 480, true)
        recyclerView.adapter = adapter

        val imageView = findViewById<ImageView>(R.id.back)

        imageView.setOnClickListener {
            finish()
        }
    }
    private fun generateDataList(): List<MovieItem> {
        val dataList: MutableList<MovieItem> = ArrayList()
        dataList.add(MovieItem(R.drawable.anime1, "Chú thuật hồi chiến", "2022"))
        dataList.add(MovieItem(R.drawable.anime2, "abc 2", "2023"))
        dataList.add(MovieItem(R.drawable.anime3, "abc 3", "2024"))
        // Thêm các phần tử khác vào danh sách dữ liệu
        return dataList
    }
}