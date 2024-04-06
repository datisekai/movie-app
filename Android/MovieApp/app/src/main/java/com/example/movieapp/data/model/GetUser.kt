package com.example.movieapp.data.model

class GetUser {
    lateinit var data : UserDTO

    constructor(data : UserDTO){
        this.data = UserDTO()
        this.data = data
    }
}