package com.example.movieapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
<<<<<<< HEAD:Android/MovieApp/app/src/main/java/com/example/movieapp/HomePage_Activity.kt
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
=======
>>>>>>> e8687ade528b7c9c6058851a391bf92e7e462114:Android/MovieApp/app/src/main/java/com/example/movieapp/ui/activity/HomePage_Activity.kt
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
<<<<<<< HEAD:Android/MovieApp/app/src/main/java/com/example/movieapp/HomePage_Activity.kt
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
=======
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.movieapp.R
>>>>>>> e8687ade528b7c9c6058851a391bf92e7e462114:Android/MovieApp/app/src/main/java/com/example/movieapp/ui/activity/HomePage_Activity.kt
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