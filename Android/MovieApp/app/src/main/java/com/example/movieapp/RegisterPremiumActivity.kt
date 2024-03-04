package com.example.movieapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2

class RegisterPremiumActivity : AppCompatActivity() {
    private var Slidehandler : Handler = Handler()
    lateinit var viewpage : ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_premium)

        viewpage = findViewById(R.id.viewPager)
        val list : MutableList<slideItem> = arrayListOf(
            slideItem(R.drawable.premium1), slideItem(R.drawable.premium2),
            slideItem(R.drawable.premium3), slideItem(R.drawable.premium4), slideItem(R.drawable.premium5)
        )
        viewpage.adapter = SlideAdapter(list,viewpage)
        viewpage.clipToPadding = false
        viewpage.clipChildren = false
        viewpage.offscreenPageLimit = 5
        viewpage.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        val composition : CompositePageTransformer = CompositePageTransformer()
        composition.addTransformer(MarginPageTransformer(30))
        composition.addTransformer(ViewPager2.PageTransformer { page, position ->
            var r : Float = Math.abs(position)
            page.scaleY = 0.085f + r*0.15f
        })
        viewpage.setPageTransformer(composition)

        viewpage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position : Int){
                Slidehandler.removeCallbacks(SlideRunnable)
                Slidehandler.postDelayed(SlideRunnable,2000)

            }

        })

    }

    private var SlideRunnable : Runnable = Runnable {
        viewpage.setCurrentItem(viewpage.currentItem + 1)
    }

    override fun onPause() {
        super.onPause()
        Slidehandler.removeCallbacks(SlideRunnable)
    }

    override fun onResume() {
        super.onResume()
        Slidehandler.postDelayed(SlideRunnable,3000)
    }
}