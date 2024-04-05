package com.example.movieapp.service

import com.example.movieapp.data.model.ClassToken
import com.example.movieapp.data.model.Film
import com.example.movieapp.data.model.Film1
import com.example.movieapp.data.model.Genre
import com.example.movieapp.data.model.LoginDTO
import com.example.movieapp.data.model.Payment
import com.example.movieapp.data.model.PaymentDTO
import com.example.movieapp.data.model.RegisterDTO
import com.example.movieapp.data.model.TokenDTO
import com.example.movieapp.data.model.User
import com.example.movieapp.data.model.UserDTO
import com.example.movieapp.data.model.Register
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

    @GET("api.film")
    fun getListFilmSearch(@Query("title") title: String, @Query("page") page: Int) : Call<Film1>

    @GET("api.film")
    fun getListFilmGenre(@Query("category_id") title: String, @Query("page") page: Int) : Call<Film1>

    @GET("api.order/me")
    fun getListPayment(@Query("page") page: Int) : Call<Payment>

    @GET("api.category")
    fun getListGenre(@Query("page") page: Int) : Call<Genre>

    @POST("api.auth/login")
    fun login(
       @Body loginDto : LoginDTO
    ) : Call<TokenDTO>

    @POST("api.user/register")
    fun register(
        @Body registerDto : RegisterDTO
    ) : Call<Register>

    @PUT("api.user/{id}")
    fun editUser(
        @Path("id") id: Int,
        @Body EditUserDto: UserDTO
    ) : Call<User>
}