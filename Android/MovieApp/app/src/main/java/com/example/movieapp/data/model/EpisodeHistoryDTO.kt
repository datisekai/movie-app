package com.example.movieapp.data.model

class EpisodeHistoryDTO {
    var id: Int = 0
    var slug: String = ""
    var title: String = ""
    var title_search: String =""
    var description: String = ""
    var view: Int = 0
    var thumbnailr: String = ""
    var url: String = ""
    var duration: String = ""
    var position: Int = 0
    var is_active: Boolean = false
    var created_at: String = ""
    var film: FilmDTO = FilmDTO()


    constructor()

    constructor(
        id: Int,
        slug: String,
        title: String,
        title_search: String,
        description: String,
        view: Int,
        thumbnailr: String,
        url: String,
        duration: String,
        position: Int,
        is_active: Boolean,
        created_at: String,
        film: FilmDTO
    ) {
        this.id = id
        this.slug = slug
        this.title = title
        this.title_search = title_search
        this.description = description
        this.view = view
        this.thumbnailr = thumbnailr
        this.url = url
        this.duration = duration
        this.position = position
        this.is_active = is_active
        this.created_at = created_at
    }
}