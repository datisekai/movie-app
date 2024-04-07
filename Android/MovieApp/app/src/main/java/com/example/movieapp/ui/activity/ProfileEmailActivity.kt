package com.example.movieapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.example.movieapp.Helper
import com.example.movieapp.R
import com.example.movieapp.data.model.ClassToken
import com.example.movieapp.data.model.UserDTO
import com.example.movieapp.service.ServiceBuilder
import java.util.concurrent.Executors

class ProfileEmailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frament_profile_detail_email)

        val btnConfirm = findViewById<AppCompatButton>(R.id.profile_detail_email_btn_confirm)
        val editText = findViewById<EditText>(R.id.profile_detail_email_editText)
        btnConfirm.setOnClickListener {
            val Email = editText.text.toString()
            if (Email?.isNotEmpty() == true){
                executeEditEmail(Email)
                editText.setText("")

                //Return To Profile Fragment
                val intent = Intent(this, HomePage_Activity::class.java)
                intent.putExtra("typeFragment",1)
                startActivity(intent)
            } else{
                val alertDialog = AlertDialog.Builder(this)
                    .setTitle("Thông báo")
                    .setMessage("Bạn chưa nhập email")
                    .setPositiveButton("OK"){ dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()

                alertDialog.show()
            }
        }
    }

    private fun executeEditEmail(email: String){
        val execute = Executors.newSingleThreadExecutor()
        execute.execute {
            val userDTO = UserDTO(ClassToken.ID, email,ClassToken.FULLNAME, ClassToken.IS_ACTIVE)

            val result = ServiceBuilder().apiService.editUser(ClassToken.ID,userDTO).execute()
            if (result.isSuccessful){
                val user = result.body().data
                ClassToken.ID = user.id
                ClassToken.FULLNAME= user.fullname
                ClassToken.EMAIL = user.email
                ClassToken.IS_ACTIVE = user.is_active
                Helper.TokenManager.saveToken(this, ClassToken.MY_TOKEN, ClassToken.ID, ClassToken.EMAIL, ClassToken.FULLNAME, ClassToken.IS_ACTIVE,ClassToken.ROLES)
            }

        }
    }

    public fun clickBack(view: View){
        finish()
    }
}