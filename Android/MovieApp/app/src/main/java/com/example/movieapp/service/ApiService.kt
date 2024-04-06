package com.example.movieapp.service

import com.example.movieapp.data.model.ClassToken
import com.example.movieapp.data.model.Comment
import com.example.movieapp.data.model.ConfirmOrder
import com.example.movieapp.data.model.Esopide
import com.example.movieapp.data.model.Film
import com.example.movieapp.data.model.Film1
import com.example.movieapp.data.model.LoginDTO
import com.example.movieapp.data.model.PayOrder
import com.example.movieapp.data.model.Profile
import com.example.movieapp.data.model.TokenDTO
import com.example.movieapp.data.model.UserDTO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {


    @GET("api.film/{id}")
    fun getFilmById(@Path("id") id:Int) : Call<Film>

    @GET("api.film")
    fun getListFilm() : Call<Film1>

    @GET("api.episode/film/{id}")
    fun getListEsopide(@Path("id") id:Int) : Call<Esopide>

    @GET("api.comment/film/{id}")
    fun getListComment(@Path("id") id:Int) : Call<Comment>

    @GET("api.auth/profile")
    fun getMyProfile() : Call<Profile>

    @POST("api.auth/login")
    fun login(
        @Body loginDto : LoginDTO
    ) : Call<TokenDTO>

    @POST("api.order")
    fun createOrder() : Call<PayOrder>

    @FormUrlEncoded
    @POST("api.order/confirm")
    fun confirmOrder(
        @Field("zalo_trans_id") transId : String
    ) : Call<ConfirmOrder>

    @PUT("api.user/{id}")
    fun editUser(
        @Path("id") id: Int,
        @Body EditUserDto: UserDTO
    ) : Call<TokenDTO>

}