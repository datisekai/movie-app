package com.example.movieapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.movieapp.Helper
import com.example.movieapp.R
import com.google.android.material.navigation.NavigationView
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso

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

        //Set name & image Profile in menu
        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        val headerView = navigationView.getHeaderView(0)
        val imgProfile = headerView.findViewById<RoundedImageView>(R.id.imageProflie)
        val userName = headerView.findViewById<TextView>(R.id.menu_username_tv)
        val currentUserName = Helper.TokenManager.getFullName(this)
        userName.text = currentUserName
        val urlAvt = "https://ui-avatars.com/api/?name=" + currentUserName
        Picasso.get()
            .load(urlAvt)
            .into(imgProfile)
    }


//    public class filmData(){
//        companion object{
//            val listFilm : MutableList<FilmDTO> = mutableListOf()
//        }
//
//    }

}