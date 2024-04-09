package com.example.movieapp.data.model

import com.google.gson.annotations.SerializedName

class FilmFavoriteDTO {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("created_at")
    var createdAt : String = ""

    @SerializedName("updated_at")
    var updatedAt : String = ""

    @SerializedName("film")
    lateinit var film: FilmDTO
    constructor(){}
    constructor(
        id: Int,
        film :FilmDTO,
        createdAt: String,
        updatedAt: String
    ) {
        this.id = id
        this.film= film
        this.createdAt = createdAt
        this.updatedAt= updatedAt
    }
}