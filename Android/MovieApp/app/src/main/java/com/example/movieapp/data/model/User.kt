package com.example.movieapp.data.model

class User {
    var message : String = ""
    lateinit var  data : UserDTO

    constructor(message: String, data: UserDTO) {
        this.data = UserDTO()
        this.message = message
        this.data = data
    }
}