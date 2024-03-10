package com.example.movieapp

class slideItem {
    private var image : Int = 0

    constructor(image: Int) {
        this.image = image
    }

    var Image : Int
        get() {return image}
        set(value) {image=value}
}