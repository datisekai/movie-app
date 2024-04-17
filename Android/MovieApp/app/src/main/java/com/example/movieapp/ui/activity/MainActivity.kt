package com.example.movieapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.movieapp.R
import com.example.movieapp.data.model.ClassToken
import com.example.movieapp.data.model.LoginDTO
import com.example.movieapp.service.ServiceBuilder
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val view = findViewById<Button>(R.id.rectangle_2)
        view.setOnClickListener{
            val intent = Intent(this, HomePage_Activity::class.java)
            startActivity(intent)
        }

    }


}