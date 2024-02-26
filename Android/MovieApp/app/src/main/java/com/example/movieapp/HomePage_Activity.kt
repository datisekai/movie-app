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
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.card.MaterialCardView
import com.google.android.material.navigation.NavigationView

class HomePage_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val drawerlayout = findViewById<DrawerLayout>(R.id.myDrawer)
        findViewById<ImageView>(R.id.imgMenu).setOnClickListener {
            drawerlayout.openDrawer(GravityCompat.START)
        }
        val navigationview = findViewById<NavigationView>(R.id.navigationView)
        navigationview.setItemIconTintList(null)

        val navController = Navigation.findNavController(this,R.id.navHostFragment)
        NavigationUI.setupWithNavController(navigationview,navController)

        val cardDataList = listOf("Card 1", "Card 2", "Card 3","Card 1")
        val listCardContainer = findViewById<LinearLayout>(R.id.listCardContainer)

        for (cardData in cardDataList) {
            val card = LinearLayout(this)
            val layoutParams1 = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams1.setMargins(8, 0, 8, 0)
            card.layoutParams = layoutParams1

            val linearLayout = LinearLayout(this)
            linearLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            linearLayout.orientation = LinearLayout.VERTICAL

            val textView = TextView(this)
            textView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
            textView.text = "Popular Movie"
            textView.setTextColor(Color.WHITE)
//            textView.setTextAppearance(com.google.android.material.R.style.TextAppearance_MaterialComponents_Headline6)
            textView.setTypeface(Typeface.DEFAULT_BOLD)
            val textSize = 20
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())
            textView.setPadding(16,0,0,24)

//            val button = Button(this)
//            button.layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//            button.text = "View More"
//            button.setTextColor(ContextCompat.getColor(this, R.color.white))
////            button.setBackgroundResource(R.drawable.rounded_button)
//            button.setPadding(8, 0, 8, 0)

//            val layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//            layoutParams.marginEnd = 8

//            button.layoutParams = layoutParams

//            val linearLayoutCard = LinearLayout(this)
//            linearLayoutCard.layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )

            val horizontalScrollView = HorizontalScrollView(this)
            horizontalScrollView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            horizontalScrollView.isHorizontalScrollBarEnabled = false
            horizontalScrollView.isScrollbarFadingEnabled = false
            horizontalScrollView.setPadding(8,0,0,40)

            val cardContainer = LinearLayout(this)
            cardContainer.id = View.generateViewId()
            cardContainer.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            cardContainer.orientation = LinearLayout.HORIZONTAL

            horizontalScrollView.addView(cardContainer)

            val cardHomePage = CardHomePage()
            cardHomePage.init(this, cardContainer)

            linearLayout.addView(textView)
//            linearLayout.addView(button)
            linearLayout.addView(horizontalScrollView)

            card.addView(linearLayout)

            listCardContainer.addView(card)

        }

    }
}