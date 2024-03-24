package com.example.movieapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.example.movieapp.Helper
import com.example.movieapp.R
import com.example.movieapp.data.model.ClassToken
import com.example.movieapp.data.model.TokenDTO
import com.example.movieapp.data.model.UserDTO
import com.example.movieapp.service.ApiService
import com.example.movieapp.service.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

class ProfileFullNameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_profile_detail_fullname)

        val context = this
        val btnSave = findViewById<AppCompatButton>(R.id.profile_detail_fullname_btn_confirm)
        val editText = findViewById<EditText>(R.id.profile_detail_fullname_editText)
        btnSave.setOnClickListener {
            val Fullname = editText.text.toString()
            if (Fullname?.isNotEmpty() == true){
                executeEditFullname(Fullname)
                editText.setText("")
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
            Log.e("TOKEN",ClassToken.MY_TOKEN)
            val userDTO = UserDTO(ClassToken.ID,ClassToken.EMAIL,fullname,ClassToken.IS_ACTIVE)
            val context = this

//            val result = ServiceBuilder().apiService.editUser(ClassToken.ID,userDTO).enqueue(object: Callback<TokenDTO>{
//                override fun onResponse(call: Call<TokenDTO>, response: Response<TokenDTO>) {
//                    if (response.isSuccessful) {
//                        val user = response.body().data.user
//                        Log.e("CHeck",user.fullname)
//                        ClassToken.FULLNAME= fullname
//                        Helper.TokenManager.saveToken(context, ClassToken.MY_TOKEN, ClassToken.ID, ClassToken.EMAIL, ClassToken.FULLNAME, ClassToken.IS_ACTIVE)
//                    } else {
//                        // Xử lý lỗi 401 Unauthorized ở đây
//                    }
//                }
//
//                override fun onFailure(call: Call<TokenDTO>, t: Throwable) {
//                    // Xử lý lỗi kết nối hoặc yêu cầu không thành công ở đây
//                }
//            })

            val result = ServiceBuilder().apiService.editUser(ClassToken.ID,userDTO).execute()
            if (result.isSuccessful){
                val user : UserDTO =result.body().data.user
                Log.e("Check",user.fullname)
                ClassToken.ID= user.id
                ClassToken.EMAIL= user.email
                ClassToken.FULLNAME= user.fullname
                ClassToken.IS_ACTIVE = user.is_active
                Helper.TokenManager.saveToken(this, ClassToken.MY_TOKEN, ClassToken.ID, ClassToken.EMAIL, ClassToken.FULLNAME, ClassToken.IS_ACTIVE)
            }

        }
    }

    public fun clickBack(view: View){
        finish()
    }
}