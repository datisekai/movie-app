package com.example.movieapp.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.movieapp.service.NetworkManager
import com.example.movieapp.ui.activity.CheckConnectActivity
import com.example.movieapp.ui.login.LoginActivity
import com.example.movieapp.R

class firstActivity : AppCompatActivity() {
    private  lateinit var videoView: VideoView
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        videoView = findViewById(R.id.videoView)
        val videoPath = "android.resource://$packageName/raw/my_video"
        videoView.setVideoPath(videoPath)
        videoView.start()
        if (intent.extras!=null){
            videoView.setOnCompletionListener {
                videoView.stopPlayback();
                val mainIntent = Intent(this,HomePage_Activity::class.java)
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(mainIntent)

                val filmId = intent.extras!!.getString("id")
                val intent = Intent(this,DetailFilmActivity::class.java)
                val bundle = Bundle()
                bundle.putInt("ID", filmId?.toInt() ?: 0)
                bundle.putBoolean("IS_ACTIVITY",false)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("DataID",bundle)
                startActivity(intent)
                finish()
            }

        }
        else{
            videoView.setOnCompletionListener {
                videoView.stopPlayback();
                if (isNetworkConnected(this)){
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    val intent = Intent(this, CheckConnectActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }




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