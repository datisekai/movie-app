package com.example.movieapp.data.model

class DataDTO {
    lateinit var user : UserDTO
    var accessToken : String = ""

    constructor(){

    }

    constructor(user: UserDTO, accessToken: String) {
        this.user = UserDTO()
        this.user = user
        this.accessToken = accessToken
    }
}
