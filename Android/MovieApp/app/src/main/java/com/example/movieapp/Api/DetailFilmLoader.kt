package com.example.movieapp.Api

import android.content.Context
import android.util.Log
import androidx.loader.content.AsyncTaskLoader
import com.example.movieapp.data.model.Film
import com.example.movieapp.data.model.LoginDTO
import java.util.concurrent.Executors

class DetailFilmLoader(context: Context, private val header : String) : AsyncTaskLoader<Film>(context) {
    override fun loadInBackground(): Film? {

        Log.e("CALL","call2")
        try {
            val service = ServiceBuilder().apiService
            val filmDetailResponse  = service.getFilmById(header,1).execute()
            Log.e("RESPONSE", filmDetailResponse.toString() + "data")
            if (filmDetailResponse.isSuccessful){
                val filmDetail = filmDetailResponse.body()
                Log.e("OK", filmDetailResponse.body().id.toString() + "data")
                return filmDetail
            }
        }catch (e : Exception){
            Log.e("CALL","call3")
            e.printStackTrace()
        }

        return null
    }


    override fun onStartLoading() {
        forceLoad()
    }

    override fun onStopLoading() {
        cancelLoad()
    }
}