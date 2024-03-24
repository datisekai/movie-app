package com.example.movieapp.Api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.data.model.Film
import com.example.movieapp.service.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyViewModel() : ViewModel() {
    private val filmListLiveData = MutableLiveData<List<Film>>()

    fun getFilmListLiveData(): LiveData<List<Film>> {
        fetchFilmList()
        return filmListLiveData
    }

    fun fetchFilmList() {
        val call = ServiceBuilder().apiService.getListFilm()
        call.enqueue(object : Callback<List<Film>> {
            override fun onResponse(call: Call<List<Film>>, response: Response<List<Film>>) {
                if (response.isSuccessful) {
                    val filmList = response.body().toList().size
                    Log.e("ERROR",filmList.toString())
                } else {
                    Log.e("ERROR","Get Film Fail")
                }
            }

            override fun onFailure(call: Call<List<Film>>, t: Throwable) {
                Log.e("ERROR","Call api fail")
                t.printStackTrace()
            }
        })
    }
}
