package com.example.movieapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.movieapp.Helper
import com.example.movieapp.R
import com.example.movieapp.data.model.FilmDTO
import com.example.movieapp.service.NetworkManager
import com.example.movieapp.ui.fragment.BlogFragment
import com.example.movieapp.ui.fragment.ProfileFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso

class HomePage_Activity : AppCompatActivity() {
    private lateinit var layout : ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        checkConnect()

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

    override fun onResume() {
        super.onResume()
        count = 0
    }

    private lateinit var cld : NetworkManager
    private var count : Int = 0
    private fun checkConnect(){
        cld = NetworkManager(application)
        cld.observe(this){
            if (it){
                if (count!=0){
                    customeToast("Đã có kết nối trở lại")
                }else{
                    count++
                }
            }else{
                customeToast("Không có kết nối Internet")
            }
        }
    }
    private fun customeToast(message : String){
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.custome_toast,this.findViewById(R.id.CustomToast))
        val toast = Toast(this)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = view
        val txt : TextView = view.findViewById(R.id.txtMessage)
        txt.text = message
        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 100)
        toast.show()
    }


}