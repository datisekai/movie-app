package com.example.movieapp.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.data.model.Film1
import com.example.movieapp.data.model.Genre
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GenreMovieViewModel: ViewModel() {

    private val _dataLoaded = MutableLiveData<Boolean>()
    val dataLoaded : LiveData<Boolean>
        get() = _dataLoaded
    fun getListGenreMovie(genreId:Int , currentPage: Int): LiveData<Film1> {
        val paymentLiveData = MutableLiveData<Film1>()

        // Gửi yêu cầu mạng và nhận kết quả
        val call = ServiceBuilder().apiService.getListFilmGenre(genreId.toString(), currentPage)
        call.enqueue(object : Callback<Film1> {
            override fun onResponse(call: Call<Film1>, response: Response<Film1>) {
                if (response.isSuccessful) {
                    val list = response.body()
                    paymentLiveData.value = list
                    _dataLoaded.value = true
                } else {
                    Log.e("ERROR",  "fail")
                }
            }

            override fun onFailure(call: Call<Film1>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return paymentLiveData
    }
}