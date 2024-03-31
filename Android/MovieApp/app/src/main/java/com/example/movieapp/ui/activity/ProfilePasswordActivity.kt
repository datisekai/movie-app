package com.example.movieapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.example.movieapp.Helper
import com.example.movieapp.R
import com.example.movieapp.data.model.ClassToken
import com.example.movieapp.data.model.UserDTO
import com.example.movieapp.service.ServiceBuilder
import java.util.concurrent.Executors

class ProfilePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_profile_detail_password)

        val btnSave = findViewById<AppCompatButton>(R.id.profile_detail_password_btn_confirm)
        val editTextNewPassword = findViewById<EditText>(R.id.profile_detail_password_editText_newpwd)
        val editTextReEnter = findViewById<TextView>(R.id.profile_detail_password_editText_renewpwd)
    }

    private fun executeEditPassword(password: String){
        val execute = Executors.newSingleThreadExecutor()
        execute.execute {
            

        }
    }

    public fun clickBack(view: View){
        finish()
    }
}