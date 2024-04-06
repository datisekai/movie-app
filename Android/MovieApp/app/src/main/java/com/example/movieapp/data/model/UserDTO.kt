package com.example.movieapp.data.model

import com.google.gson.annotations.SerializedName

class UserDTO {
    var id : Int = 0
    var email : String = ""
    var fullname : String = ""
    @SerializedName("roles")
    var roles: ArrayList<String> = arrayListOf()
    var is_active : Boolean = false


    constructor(){}
    constructor(id: Int, email: String, fullname: String, is_active: Boolean) {
        this.id = id
        this.email = email
        this.fullname = fullname
        this.is_active = is_active
    }
}
