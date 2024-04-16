package com.example.movieapp.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.data.model.RequestFcmToken
import com.example.movieapp.data.model.UserDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FcmTokenViewModel : ViewModel() {

    fun putFCMToken(fcmToken: RequestFcmToken){
        val call = ServiceBuilder().apiService.pustFcmToken(fcmToken)
        call.enqueue(object : Callback<UserDTO>{
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                if (response.isSuccessful){
                    Log.e("FCMTOKEN",response.body().fcmToken)
                }else{
                    Log.e("ERROR","Fail")
                }
            }

            override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

}