package com.example.movieapp.ui.activity

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.movieapp.Api.MyViewModel
import com.example.movieapp.R
import com.example.movieapp.adapter.PaymentsAdapter
import com.example.movieapp.adapter.SlideAdapter
import com.example.movieapp.data.model.PayOrder
import com.example.movieapp.service.ServiceBuilder
import com.example.movieapp.service.ZaloPay.Api.CreateOrder
import com.example.movieapp.slideItem
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.makeramen.roundedimageview.RoundedImageView
import org.json.JSONObject
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener
import java.lang.Math.abs

class RegisterPremiumActivity : AppCompatActivity() , View.OnClickListener{
    private var Slidehandler : Handler = Handler()
    lateinit var viewpage : ViewPager2
    private lateinit var list : ArrayList<slideItem>
    private lateinit var recyclerView : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_premium)

        // Payments
        recyclerView = findViewById<RecyclerView>(R.id.recyclerPayment)
        val data : List<Int> = mutableListOf(R.drawable.zaloimg,R.drawable.qr_img,R.drawable.gate_img,R.drawable.momo_img,
            R.drawable.vnpay_img,R.drawable.onepay_img)
        recyclerView.layoutManager = GridLayoutManager(this,3)
        recyclerView.adapter = PaymentsAdapter(this,data)

        val button = findViewById<ImageButton>(R.id.imgButtonPremium)
        button.setOnClickListener(this)

        list = ArrayList()
        //list.add(slideItem(R.drawable.premium1))
        //list.add(slideItem(R.drawable.premium2))
        //list.add(slideItem(R.drawable.premium3))
        //list.add(slideItem(R.drawable.premium4))
        //list.add(slideItem(R.drawable.premium5))

        list.add(slideItem(R.drawable.premium_1))
        list.add(slideItem(R.drawable.premium_2))
        list.add(slideItem(R.drawable.premium_3))


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

        // Thanh toan zaloPay
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // ZaloPay SDK
        ZaloPaySDK.init(2553, Environment.SANDBOX)
        val buttonPay = findViewById<Button>(R.id.buttonPay)
        buttonPay.setOnClickListener(this)


        if (recyclerView.isActivated){
            val roundeImage : RoundedImageView = recyclerView[0].findViewById(R.id.checkContainer)
            Log.e("IDDDD",roundeImage.toString())
        }

        val btnPayMent = findViewById<Button>(R.id.btnPayInSheet)
        btnPayMent.setOnClickListener(this)

    }

    private fun requestZaloPay(){
        val viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        val tmp : LiveData<PayOrder> = viewModel.getTokenCreateOrder()
        var token : String
        tmp.observe(this) { tokens ->
            token = tokens.data.token
            Log.e("TOKEN",token)
            ZaloPaySDK.getInstance().payOrder(this, token, "demozpdk://app", object : PayOrderListener {
                    override fun onPaymentSucceeded(transactionId: String, transToken: String, appTransID: String) {
                        val viewModel = ViewModelProvider(this@RegisterPremiumActivity).get(MyViewModel::class.java)
                        viewModel.postZaloTransId(appTransID,this@RegisterPremiumActivity)
                        runOnUiThread(object : Runnable {
                            override fun run() {
                                Log.e("APPID",appTransID)
                                AlertDialog.Builder(this@RegisterPremiumActivity)
                                    .setTitle("Payment success")
                                    .setMessage(
                                        String.format(
                                            "TransactionId: %s - TransToken: %s",
                                            transactionId,
                                            transToken
                                        )
                                    )
                                    .setPositiveButton("OK",
                                        object : DialogInterface.OnClickListener {
                                            override fun onClick(
                                                dialog: DialogInterface?,
                                                which: Int
                                            ) {
                                            }
                                        })
                                    .setNegativeButton("Cancel", null).show()
                            }
                        })
                    }

                    override fun onPaymentCanceled(zpTransToken: String, appTransID: String) {
                        AlertDialog.Builder(this@RegisterPremiumActivity)
                            .setTitle("User Cancel Payment")
                            .setMessage(String.format("zpTransToken: %s \n", zpTransToken))
                            .setPositiveButton("OK", object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                }
                            })
                            .setNegativeButton("Cancel", null).show()
                    }

                    override fun onPaymentError(
                        zaloPayError: ZaloPayError?,
                        zpTransToken: String,
                        appTransID: String
                    ) {
                        AlertDialog.Builder(this@RegisterPremiumActivity)
                            .setTitle(" Payment Fail")
                            .setMessage(
                                String.format(
                                    "ZaloPayErrorCode: %s \nTransToken: %s",
                                    zaloPayError.toString(),
                                    zpTransToken
                                )
                            )
                            .setPositiveButton("OK", object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                }
                            })
                            .setNegativeButton("Cancel", null).show()
                    }
                })
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)

    }


    private var SlideRunnable : Runnable = Runnable {
        viewpage.setCurrentItem(viewpage.currentItem + 1)
    }


    private fun showBottomSheet(){
        val linearLayout = findViewById<LinearLayout>(R.id.layoutBottomSheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(linearLayout)
        if (bottomSheetBehavior.state!=BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }else{
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

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
            R.id.buttonPay -> showBottomSheet()
            R.id.btnPayInSheet ->{
                requestZaloPay()
            }

        }

    }



    }


