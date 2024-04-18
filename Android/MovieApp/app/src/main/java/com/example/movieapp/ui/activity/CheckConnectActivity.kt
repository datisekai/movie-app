package com.example.movieapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.movieapp.R
import com.example.movieapp.service.NetworkManager
import com.example.movieapp.ui.login.LoginActivity

class CheckConnectActivity : AppCompatActivity() {
    private lateinit var cld : NetworkManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_connect)
        checkConnect()
    }

    private fun checkConnect(){
        cld = NetworkManager(application)
        cld.observe(this){
            if (it){
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)

            }

        }
    }
}