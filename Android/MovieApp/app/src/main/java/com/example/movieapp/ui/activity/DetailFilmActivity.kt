package com.example.movieapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
<<<<<<< HEAD:Android/MovieApp/app/src/main/java/com/example/movieapp/ui/activity/DetailFilmActivity.kt
import com.example.movieapp.R
=======
import android.view.View
>>>>>>> master:Android/MovieApp/app/src/main/java/com/example/movieapp/DetailFilmActivity.kt

class DetailFilmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_film)
    }

    public fun clickWatch(view:View){
        val intent : Intent =  Intent(this,PlayerActivity::class.java);
        startActivity(intent);

    }

}