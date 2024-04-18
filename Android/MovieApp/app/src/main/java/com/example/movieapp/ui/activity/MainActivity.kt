package com.example.movieapp.ui.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.example.movieapp.R
import com.example.movieapp.data.model.ClassToken
import com.example.movieapp.data.model.LoginDTO
import com.example.movieapp.service.NetworkManager
import com.example.movieapp.service.ServiceBuilder
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var layout : RelativeLayout
    private lateinit var view : Button
    var count : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        checkConnect()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        view = findViewById<Button>(R.id.rectangle_2)
        view.setOnClickListener{
           if (isNetworkConnected(this)){
               view.setOnClickListener{
                   val intent = Intent(this, HomePage_Activity::class.java)
                   startActivity(intent)
               }
           }else{
               customeToast("Đã xảy ra lỗi! Vui lòng thử lại")
           }
        }

    }

    private lateinit var cld : NetworkManager
    private fun checkConnect(){
        cld = NetworkManager(application)
        cld.observe(this){
            if (it){
               if (count!=0){
                   customeToast("Đã có kết nối trở lại")
                   count++
               }else{
                   count++
               }
            }else{
                customeToast("Không có kết nối Internet")
            }
        }
    }

    private fun customeToast(message : String){
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.custome_toast,this.findViewById(R.id.CustomToast))
        val toast = Toast(this)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = view
        val txt : TextView = view.findViewById(R.id.txtMessage)
        txt.text = message
        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 100)
        toast.show()
    }

    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }



}