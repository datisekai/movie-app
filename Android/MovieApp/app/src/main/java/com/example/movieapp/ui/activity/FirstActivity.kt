package com.example.movieapp.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.movieapp.R
import com.example.movieapp.ui.login.LoginActivity


class FirstActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        val videoView: VideoView = findViewById(R.id.videoView)
        val videoPath = "android.resource://$packageName/raw/my_video"
        videoView.setVideoPath(videoPath)
        videoView.start()
        videoView.setOnCompletionListener {
            videoView.stopPlayback()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}