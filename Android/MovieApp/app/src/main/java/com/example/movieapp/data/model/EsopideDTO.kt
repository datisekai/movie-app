package com.example.movieapp.data.model

import com.google.gson.annotations.SerializedName

class EsopideDTO {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("slug")
    var slug: String = ""

    @SerializedName("title")
    var title: String = ""

    @SerializedName("title_search")
    var titleSearch: String = ""

    @SerializedName("description")
    var description: String? = ""

    @SerializedName("view")
    var view: Int = 0

    @SerializedName("thumbnail")
    var thumbnail: String? = ""

    @SerializedName("url")
    var url: String = ""

    @SerializedName("duration")
    var duration: String = ""

    @SerializedName("position")
    var position: Int = 0

    @SerializedName("is_active")
    var isActive: Boolean = false

    constructor(
        id: Int,
        slug: String,
        title: String,
        titleSearch: String,
        description: String?,
        view: Int,
        thumbnail: String,
        url: String,
        duration: String,
        position: Int,
        isActive: Boolean
    ) {
        this.id = id
        this.slug = slug
        this.title = title
        this.titleSearch = titleSearch
        this.description = description
        this.view = view
        this.thumbnail = thumbnail
        this.url = url
        this.duration = duration
        this.position = position
        this.isActive = isActive
    }
}