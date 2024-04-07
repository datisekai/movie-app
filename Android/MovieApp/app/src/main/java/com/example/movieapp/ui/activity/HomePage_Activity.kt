package com.example.movieapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.movieapp.Api.MyViewModel
import com.example.movieapp.data.model.Film
import com.example.movieapp.R
import com.example.movieapp.data.model.FilmDTO
import com.example.movieapp.ui.fragment.BlogFragment
import com.example.movieapp.ui.fragment.ProfileFragment
import com.google.android.material.navigation.NavigationView

class HomePage_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

//        val viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
//        viewModel.getListFilm().observe(this, { films ->
//            Log.e("DATA",films.data.get(0).title)
//            filmData.listFilm.addAll(films.data)
//
//        })

        val drawerlayout = findViewById<DrawerLayout>(R.id.myDrawer)
        findViewById<ImageView>(R.id.imgMenu).setOnClickListener {
            drawerlayout.openDrawer(GravityCompat.START)
        }
        val navigationview = findViewById<NavigationView>(R.id.navigationView)
        navigationview.setItemIconTintList(null)

        val navController = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupWithNavController(navigationview, navController)

        var typeFragment = intent.extras?.getInt("typeFragment")
        if(typeFragment != null){
            if(typeFragment == 1){
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
                val navController = navHostFragment.navController
                navController.navigate(R.id.action_currentFragment_to_menu_Profile)

                intent.removeExtra("typeFragment")
                typeFragment = null
            }
            else if(typeFragment == 2){
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
                val navController = navHostFragment.navController
                navController.navigate(R.id.action_currentFragment_to_menu_Blog)

                intent.removeExtra("typeFragment")
                typeFragment = null
            }
        }
    }


//    public class filmData(){
//        companion object{
//            val listFilm : MutableList<FilmDTO> = mutableListOf()
//        }
//
//    }

}