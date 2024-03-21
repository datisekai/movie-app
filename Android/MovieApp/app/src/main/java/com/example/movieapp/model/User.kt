package com.example.movieapp.model

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val email: String,
    val fullname: String,
    val roles: List<String>,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)