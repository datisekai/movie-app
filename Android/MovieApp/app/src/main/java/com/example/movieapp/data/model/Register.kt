package com.example.movieapp.data.model

class Register {
    var message : String = ""
    lateinit var  data : RegisterUserDTO

    constructor(message: String, data: RegisterUserDTO) {
        this.data = RegisterUserDTO()
        this.message = message
        this.data = data
    }
}