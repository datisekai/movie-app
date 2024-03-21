package com.example.movieapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.movieapp.Api.ServiceBuilder
import com.example.movieapp.data.model.LoginDTO
import com.example.movieapp.ui.login.classToken
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        performNetWorkRequest()
        val view = findViewById<View>(R.id.rectangle_1)
        view.setOnClickListener{
            val intent = Intent(this, HomePage_Activity::class.java)
            startActivity(intent)
        }

    }

    fun performNetWorkRequest() {
        val excutor = Executors.newSingleThreadExecutor()
        excutor.execute{
            val login = LoginDTO("datly030102@gmail.com", "datisekai")
            val result = ServiceBuilder().apiService.login(login).execute()
            if (result.isSuccessful){
                var token : String = result.body().data.accessToken
                Log.e("TOKEN",token)
                classToken.MY_TOKEN = token
            }

        }
    }


}