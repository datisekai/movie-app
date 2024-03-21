package com.example.movieapp.Api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceBuilder {
    var gson : Gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()

    var apiService : ApiService = Retrofit.Builder()
            .baseUrl("https://movie-backend.datisekai.id.vn/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)

}