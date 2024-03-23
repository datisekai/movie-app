package com.example.movieapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import android.view.View

class ProfileDetailsActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_profile_details)

        //set onclick for profile to detail
        val buttonclickFullname: RelativeLayout = findViewById(R.id.profile_detail_fullname_onclick)
        buttonclickFullname.setOnClickListener(this)
        //set onclick from profile to history
        val buttonclickEmail: RelativeLayout = findViewById(R.id.profile_detail_email_onclick)
        buttonclickEmail.setOnClickListener(this)
        //set onclick from profile to history
        val buttonclickPhonenumber: RelativeLayout = findViewById(R.id.profile_detail_phonenumber_onclick)
        buttonclickPhonenumber.setOnClickListener(this)
        //set onclick from profile to history
        val buttonclickBirthday: RelativeLayout = findViewById(R.id.profile_detail_birthday_onclick)
        buttonclickBirthday.setOnClickListener(this)
        //set onclick from profile to history
        val buttonclickPassword: RelativeLayout = findViewById(R.id.profile_detail_password_onclick)
        buttonclickPassword.setOnClickListener(this)
        //set onclick from profile to history
        val buttonclickGender: RelativeLayout = findViewById(R.id.profile_detail_gender_onclick)
        buttonclickGender.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.profile_detail_fullname_onclick -> {
                val intent = Intent(this,ProfileFullNameActivity::class.java)
                startActivity(intent)
            }

            R.id.profile_detail_email_onclick -> {
                val intent = Intent(this,ProfileEmailActivity::class.java)
                startActivity(intent)
            }

            R.id.profile_detail_phonenumber_onclick ->{
                val intent = Intent(this,ProfilePhoneNumberActivity::class.java)
                startActivity(intent)
            }

            R.id.profile_detail_birthday_onclick ->{
            }

            R.id.profile_detail_password_onclick ->{
                val intent = Intent(this,ProfilePasswordActivity::class.java)
                startActivity(intent)
            }

            R.id.profile_detail_gender_onclick ->{
            }

        }
    }
}