package com.example.movieapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.adapter.model.Article

class ArticleBlogAdapter (var ds:List<Article>)
    :RecyclerView.Adapter<ArticleBlogAdapter.ArticleViewHolder>(){
    //Tạo class ViewHolder
    inner class ArticleViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val article_content: TextView = itemView.findViewById(R.id.article_content)
        val article_title: TextView = itemView.findViewById(R.id.article_title)
        val article_dateCreated: TextView = itemView.findViewById(R.id.article_dateCreated)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview_blog,parent,false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.itemView.apply {
            holder.article_content.text = ds[position].content
            holder.article_title.text = ds[position].title
            holder.article_dateCreated.text = ds[position].createDate
        }
    }

    override fun getItemCount(): Int {
        return ds?.size ?: 0
    }
}