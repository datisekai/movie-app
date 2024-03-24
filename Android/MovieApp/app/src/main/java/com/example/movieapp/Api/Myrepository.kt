package com.example.movieapp.Api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movieapp.data.model.Film
import retrofit2.Call

class Myrepository() {
     fun fecthData() : LiveData<List<Film>>{
        val data = MutableLiveData<List<Film>>()
        val service = ServiceBuilder().apiService.getListFilm().execute()
         if (service.isSuccessful){
             data.value = service.body()
             return data
         }
         else{
             Log.e("ERROR","Get list film fail")
         }
         return data
    }

}