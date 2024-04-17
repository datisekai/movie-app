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
import com.example.movieapp.RoundCornerTransformationPicasso
import com.example.movieapp.databinding.FragmentBlogDetailBinding
import com.example.movieapp.service.ArticleDetailsViewModel
import com.squareup.picasso.Picasso

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
                val scale: Float = this.resources.displayMetrics.density
                val borderWidthdp = (2 * scale + 0.5f).toInt()
                val whiteColor = 0xFFFFFFFF.toInt()
                Picasso.get()
                    .load(newData.data.thumbnail)
                    .error(R.drawable.default_esopide)
                    .resize(850, 0)
                    .centerCrop()
                    .transform(RoundCornerTransformationPicasso(30f, borderWidthdp.toFloat(), whiteColor))
                    .into(binding.articleImg)
//                Glide.with(this)
//                    .load(newData.data.thumbnail)
//                    .error(R.drawable.default_esopide)
//                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
//                    .apply(RequestOptions().override(800, Integer.MAX_VALUE))
//                    .into(binding.articleImg)
                //Set content for WebView
                val webView = binding.blogDetailWebview
                val contentWebView = newData.data.content
                webView.loadData(contentWebView,"text/html", "UTF-8")
                //Handle links in webview without open another browser
                webView.webViewClient = WebViewClient()
                webView.settings.domStorageEnabled = true
                //Set CSS for webview
                val css = "<style type='text/css'>body { color: #ffffff; background-color:#18181B;}</style>"
                val webViewContent = "<html><head>$css</head><body>$contentWebView</body></html>"
                webView.loadDataWithBaseURL(null, webViewContent, "text/html", "UTF-8", null)
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