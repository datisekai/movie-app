package com.example.movieapp.data.model

class RegisterUserDTO {
    var id : Int = 0
    var email : String = ""
    var fullname : String = ""
    var is_active : Boolean = false

    constructor(){}
    constructor(id: Int, email: String, fullname: String, is_active: Boolean) {
        this.id = id
        this.email = email
        this.fullname = fullname
        this.is_active = is_active
    }
}