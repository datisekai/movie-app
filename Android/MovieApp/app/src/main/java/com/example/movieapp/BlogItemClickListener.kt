package com.example.movieapp

import com.example.movieapp.adapter.model.Article

interface BlogItemClickListener {
    fun onItemClicked(article: Article)
}