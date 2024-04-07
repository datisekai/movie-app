package com.example.movieapp.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movieapp.data.model.Film1
import com.example.movieapp.data.model.Payment
import com.example.movieapp.data.model.PaymentDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentViewModel : ViewModel(){
    private val _dataLoaded = MutableLiveData<Boolean>()
    val dataLoaded : LiveData<Boolean>
        get() = _dataLoaded
    fun getListPayment(currentPage: Int): LiveData<Payment> {
        val paymentLiveData = MutableLiveData<Payment>()

        // Gửi yêu cầu mạng và nhận kết quả
        val call = ServiceBuilder().apiService.getListPayment(currentPage)
        call.enqueue(object : Callback<Payment> {
            override fun onResponse(call: Call<Payment>, response: Response<Payment>) {
                if (response.isSuccessful) {
                    val paymentList = response.body()
                    paymentLiveData.value = paymentList
                    _dataLoaded.value = true
                } else {
                    Log.e("ERROR",  "fail")
                }
            }

            override fun onFailure(call: Call<Payment>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return paymentLiveData
    }
}