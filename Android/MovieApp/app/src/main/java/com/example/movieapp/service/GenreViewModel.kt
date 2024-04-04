package com.example.movieapp.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.data.model.Genre
import com.example.movieapp.data.model.Payment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GenreViewModel: ViewModel() {
    private val _dataLoaded = MutableLiveData<Boolean>()
    val dataLoaded : LiveData<Boolean>
        get() = _dataLoaded
    fun getListGenre(currentPage: Int): LiveData<Genre> {
        val paymentLiveData = MutableLiveData<Genre>()

        // Gửi yêu cầu mạng và nhận kết quả
        val call = ServiceBuilder().apiService.getListGenre(currentPage)
        call.enqueue(object : Callback<Genre> {
            override fun onResponse(call: Call<Genre>, response: Response<Genre>) {
                if (response.isSuccessful) {
                    val genreList = response.body()
                    paymentLiveData.value = genreList
                    _dataLoaded.value = true
                } else {
                    Log.e("ERROR",  "fail")
                }
            }

            override fun onFailure(call: Call<Genre>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return paymentLiveData
    }
}