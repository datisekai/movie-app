package com.example.movieapp.data.model

import com.example.movieapp.adapter.model.Article

class Articles {
    var data: List<Article> = ArrayList<Article>()
    var totalEntries: Int = 0
    var page: Int = 0
    var limit: Int = 0

    constructor()

    constructor(data: List<Article>, totalEntries: Int, page: Int, limit: Int){
        this.data = data
        this.totalEntries = totalEntries
        this.page = page
        this.limit = limit
    }
}