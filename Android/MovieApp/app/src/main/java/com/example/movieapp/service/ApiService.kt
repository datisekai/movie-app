package com.example.movieapp.service

import com.example.movieapp.data.model.Articles
import com.example.movieapp.data.model.ClassToken
import com.example.movieapp.data.model.EditPasswordUserDTO
import com.example.movieapp.data.model.EpisodeHistoryDTO
import com.example.movieapp.data.model.Comment
import com.example.movieapp.data.model.CommentCreate
import com.example.movieapp.data.model.ConfirmOrder
import com.example.movieapp.data.model.Esopide
import com.example.movieapp.data.model.Film
import com.example.movieapp.data.model.Film1
import com.example.movieapp.data.model.Genre
import com.example.movieapp.data.model.GetArticle
import com.example.movieapp.data.model.GetUser
import com.example.movieapp.data.model.LoginDTO
import com.example.movieapp.data.model.Payment
import com.example.movieapp.data.model.PaymentDTO
import com.example.movieapp.data.model.RegisterDTO
import com.example.movieapp.data.model.PayOrder
import com.example.movieapp.data.model.Profile
import com.example.movieapp.data.model.RequestComment
import com.example.movieapp.data.model.RequestFilmFavorite
import com.example.movieapp.data.model.TokenDTO
import com.example.movieapp.data.model.User
import com.example.movieapp.data.model.UserDTO
import com.example.movieapp.data.model.Register
import com.example.movieapp.ui.fragment.EpisodeIdsWrapper
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

    @GET("api.favourite/me")
    fun getListFilmFavorite(@Query("page") page: Int) : Call<Film1>

    @GET("api.order/me")
    fun getListPayment(@Query("page") page: Int) : Call<Payment>

    @GET("api.category")
    fun getListGenre(@Query("page") page: Int) : Call<Genre>
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
    @POST("api.user/register")
    fun register(
        @Body registerDto : RegisterDTO
    ) : Call<Register>

    //User
    @GET("api.user/{id}")
    fun getUserById(
        @Path("id") id: Int
    ) : Call<GetUser>
    @POST("api.order")
    fun createOrder() : Call<PayOrder>

    @FormUrlEncoded
    @POST("api.order/confirm")
    fun confirmOrder(
        @Field("zalo_trans_id") transId : String
    ) : Call<ConfirmOrder>

    @POST("api.comment")
    fun createComment(
       @Body requestComment: RequestComment
    ) : Call<CommentCreate>

    @POST("api.favourite")
    fun postFilmFavorite(
        @Body requestFilmFavorite: RequestFilmFavorite
    ) : Call<CommentCreate>
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

    //History
    @POST("api.episode/history")
    fun getHistory(
        @Body episode_ids: EpisodeIdsWrapper
    ) : Call<List<EpisodeHistoryDTO>>
}