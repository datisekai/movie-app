package com.example.movieapp

import android.animation.Animator
import android.graphics.Color
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.movieapp.Api.ServiceBuilder
import com.example.movieapp.data.model.LoginDTO
import java.lang.Math.abs
import java.util.concurrent.Executors

class RegisterPremiumActivity : AppCompatActivity() , View.OnClickListener{
    private var Slidehandler : Handler = Handler()
    lateinit var viewpage : ViewPager2
    private lateinit var list : ArrayList<slideItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_premium)

        setColorTextButton()
        val button_pack1 = findViewById<ImageButton>(R.id.buttonPack1)
        button_pack1.setOnClickListener(this)

        val button_pack2 = findViewById<ImageButton>(R.id.buttonPack2)
        button_pack2.setOnClickListener(this)

        val button_pack3 = findViewById<ImageButton>(R.id.buttonPack3)
        button_pack3.setOnClickListener(this)

        val button_pack4 = findViewById<ImageButton>(R.id.buttonPack4)
        button_pack4.setOnClickListener(this)

        val button = findViewById<ImageButton>(R.id.imgButtonPremium)
        button.setOnClickListener(this)

        list = ArrayList()
        list.add(slideItem(R.drawable.premium1))
        list.add(slideItem(R.drawable.premium2))
        list.add(slideItem(R.drawable.premium3))
        list.add(slideItem(R.drawable.premium4))
        list.add(slideItem(R.drawable.premium5))


        viewpage = findViewById(R.id.viewPager)
        viewpage.adapter = SlideAdapter(list,viewpage)
        viewpage.clipToPadding = false
        viewpage.clipChildren = false
        viewpage.offscreenPageLimit = 5
        viewpage.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        val composition : CompositePageTransformer = CompositePageTransformer()
        composition.addTransformer(MarginPageTransformer(30))
        composition.addTransformer(ViewPager2.PageTransformer { page, position ->
            var r  =1- abs(position)
            page.scaleY = 0.85f + r + 0.15f
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

    private fun setColorTextButton(){
        val radio1_1 = findViewById<RadioButton>(R.id.radion_one)
        setColorForPrice(radio1_1.text.toString(),radio1_1)
        val radio1_2 = findViewById<RadioButton>(R.id.radion_two)
        setColorForPrice(radio1_2.text.toString(),radio1_2)
        val radio1_3 = findViewById<RadioButton>(R.id.radion_three)
        setColorForPrice(radio1_3.text.toString(),radio1_3)

        val radio2_1 = findViewById<RadioButton>(R.id.radion_1)
        setColorForPrice(radio2_1.text.toString(),radio2_1)
        val radio2_2 = findViewById<RadioButton>(R.id.radion_2)
        setColorForPrice(radio2_2.text.toString(),radio2_2)
        val radio2_3 = findViewById<RadioButton>(R.id.radion_3)
        setColorForPrice(radio2_3.text.toString(),radio2_3)

        val radio3_1 = findViewById<RadioButton>(R.id.radion_1_pack3)
        setColorForPrice(radio3_1.text.toString(),radio3_1)
        val radio3_2 = findViewById<RadioButton>(R.id.radion_2_pack3)
        setColorForPrice(radio3_2.text.toString(),radio3_2)
        val radio3_3 = findViewById<RadioButton>(R.id.radion_3_pack3)
        setColorForPrice(radio3_3.text.toString(),radio3_3)

        val radio4_1 = findViewById<RadioButton>(R.id.radion_1_pack4)
        setColorForPrice(radio4_1.text.toString(),radio4_1)
        val radio4_2 = findViewById<RadioButton>(R.id.radion_2_pack4)
        setColorForPrice(radio4_2.text.toString(),radio4_2)
        val radio4_3 = findViewById<RadioButton>(R.id.radion_3_pack4)
        setColorForPrice(radio4_3.text.toString(),radio4_3)

    }

    private fun setColorForPrice(text:String, radioButton: RadioButton){
        val spannableString : SpannableString = SpannableString(text)

        // Đặt màu
        val colorspan : ForegroundColorSpan = ForegroundColorSpan(Color.GREEN)
        val lineBreakIndex : Int = text.indexOf("\n")
        spannableString.setSpan(colorspan,lineBreakIndex+1,text.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        radioButton.text = spannableString
    }

    override fun onPause() {
        super.onPause()
        Slidehandler.removeCallbacks(SlideRunnable)
    }

    override fun onResume() {
        super.onResume()
        Slidehandler.postDelayed(SlideRunnable,3000)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.imgButtonPremium -> {
                finish()
            }
            R.id.buttonPack1 -> {
                val radiogp3 = findViewById<RadioGroup>(R.id.radioGroup3)
                val button3 = findViewById<ImageButton>(R.id.buttonPack3)

                val radiogp1 = findViewById<RadioGroup>(R.id.radioGroup)
                val button1 = findViewById<ImageButton>(R.id.buttonPack1)

                val radiogp2 = findViewById<RadioGroup>(R.id.radioGroup2)
                val button2 = findViewById<ImageButton>(R.id.buttonPack2)

                val radiogp4 = findViewById<RadioGroup>(R.id.radioGroup4)
                val button4 = findViewById<ImageButton>(R.id.buttonPack4)

                if (radiogp2.visibility==View.VISIBLE){
                    radiogp2.visibility=View.GONE
                    button2.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
                }else if(radiogp3.visibility==View.VISIBLE){
                    radiogp3.visibility=View.GONE
                    button3.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
                }else if(radiogp4.visibility==View.VISIBLE){
                    radiogp4.visibility=View.GONE
                    button4.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
                }

                animationGropDown(radiogp1,button1)
            }
            R.id.buttonPack2 -> {
                val radiogp3 = findViewById<RadioGroup>(R.id.radioGroup3)
                val button3 = findViewById<ImageButton>(R.id.buttonPack3)

                val radiogp1 = findViewById<RadioGroup>(R.id.radioGroup)
                val button1 = findViewById<ImageButton>(R.id.buttonPack1)

                val radiogp2 = findViewById<RadioGroup>(R.id.radioGroup2)
                val button2 = findViewById<ImageButton>(R.id.buttonPack2)

                val radiogp4 = findViewById<RadioGroup>(R.id.radioGroup4)
                val button4 = findViewById<ImageButton>(R.id.buttonPack4)

                if (radiogp1.visibility==View.VISIBLE){
                    radiogp1.visibility=View.GONE
                    button1.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
                }else if(radiogp3.visibility==View.VISIBLE){
                    radiogp3.visibility=View.GONE
                    button3.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
                }else if(radiogp4.visibility==View.VISIBLE){
                    radiogp4.visibility=View.GONE
                    button4.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
                }

                animationGropDown(radiogp2,button2)
            }
            R.id.buttonPack3 -> {
                val radiogp3 = findViewById<RadioGroup>(R.id.radioGroup3)
                val button3 = findViewById<ImageButton>(R.id.buttonPack3)

                val radiogp1 = findViewById<RadioGroup>(R.id.radioGroup)
                val button1 = findViewById<ImageButton>(R.id.buttonPack1)

                val radiogp2 = findViewById<RadioGroup>(R.id.radioGroup2)
                val button2 = findViewById<ImageButton>(R.id.buttonPack2)

                val radiogp4 = findViewById<RadioGroup>(R.id.radioGroup4)
                val button4 = findViewById<ImageButton>(R.id.buttonPack4)

                if (radiogp1.visibility==View.VISIBLE){
                    radiogp1.visibility=View.GONE
                    button1.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
                }else if(radiogp2.visibility==View.VISIBLE){
                    radiogp2.visibility=View.GONE
                    button2.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
                }else if(radiogp4.visibility==View.VISIBLE){
                    radiogp4.visibility=View.GONE
                    button4.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
                }

                animationGropDown(radiogp3,button3)
            }
            R.id.buttonPack4 -> {
                val radiogp3 = findViewById<RadioGroup>(R.id.radioGroup3)
                val button3 = findViewById<ImageButton>(R.id.buttonPack3)

                val radiogp1 = findViewById<RadioGroup>(R.id.radioGroup)
                val button1 = findViewById<ImageButton>(R.id.buttonPack1)

                val radiogp2 = findViewById<RadioGroup>(R.id.radioGroup2)
                val button2 = findViewById<ImageButton>(R.id.buttonPack2)

                val radiogp4 = findViewById<RadioGroup>(R.id.radioGroup4)
                val button4 = findViewById<ImageButton>(R.id.buttonPack4)

                if (radiogp1.visibility==View.VISIBLE){
                    radiogp1.visibility=View.GONE
                    button1.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
                }else if(radiogp2.visibility==View.VISIBLE){
                    radiogp2.visibility=View.GONE
                    button2.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
                }else if(radiogp3.visibility==View.VISIBLE){
                    radiogp3.visibility=View.GONE
                    button3.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
                }

                animationGropDown(radiogp4,button4)
            }

        }

    }

    private fun animationGropDown(radiogp:RadioGroup,button:ImageButton) {
        val scrollView = findViewById<NestedScrollView>(R.id.scrollView)
        if (radiogp.visibility == View.VISIBLE){
            button.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
            radiogp.animate()
                .alpha(0.0f)
                .translationY(0f)
                .setDuration(300)
                .setListener(object : Animator.AnimatorListener{
                    override fun onAnimationStart(animation: Animator) {
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        radiogp.visibility = View.GONE
                        scrollView.requestLayout()
                    }

                    override fun onAnimationCancel(animation: Animator) {
                    }

                    override fun onAnimationRepeat(animation: Animator) {
                    }

                })
        }else{
            button.setImageResource(R.drawable.baseline_keyboard_arrow_up_24)
            radiogp.visibility = View.VISIBLE
            radiogp.alpha = 0.0f
            radiogp.translationY = -radiogp.height.toFloat() + 10
            radiogp.animate()
                .alpha(1.0f)
                .translationY(0f)
                .setDuration(300)
                .setListener(null)
        }
    }
}