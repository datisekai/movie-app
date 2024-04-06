package com.example.movieapp.ui.activity

import com.example.movieapp.adapter.CustomAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.Api.MyViewModel
import com.example.movieapp.GridSpacingItemDecoration
import com.example.movieapp.R
import com.example.movieapp.adapter.model.Movie
import com.example.movieapp.data.model.Film1
import com.example.movieapp.data.model.FilmDTO
import com.example.movieapp.service.SearchViewModel
import com.example.movieapp.ui.fragment.HomePageFragment

class SearchActivity : AppCompatActivity() {
    private var currentPage = 1
    private var totalEntries = 0
    private var dataList: MutableList<Movie> = ArrayList()
    private lateinit var adapter: CustomAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        dataList.clear()

        val receivedValue = intent.getStringExtra("q")

        val textSearch= findViewById<EditText>(R.id.textSearch)
        textSearch.setText(receivedValue)

        val textResultSearch= findViewById<TextView>(R.id.textResultSearch)
        textResultSearch.text="Search Results: " + receivedValue.toString()

        val progressbar: ProgressBar = findViewById(R.id.progressBar)
        val viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        val spacing = 24
        recyclerView.addItemDecoration(GridSpacingItemDecoration(2, spacing, false))

        recyclerView.layoutManager = GridLayoutManager(this, 2)

        callAPI(viewModel, receivedValue.toString(), progressbar, recyclerView)

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
                    callAPI(viewModel, receivedValue.toString(), progressbar, recyclerView)
                }
            }
        })


        adapter = CustomAdapter(dataList, R.layout.card, 480, 480, true)
        recyclerView.adapter = adapter

        textSearch.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                dataList.clear()
                textResultSearch.text="Search Results: " + textSearch.text.toString()
                callAPI(viewModel, textSearch.text.toString(), progressbar, recyclerView)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }


        val imageView = findViewById<ImageView>(R.id.back)

        imageView.setOnClickListener {
            finish()
        }
    }
    fun callAPI(viewModel: SearchViewModel, receivedValue: String, progressbar: ProgressBar,recyclerView: RecyclerView){
        viewModel.getListFilm(receivedValue.toString(), currentPage).observe(this) { films ->

            totalEntries= films.totalEntries

            for (o in films.data){
                dataList.add(Movie(o.id ,o.thumbnail, o.title.toString(), o.description.toString(), o.isRequiredPremium))
            }

            progressbar.visibility = View.GONE

            adapter?.notifyDataSetChanged()
        }
    }
}