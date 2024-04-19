package com.example.movieapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.BlogItemClickListener
import com.example.movieapp.R
import com.example.movieapp.adapter.ArticleBlogAdapter
import com.example.movieapp.adapter.model.Article
import com.example.movieapp.data.model.Articles
import com.example.movieapp.service.BlogViewModel
import com.example.movieapp.service.NetworkManager
import com.example.movieapp.ui.activity.BlogDetailActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlogFragment : Fragment(), BlogItemClickListener {
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

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            val blogView = inflater.inflate(R.layout.fragment_blog, container, false)
            // Get RecyclerView from layout
            val rcvBlog = blogView.findViewById<RecyclerView>(R.id.rcv_articles_blog)

            val progresssBar = blogView.findViewById<ProgressBar>(R.id.progressBar2)
            // Custom RecyclerView
            rcvBlog.layoutManager = LinearLayoutManager(
                this.requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            val DevideritemDeco = DividerItemDecoration(rcvBlog.context, LinearLayoutManager.VERTICAL)
            rcvBlog.addItemDecoration(DevideritemDeco)

//            val ds = mutableListOf<Article>()
//            ds.add(Article(1,"Test1","UserTest1","","","Content1",true,"1/1/2024"))
//            ds.add(Article(2,"Test2","UserTest2","","","Content2",true,"1/1/2024"))
//            ds.add(Article(3,"Test3","UserTest3","","","Content3",true,"1/1/2024"))
//            ds.add(Article(4,"Test4","UserTest4","","","Content4,",true,"1/1/2024"))
//            ds.add(Article(5,"Test5","UserTest5","","","Content5",true,"1/1/2024"))
//            ds.add(Article(6,"Test6","UserTest6","","","Content6",true,"1/1/2024"))

            var articles = Articles()
            var nextPage = 0
            val viewModel = ViewModelProvider(this).get(BlogViewModel::class.java)
            var adapter = ArticleBlogAdapter(articles.data, this)
            rcvBlog.adapter = adapter

            viewModel.getDataList().observe(viewLifecycleOwner) { newData ->
                progresssBar.visibility = View.GONE
                adapter.setData(newData.data)
                adapter.notifyDataSetChanged()
                articles = newData
                nextPage = articles.page + 1
            }

            rcvBlog.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    val limit = articles.limit
                    val totalEntries = articles.totalEntries
                    val maxPage = totalEntries / limit + if (totalEntries % limit == 0) 0 else 1

                    if(!recyclerView.canScrollVertically(1)){
                        Log.e("CheckPage",nextPage.toString())
                        if(nextPage!= null && nextPage <= maxPage){
                            viewModel.LoadMoreArticle(nextPage)
                            nextPage++
                        }
                        Log.e("CheckPageAfter",nextPage.toString())
                    }

                }
            })

            // Inflate the layout for this fragment
            return blogView
        }

    override fun onItemClicked(article: Article) {
        val intent = Intent(requireContext(), BlogDetailActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("id",article.id)
        intent.putExtras(bundle)
        startActivity(intent)
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlogFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BlogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}