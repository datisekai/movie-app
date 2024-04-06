package com.example.movieapp.Api

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.Helper
import com.example.movieapp.data.model.ClassToken
import com.example.movieapp.data.model.Comment
import com.example.movieapp.data.model.CommentCreate
import com.example.movieapp.data.model.ConfirmOrder
import com.example.movieapp.data.model.DataDTO
import com.example.movieapp.data.model.Esopide
import com.example.movieapp.data.model.Film
import com.example.movieapp.data.model.Film1
import com.example.movieapp.data.model.PayOrder
import com.example.movieapp.data.model.Profile
import com.example.movieapp.data.model.RequestComment
import com.example.movieapp.data.model.RequestFilmFavorite
import com.example.movieapp.data.model.UserDTO
import com.example.movieapp.service.ServiceBuilder
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyViewModel() : ViewModel() {
    private val _dataLoaded = MutableLiveData<Boolean>()
    val dataLoaded : LiveData<Boolean>
        get() = _dataLoaded
    fun getListFilm(): LiveData<Film1> {
        val filmLiveData = MutableLiveData<Film1>()
        // Gửi yêu cầu mạng và nhận kết quả
        val call = ServiceBuilder().apiService.getListFilm()
        call.enqueue(object : Callback<Film1> {
            override fun onResponse(call: Call<Film1>, response: Response<Film1>) {
                if (response.isSuccessful) {
                    val filmList = response.body()
                    filmLiveData.value = filmList
                    _dataLoaded.value = true
                } else {
                   Log.e("ERROR",  "fail")
                }
            }

            override fun onFailure(call: Call<Film1>, t: Throwable) {
               t.printStackTrace()
            }
        })

        return filmLiveData
    }

    fun getAllEsopide(id : Int): LiveData<Esopide>{
        val esopideLiveData = MutableLiveData<Esopide>()
        val call = ServiceBuilder().apiService.getListEsopide(id)
        call.enqueue(object  : Callback<Esopide>{
            override fun onResponse(call: Call<Esopide>, response: Response<Esopide>) {
                if (response.isSuccessful){
                    esopideLiveData.value = response.body()
                    _dataLoaded.value = true
                }else {
                    Log.e("ERROR",  "fail")
                }
            }

            override fun onFailure(call: Call<Esopide>, t: Throwable) {
                t.printStackTrace()
            }
        })
        return esopideLiveData
    }

    fun getAllComment(id : Int) : LiveData<Comment>{
        val commentLiveData = MutableLiveData<Comment>()
        val call = ServiceBuilder().apiService.getListComment(id)
        call.enqueue(object : Callback<Comment>{
            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                if (response.isSuccessful){
                    commentLiveData.value = response.body()
                    _dataLoaded.value = true
                }else{
                    Log.e("ERROR",  "fail")
                }
            }

            override fun onFailure(call: Call<Comment>, t: Throwable) {
                t.printStackTrace()
            }

        })
        return commentLiveData
    }

    fun getTokenCreateOrder() : LiveData<PayOrder>{
        val tokenPayOrder : LiveData<PayOrder> = MutableLiveData()
        val call = ServiceBuilder().apiService.createOrder()
        call.enqueue(object : Callback<PayOrder>{
            override fun onResponse(call: Call<PayOrder>, response: Response<PayOrder>) {
                if (response.isSuccessful){
                    (tokenPayOrder as MutableLiveData).value = response.body()
                    _dataLoaded.value = true
                }else {
                    Log.e("ERROR",  "fail")
                }
            }

            override fun onFailure(call: Call<PayOrder>, t: Throwable) {
                t.printStackTrace()
            }

        })

        return tokenPayOrder
    }

    fun postZaloTransId(transId : String,context: Context){
        val call = ServiceBuilder().apiService.confirmOrder(transId)
        call.enqueue(object : Callback<ConfirmOrder>{
            override fun onResponse(call: Call<ConfirmOrder>, response: Response<ConfirmOrder>) {
                if (response.isSuccessful){
                    Log.e("RESULT",  "Success")
                    updateProfile(context)
                }else{
                    Log.e("ERROR",  "fail")
                }
            }

            override fun onFailure(call: Call<ConfirmOrder>, t: Throwable) {
                t.printStackTrace()
            }

        })

    }

    fun createComment(requestComment: RequestComment){
        val call = ServiceBuilder().apiService.createComment(requestComment)
        call.enqueue(object : Callback<CommentCreate>{
            override fun onResponse(call: Call<CommentCreate>, response: Response<CommentCreate>) {
                if (response.isSuccessful){
                    Log.e("RESULT", "Success")
                }else
                {
                    Log.e("ERROR",  "fail")
                }
            }

            override fun onFailure(call: Call<CommentCreate>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    fun postFilmFavoutite(requestFilmFavorite: RequestFilmFavorite){
        val call = ServiceBuilder().apiService.postFilmFavorite(requestFilmFavorite)
        call.enqueue(object : Callback<CommentCreate>{
            override fun onResponse(
                call: Call<CommentCreate>,
                response: Response<CommentCreate>
            ) {
                if (response.isSuccessful){
                    Log.e("RESULT", "Success")
                }else
                {
                    Log.e("ERROR",  "fail")
                }
            }

            override fun onFailure(call: Call<CommentCreate>, t: Throwable) {
               t.printStackTrace()
            }

        })
    }


    fun updateProfile(context: Context){
        val call = ServiceBuilder().apiService.getMyProfile()
        call.enqueue(object : Callback<Profile>{
            override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                if (response.isSuccessful){
                    val user = response.body().user
                    ClassToken.MY_TOKEN= Helper.TokenManager.getToken(context).toString()
                    Log.e("ROLES",ClassToken.MY_TOKEN)
                    ClassToken.ID= user.id
                    ClassToken.EMAIL= user.email
                    ClassToken.FULLNAME= user.fullname
                    ClassToken.IS_ACTIVE = user.is_active
                    ClassToken.ROLES = user.roles
                    Log.e("ROLES",ClassToken.ROLES[0])
                    Helper.TokenManager.saveToken(context, ClassToken.MY_TOKEN, ClassToken.ID, ClassToken.EMAIL, ClassToken.FULLNAME, ClassToken.IS_ACTIVE,ClassToken.ROLES )
                }
                else{
                    Log.e("ERROR",  "fail")
                }
            }

            override fun onFailure(call: Call<Profile>, t: Throwable) {
                t.printStackTrace()
            }

        })

    }

}
