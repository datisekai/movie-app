package com.example.movieapp.ui.activity

import com.example.movieapp.adapter.CustomAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.GridSpacingItemDecoration
import com.example.movieapp.R
import com.example.movieapp.adapter.model.Movie
import com.example.movieapp.adapter.model.PaymentHistory
import com.example.movieapp.service.GenreMovieViewModel
import com.example.movieapp.service.PaymentViewModel

class ResultGenreActivity : AppCompatActivity() {
    private var currentPage = 1
    private var totalEntries = 0
    private var dataList: MutableList<Movie> = ArrayList()
    private lateinit var adapter: CustomAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_genre)

        val bundle = intent.extras
        var id = -1
        if (bundle != null) {
            id = bundle.getInt("id")
            val name = bundle.getString("name")
            val textViewResultGenre= findViewById<TextView>(R.id.textViewResultGenre)
            textViewResultGenre.text ="Thể loại: " +name.toString()
        }

        dataList.clear()

        val progressbar: ProgressBar = findViewById(R.id.progressBar)
        val viewModel = ViewModelProvider(this).get(GenreMovieViewModel::class.java)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val spacing = 24
        recyclerView.addItemDecoration(GridSpacingItemDecoration(2, spacing, false))

        recyclerView.layoutManager = GridLayoutManager(this, 2)

        if(id !== -1){
            callAPI(viewModel, id,  progressbar)

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
                        callAPI(viewModel,id,  progressbar)
                    }
                }
            })

        }

        adapter = dataList?.let { CustomAdapter(it, R.layout.card, 480, 480, true) }!!
        recyclerView.adapter = adapter
        val imageView = findViewById<ImageView>(R.id.back)

        imageView.setOnClickListener {
            finish()
        }
    }

    fun callAPI(viewModel: GenreMovieViewModel, genreId: Int , progressbar: ProgressBar){
        viewModel.getListGenreMovie(genreId, currentPage).observe(this) { films ->

            totalEntries= films.totalEntries

            for (o in films.data){
                dataList.add(Movie(o.id, o.thumbnail, o.title, o.description.toString(), o.isRequiredPremium))
            }

            progressbar.visibility = View.GONE
            if(totalEntries ==0 ){
                val viewNoItem: TextView = findViewById(R.id.viewNoItem)
                viewNoItem.visibility = View.VISIBLE
            }
            adapter?.notifyDataSetChanged()
        }
    }
}