package com.example.movieapp.service

import android.content.Context
import android.util.Log
import androidx.loader.content.AsyncTaskLoader
import com.example.movieapp.data.model.ClassToken
import com.example.movieapp.data.model.GetUser

class UserLoader(context: Context): AsyncTaskLoader<GetUser>(context) {

    override fun loadInBackground(): GetUser? {
        try {
            Log.e("TestCal",id.toString())
            val result = ServiceBuilder().apiService.getUserById(ClassToken.ID).execute()
            if(result.isSuccessful){
                val user = result.body()
                return user;
            }

        }catch (e: Exception){
            e.printStackTrace()
        }
        return null
    }

    override fun onStartLoading() {
        forceLoad()
    }
}