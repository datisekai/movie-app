package com.example.movieapp.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.data.model.Film1
import com.example.movieapp.data.model.FilmFavorite
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteViewModel: ViewModel() {
    private val _dataLoaded = MutableLiveData<Boolean>()
    val dataLoaded : LiveData<Boolean>
        get() = _dataLoaded
    fun getListGenreMovie(currentPage: Int): LiveData<FilmFavorite> {
        val paymentLiveData = MutableLiveData<FilmFavorite>()

        // Gửi yêu cầu mạng và nhận kết quả
        val call = ServiceBuilder().apiService.getListFilmFavorite(currentPage)
        call.enqueue(object : Callback<FilmFavorite> {
            override fun onResponse(call: Call<FilmFavorite>, response: Response<FilmFavorite>) {
                if (response.isSuccessful) {
                    val list = response.body()
                    paymentLiveData.value = list
                    _dataLoaded.value = true
                } else {
                    Log.e("ERROR",  "fail")
                }
            }

            override fun onFailure(call: Call<FilmFavorite>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return paymentLiveData
    }
}