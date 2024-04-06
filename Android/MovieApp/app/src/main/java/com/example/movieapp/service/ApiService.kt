package com.example.movieapp.service

import com.example.movieapp.data.model.Articles
import com.example.movieapp.data.model.ClassToken
import com.example.movieapp.data.model.EditPasswordUserDTO
import com.example.movieapp.data.model.Film
import com.example.movieapp.data.model.Film1
import com.example.movieapp.data.model.GetArticle
import com.example.movieapp.data.model.GetUser
import com.example.movieapp.data.model.LoginDTO
import com.example.movieapp.data.model.TokenDTO
import com.example.movieapp.data.model.User
import com.example.movieapp.data.model.UserDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {


    @GET("api.film/{id}")
    fun getFilmById(@Path("id") id:Int) : Call<Film>

    @GET("api.film")
    fun getListFilm() : Call<Film1>

    @POST("api.auth/login")
    fun login(
       @Body loginDto : LoginDTO
    ) : Call<TokenDTO>

    //User
    @GET("api.user/{id}")
    fun getUserById(
        @Path("id") id: Int
    ) : Call<GetUser>
    @PUT("api.user/{id}")
    fun editUser(
        @Path("id") id: Int,
        @Body EditUserDto: UserDTO
    ) : Call<User>
    @PUT("api.user/{id}")
    fun editPasswordUser(
        @Path("id") id: Int,
        @Body EditUserDto: EditPasswordUserDTO
    ) : Call<User>

    //Blog
    @GET("api.article")
    fun getFirtListBlog() : Call<Articles>
    @GET("api.article")
    fun getListBlogPage(
        @Query("page") page: Int
    ) : Call<Articles>
    //Article
    @GET("api.article/{id}")
    fun getArticleById(@Path("id") id: Int) : Call<GetArticle>
}