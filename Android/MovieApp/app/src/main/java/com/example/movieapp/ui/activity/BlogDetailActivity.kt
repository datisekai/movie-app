package com.example.movieapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentBlogDetailBinding
import com.example.movieapp.service.ArticleDetailsViewModel

class BlogDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Get id From OnClick Blog
        val bundle = intent.extras
        val id = bundle?.getInt("id")
        //Get Layout Binding
        val binding: FragmentBlogDetailBinding = FragmentBlogDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Init ViewModel
        val viewModel = ViewModelProvider(this).get(ArticleDetailsViewModel::class.java)
        viewModel.GetArticleById(id).observe(this){newData ->
            if(newData != null){
                binding.articleProgressBar.visibility = View.GONE
                binding.articleTitle.text = newData.data.title
                //Set Image for ImageView
                if(newData.data.thumbnail.isNullOrBlank()){
                    Glide.with(this)
                        .load(R.drawable.default_esopide)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .apply(RequestOptions().override(600, Integer.MAX_VALUE))
                        .into(binding.articleImg)
                }
                else{
                    Glide.with(this)
                        .load(newData.data.thumbnail)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .apply(RequestOptions().override(800, Integer.MAX_VALUE))
                        .into(binding.articleImg)
                }
                //Set content for WebView
                val webView = binding.blogDetailWebview
                webView.loadData(newData.data.content,"text/html", "UTF-8")
                //Handle links in webview without open another browser
                webView.webViewClient = WebViewClient()
            }else{
                BackToBlogFragment()
            }
        }
    }

    fun clickBack(view: View){
        BackToBlogFragment()
    }

    private fun BackToBlogFragment(){
        //Return To Blog Fragment
        val intent = Intent(this, HomePage_Activity::class.java)
        intent.putExtra("typeFragment",2)
        startActivity(intent)
    }
}