package com.example.movieapp.data.model

import com.google.gson.annotations.SerializedName

class Film {
    lateinit var data : FilmDTO

    constructor(data: FilmDTO) {
        this.data = FilmDTO()
        this.data = data
    }


}