package com.example.movieapp

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.navigation.ui.NavigationUI
import com.example.movieapp.Api.MyViewModel
import com.example.movieapp.Api.Myrepository
import com.example.movieapp.Api.ServiceBuilder
import com.example.movieapp.data.model.Film
import com.google.android.material.card.MaterialCardView
import com.google.android.material.navigation.NavigationView

class HomePage_Activity : AppCompatActivity() {


    private val filmList: MutableList<Film> = mutableListOf()
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



    }

    override fun onStart() {
        super.onStart()
        val viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        viewModel.getALLFlim().observe(this) { films ->
            filmList.clear()
            filmList.addAll(films)
        }
    }
}