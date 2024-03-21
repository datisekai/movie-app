package com.example.movieapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.adapter.ArticleBlogAdapter
import com.example.movieapp.model.Article

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlogFragment : Fragment() {
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

            val ds = mutableListOf<Article>()
            ds.add(Article("Test1","UserTest1","Content1","1/1/2024"))
            ds.add(Article("Test2","UserTest2","Content2","1/1/2024"))
            ds.add(Article("Test3","UserTest3","Content3","1/1/2024"))
            ds.add(Article("Test4","UserTest4","Content4","1/1/2024"))
            ds.add(Article("Test5","UserTest5","Content5","1/1/2024"))
            ds.add(Article("Test6","UserTest6","Content6","1/1/2024"))


            val blogAdapter = ArticleBlogAdapter(ds)
            rcvBlog.adapter = blogAdapter
            rcvBlog.layoutManager = LinearLayoutManager(
                this.requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )

            val DevideritemDeco = DividerItemDecoration(rcvBlog.context, LinearLayoutManager.VERTICAL)
            rcvBlog.addItemDecoration(DevideritemDeco)

            // Inflate the layout for this fragment
            return blogView
        }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
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