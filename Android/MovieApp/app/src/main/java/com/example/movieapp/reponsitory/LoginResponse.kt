package com.example.movieapp.reponsitory

import com.example.movieapp.model.User
import com.google.gson.annotations.SerializedName

data class LoginResponse(val user: User,
                         @SerializedName("accessToken")
                         val accessToken: String)
