package com.example.movieapp.data.model

class Film {
    lateinit var data : FilmDTO

    constructor(data: FilmDTO) {
        this.data = FilmDTO()
        this.data = data
    }


}