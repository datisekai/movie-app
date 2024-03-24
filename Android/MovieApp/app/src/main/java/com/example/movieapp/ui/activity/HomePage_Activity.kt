package com.example.movieapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.movieapp.Api.MyViewModel
import com.example.movieapp.data.model.Film
import com.example.movieapp.R
import com.google.android.material.navigation.NavigationView

class HomePage_Activity : AppCompatActivity() {


    private  val filmList: MutableList<Film> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)


        val drawerlayout = findViewById<DrawerLayout>(R.id.myDrawer)
        findViewById<ImageView>(R.id.imgMenu).setOnClickListener {
            drawerlayout.openDrawer(GravityCompat.START)
        }
        val navigationview = findViewById<NavigationView>(R.id.navigationView)
        navigationview.setItemIconTintList(null)

        val navController = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupWithNavController(navigationview, navController)


        val viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        viewModel.getFilmListLiveData().observe(this, { films ->
            Log.e("ERROR","fail")
            filmList.clear()
            filmList.addAll(films)
            filmData.listFilm.addAll(filmList)

        })

    }


    class filmData(){
        companion object{
            val listFilm : MutableList<Film> = mutableListOf()
        }

    }

}