package com.example.movieapp.data.model

import android.provider.ContactsContract.CommonDataKinds.Email
import com.google.gson.annotations.SerializedName




class LoginDTO {
    @SerializedName("email")
    private var email: String? = null

    @SerializedName("password")
    private var password: String? = null

    // Getter và setter

    // Getter và setter
    constructor(email: String?, password: String?) {
        this.email = email
        this.password = password
    }
}