package com.example.movieapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.movieapp.Helper
import com.example.movieapp.R
import com.example.movieapp.data.model.ClassToken
import com.example.movieapp.data.model.UserDTO
import com.example.movieapp.service.ServiceBuilder
import java.util.concurrent.Executors


class ProfileFullNameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_profile_detail_fullname)

        val btnSave = findViewById<AppCompatButton>(R.id.profile_detail_fullname_btn_confirm)
        val editText = findViewById<EditText>(R.id.profile_detail_fullname_editText)
        val oldTv = findViewById<TextView>(R.id.profile_detail_oldfullname_textView)

        oldTv.setText(ClassToken.FULLNAME)
        btnSave.setOnClickListener {
            val Fullname = editText.text.toString()
            if (Fullname?.isNotEmpty() == true){
                executeEditFullname(Fullname)
                editText.setText("")
                oldTv.setText(ClassToken.FULLNAME)

                //Return To Profile Fragment
                val intent = Intent(this, HomePage_Activity::class.java)
                intent.putExtra("typeFragment",1)
                startActivity(intent)
            } else{
                val alertDialog = AlertDialog.Builder(this)
                    .setTitle("Thông báo")
                    .setMessage("Bạn chưa nhập tên")
                    .setPositiveButton("OK"){ dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()

                alertDialog.show()
            }
        }
    }

    private fun executeEditFullname(fullname: String){
        val execute = Executors.newSingleThreadExecutor()
        execute.execute {
            val userDTO = UserDTO(ClassToken.ID,ClassToken.EMAIL,fullname,ClassToken.IS_ACTIVE)

            val result = ServiceBuilder().apiService.editUser(ClassToken.ID,userDTO).execute()
            if (result.isSuccessful){
                val user = result.body().data
                ClassToken.ID = user.id
                ClassToken.FULLNAME= user.fullname
                ClassToken.EMAIL = user.email
                ClassToken.IS_ACTIVE = user.is_active
                ClassToken.ROLES = user.roles
                Helper.TokenManager.saveToken(this, ClassToken.MY_TOKEN, ClassToken.ID, ClassToken.EMAIL, ClassToken.FULLNAME, ClassToken.IS_ACTIVE,ClassToken.ROLES)
            }

        }
    }

    public fun clickBack(view: View){
        finish()
    }
}