package com.example.movieapp.data.model

import com.google.gson.annotations.SerializedName

class RegisterDTO {
    @SerializedName("email")
    private var email: String? = null

    @SerializedName("password")
    private var password: String? = null

    @SerializedName("fullname")
    private var fullname: String? = null

    // Getter và setter

    // Getter và setter
    constructor(email: String?, password: String?, fullname: String?) {
        this.email = email
        this.password = password
        this.fullname=fullname
    }
}