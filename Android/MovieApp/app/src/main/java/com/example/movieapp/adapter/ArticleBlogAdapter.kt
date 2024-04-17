package com.example.movieapp.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.BlogItemClickListener
import com.example.movieapp.R
import com.example.movieapp.RoundCornerTransformationPicasso
import com.example.movieapp.adapter.model.Article
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ArticleBlogAdapter (var ds:List<Article>, private val listener: BlogItemClickListener)
    :RecyclerView.Adapter<ArticleBlogAdapter.ArticleViewHolder>(){
        private var screenWidth = 0
    //Tạo class ViewHolder
    inner class ArticleViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val article_description: TextView = itemView.findViewById(R.id.article_description)
        val article_title: TextView = itemView.findViewById(R.id.article_title)
        val article_dateCreated: TextView = itemView.findViewById(R.id.article_dateCreated)
        val article_img: ImageView = itemView.findViewById(R.id.blog_article_img)

        val btnBlogClick: LinearLayout = itemView.findViewById(R.id.item_blog)
        fun bindData(item: Article, listener: BlogItemClickListener){
            //SetOnClick to Detail
            btnBlogClick.setOnClickListener{
                val i = item
                listener.onItemClicked(i)
            }
            //Set image Blog
            val desiredWidth = (screenWidth * 0.8).toInt()
            //layoutParams.width = desiredWidth
            //article_img.layoutParams = layoutParams
            //Get dp from 2 px by Scale screen
            val scale: Float = itemView.resources.displayMetrics.density
            val borderWidthdp = (2 * scale + 0.5f).toInt()
            //Convert Long Color into Int
            val whiteColor = 0xFFFFFFFF.toInt()
            Picasso.get()
                .load(item.thumbnail)
                .error(R.drawable.default_esopide)
                .resize(desiredWidth, 0)
                .centerCrop()
                .transform(RoundCornerTransformationPicasso(30f, borderWidthdp.toFloat(), whiteColor))
                .into(article_img)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        //GetScreenWidth
        val displayMetrics = parent.context.resources.displayMetrics
        screenWidth = displayMetrics.widthPixels

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview_blog,parent,false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.itemView.apply {
            holder.article_description.text = ds[position].desciption
            holder.article_title.text = ds[position].title

            //Set Create Date
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val dateTime: LocalDateTime = LocalDateTime.parse(ds[position].created_at, formatter)
            val formattedDateTime: String = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
            holder.article_dateCreated.text = formattedDateTime
        }
        holder.bindData(ds[position], listener)
    }

    override fun getItemCount(): Int {
        return ds?.size ?: 0
    }

    fun setData(newData : List<Article>){
        ds = newData
    }
}