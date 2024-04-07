package com.example.movieapp.data.model

import com.google.gson.annotations.SerializedName

class CommentDTO {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("content")
    var content: String = ""

    @SerializedName("created_at")
    var createAt: String = ""

    @SerializedName("updated_at")
    var updateAt: String = ""


}