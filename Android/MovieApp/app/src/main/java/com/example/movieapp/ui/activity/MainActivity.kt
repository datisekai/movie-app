package com.example.movieapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.movieapp.R
import com.example.movieapp.data.model.LoginDTO
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view = findViewById<View>(R.id.rectangle_1)
        view.setOnClickListener{
            val intent = Intent(this, HomePage_Activity::class.java)
            startActivity(intent)
        }

    }

}