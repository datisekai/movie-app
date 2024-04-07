package com.example.movieapp.data.model

class TokenDTO {
    var message : String = ""
    lateinit var  data : DataDTO
    constructor() {}
    constructor(message: String, data: DataDTO) {
        this.data = DataDTO()
        this.message = message
        this.data = data
    }
}