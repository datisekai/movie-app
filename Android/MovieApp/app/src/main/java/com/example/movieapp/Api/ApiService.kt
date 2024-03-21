package com.example.movieapp.Api

import com.example.movieapp.data.model.Film
import com.example.movieapp.data.model.LoginDTO
import com.example.movieapp.data.model.TokenDTO
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {


    @GET("api.film/{id}")
    fun getFilmById(@Header("Authorization") header : String,
                    @Path("id") id:Int) : Call<Film>

    @GET("api.film")
    fun getListFilm() : Call<List<Film>>

    @POST("api.auth/login")
    fun login(
       @Body loginDto : LoginDTO
    ) : Call<TokenDTO>
}