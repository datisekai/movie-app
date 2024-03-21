package com.example.movieapp.data.model

import com.google.gson.annotations.SerializedName

class Film {
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
    var thumbnail: String = ""

    @SerializedName("type")
    var type: String = ""

    @SerializedName("status")
    var status: String = ""

    @SerializedName("is_required_premium")
    var isRequiredPremium: Boolean = false

    @SerializedName("director")
    var director: String? = ""

    @SerializedName("location")
    var location: String? = ""

    @SerializedName("is_active")
    var isActive: Boolean = false


    constructor(){}
    constructor(
        id: Int,
        slug: String,
        title: String,
        titleSearch: String,
        description: String,
        view: Int,
        thumbnail: String,
        type: String,
        status: String,
        isRequiredPremium: Boolean,
        director: String,
        location: String,
        isActive: Boolean,
    ) {
        this.id = id
        this.slug = slug
        this.title = title
        this.titleSearch = titleSearch
        this.description = description
        this.view = view
        this.thumbnail = thumbnail
        this.type = type
        this.status = status
        this.isRequiredPremium = isRequiredPremium
        this.director = director
        this.location = location
        this.isActive = isActive
    }


}