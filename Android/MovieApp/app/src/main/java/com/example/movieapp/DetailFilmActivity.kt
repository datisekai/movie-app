package com.example.movieapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class DetailFilmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_film)
    }

    public fun clickWatch(view:View){
        val intent : Intent =  Intent(this,PlayerActivity::class.java);
        startActivity(intent);

    }

    public fun clickBack(view: View){
        finish()
    }


}