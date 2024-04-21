package com.example.movieapp.adapter.model

class Article(val id: Int, val slug:String,val title:String, val description:String, val thumbnail:String, val content:String, val is_active: Boolean, val created_at:String) {
    override fun toString(): String {
        return this.id.toString() + " " + this.title + " " + this.thumbnail + this.is_active.toString() + " " + this.created_at
    }

}