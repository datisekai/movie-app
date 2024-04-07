package com.example.movieapp.data.model

import com.google.gson.annotations.SerializedName

class GenreDTO {
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("title")
    var title: String = ""
    @SerializedName("title_search")
    var titleSearch: String = ""
    @SerializedName("slug")
    var slug: String = ""
    @SerializedName("description")
    var description: String = ""
    @SerializedName("thumbnail")
    var thumbnail: String = ""
    @SerializedName("is_active")
    var isActive: Boolean = false
    @SerializedName("created_at")
    var createdAt: String = ""
    @SerializedName("updated_at")
    var updatedAt: String = ""

    constructor(){}
    constructor(
        id: Int,
        title: String,
        titleSearch: String,
        slug: String,
        description: String,
        thumbnail: String,
        isActive: Boolean,
        createdAt: String,
        updatedAt: String
    ) : this() {
        this.id = id
        this.title = title
        this.titleSearch = titleSearch
        this.slug = slug
        this.description = description
        this.thumbnail = thumbnail
        this.isActive = isActive
        this.createdAt = createdAt
        this.updatedAt = updatedAt
    }
}