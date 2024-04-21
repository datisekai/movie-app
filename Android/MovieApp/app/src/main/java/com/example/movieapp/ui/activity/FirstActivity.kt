package com.example.movieapp.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.movieapp.Helper
import com.example.movieapp.R
import com.example.movieapp.data.model.ClassToken
import com.example.movieapp.ui.fragment.BlogFragment
import com.example.movieapp.ui.login.LoginActivity
import java.util.Date


class firstActivity : AppCompatActivity() {
    private  lateinit var videoView: VideoView
    var flag = false
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        videoView = findViewById(R.id.videoView)
        val videoPath = "android.resource://$packageName/raw/my_video"
        videoView.setVideoPath(videoPath)
        videoView.start()
        videoView.setOnCompletionListener {
            videoView.stopPlayback();
            if (isNetworkConnected(this)){
               if (intent.extras!=null && intent.extras!!.getString("id")!=null){
                  checkLogin("NOTIFICATION")
               }else{
                   checkLogin("LOGIN")
               }
            }else{
                val intent = Intent(this, CheckConnectActivity::class.java)
                startActivity(intent)
                finish()
            }
        }






    }

    private fun checkLogin(key : String){
        try {
            val token = Helper.TokenManager.getToken(this)
            if (token != null && !isTokenExpired(token)) {
                val id = Helper.TokenManager.getId(this)
                val email = Helper.TokenManager.getEmail(this)
                val fullname = Helper.TokenManager.getFullName(this)
                val isActive = Helper.TokenManager.getIsActive(this)
                val role = Helper.TokenManager.getRoles(this)
                var roles : ArrayList<String> = arrayListOf()
                val tmp = role?.split(",")
                if (tmp!=null){
                    for (o in tmp){
                        roles.add(o)
                    }
                }
                ClassToken.MY_TOKEN= token.toString()
                ClassToken.ID= id?: 0
                ClassToken.EMAIL= email.toString()
                ClassToken.FULLNAME= fullname.toString()
                ClassToken.IS_ACTIVE = isActive!!
                ClassToken.ROLES = roles
//                val checkToken ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOjIsImlhdCI6MTcxMTI2MTU5MiwiZXhwIjoxNzExMjY4NzkyfQ.ijPEfRK325BP_3ubNSHkoxUWtbxfvPkntaav-zIeL-k"
//                Helper.TokenManager.saveToken(this, checkToken, ClassToken.ID, ClassToken.EMAIL, ClassToken.FULLNAME, ClassToken.IS_ACTIVE, ArrayList())
               if (key=="NOTIFICATION"){
                   val mainIntent = Intent(this,HomePage_Activity::class.java)
                   mainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                   startActivity(mainIntent)

                   val filmId = intent.extras!!.getString("id")


                   val intent = Intent(this,DetailFilmActivity::class.java)
                   val bundle = Bundle()
                   bundle.putInt("ID", filmId?.toInt() ?: 0)
                   bundle.putBoolean("IS_ACTIVITY",false)
                   intent.putExtra("DataID",bundle)
                   startActivity(intent)

                   finish()
               }else{
                   val intent = Intent(this, MainActivity::class.java)
                   startActivity(intent)
                   finish()
               }
            }

        } catch (e: TokenExpiredException) {
            Helper.TokenManager.clearToken(this)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    fun isTokenExpired(jwtToken: String): Boolean {
        try {
            val algorithm = Algorithm.HMAC256("datisekai")
            val jwtVerifier = JWT.require(algorithm).build()
            val decodedJWT: DecodedJWT = jwtVerifier.verify(jwtToken)

            val expirationTime: Date? = decodedJWT.expiresAt
            if (expirationTime != null) {
                val currentTime = Date()
                return currentTime.after(expirationTime)
            }
        } catch (e: SignatureVerificationException) {
            if (e is TokenExpiredException) {
                return true
            }
            // Xử lý các ngoại lệ khác nếu cần thiết
        } catch (e: Exception) {
            return true
        }

        return true
    }

    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }



}