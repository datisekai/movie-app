package com.example.movieapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.example.movieapp.Helper
import com.example.movieapp.R
import com.example.movieapp.data.model.ClassToken
import com.example.movieapp.data.model.EditPasswordUserDTO
import com.example.movieapp.data.model.UserDTO
import com.example.movieapp.databinding.FragmentProfileDetailPasswordBinding
import com.example.movieapp.service.ServiceBuilder
import java.util.concurrent.Executors

class ProfilePasswordActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener{

    private lateinit var binding: FragmentProfileDetailPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentProfileDetailPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnSave = binding.profileDetailPasswordBtnConfirm
        val editTextNewPassword = binding.profileDetailPasswordEditTextNewpwd
        val editTextReEnter = binding.profileDetailPasswordEditTextRenewpwd

        editTextNewPassword.onFocusChangeListener = this
        editTextReEnter.onFocusChangeListener = this
        btnSave.setOnClickListener(this)
    }

    private fun validatePassword(): Boolean{
        var errorMes: String? = null
        val value = binding.profileDetailPasswordEditTextNewpwd.text.toString()
        if(value.isEmpty()){
            errorMes = "Password is require"
        } else if(value.length < 5){
            errorMes = "Password must be greater than 5 characters"
        }

        if(errorMes != null){
            binding.profileDetailPasswordEditTextNewpwd.apply {
                error = errorMes
            }
        }
        return  errorMes == null
    }
    private fun validateConfirmPassword(): Boolean{
        var errorMes: String? = null
        val value = binding.profileDetailPasswordEditTextRenewpwd.text.toString()
        if(value.isEmpty()){
            errorMes = "Password is require"
        } else if(value.length < 5){
            errorMes = "Password must be greater than 5 characters"
        }

        if(errorMes != null){
            binding.profileDetailPasswordEditTextRenewpwd.apply {
                error = errorMes
            }
        }
        return  errorMes == null
    }
    private fun validatePasswordAndConfirmPassword():Boolean{
        var errorMes: String? = null
        val password = binding.profileDetailPasswordEditTextRenewpwd.text.toString()
        val cfPassword = binding.profileDetailPasswordEditTextNewpwd.text.toString()
        if(password != cfPassword){
            errorMes = "Confirm Password doesn't match with password"
        }

        if(errorMes != null){
            binding.profileDetailPasswordEditTextRenewpwd.apply {
                error = errorMes
            }
        }
        return  errorMes == null
    }

    override fun onClick(view: View?) {
        if(view != null && view.id == R.id.profile_detail_password_btn_confirm){
            if(validatePassword() && validateConfirmPassword() && validatePasswordAndConfirmPassword()){
                val result = executeEditPassword(binding.profileDetailPasswordEditTextNewpwd.text.toString())
                if(result){
                    val alertSuccessDialog = AlertDialog.Builder(this)
                        .setTitle("Thông báo")
                        .setMessage("Sửa mật khẩu thành công")
                        .setPositiveButton("OK"){ dialog, _ ->
                            dialog.dismiss()

                            binding.profileDetailPasswordEditTextNewpwd.setText("")
                            binding.profileDetailPasswordEditTextRenewpwd.setText("")
                        }
                        .create()
                    alertSuccessDialog.show()

                    //Return To Profile Fragment
                    val intent = Intent(this, HomePage_Activity::class.java)
                    intent.putExtra("typeFragment",1)
                    startActivity(intent)
                }else{
                    val alertFailDialog = AlertDialog.Builder(this)
                        .setTitle("Thông báo")
                        .setMessage("Sửa mật khẩu thất bại")
                        .setPositiveButton("OK"){ dialog, _ ->
                            dialog.dismiss()
                            binding.profileDetailPasswordEditTextNewpwd.setText("")
                            binding.profileDetailPasswordEditTextRenewpwd.setText("")
                        }
                        .create()
                    alertFailDialog.show()
                }
            }
            else{
                val alertDialog = AlertDialog.Builder(this)
                    .setTitle("Thông báo")
                    .setMessage("Mật khẩu đã nhập không phù hợp")
                    .setPositiveButton("OK"){ dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()

                alertDialog.show()
            }
        }
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if(view != null){
            when(view.id){
                R.id.profile_detail_password_editText_newpwd ->{
                    if(!hasFocus){
                        validatePassword()
                    }
                }
                R.id.profile_detail_password_editText_renewpwd ->{
                    if(!hasFocus){
                        validateConfirmPassword()
                        validatePasswordAndConfirmPassword()
                    }
                }
            }
        }
    }

    private fun executeEditPassword(password: String): Boolean {
        val execute = Executors.newSingleThreadExecutor()

        val futureResult = execute.submit<Boolean> {
            val userDTO = EditPasswordUserDTO(ClassToken.ID,ClassToken.EMAIL,ClassToken.FULLNAME,ClassToken.IS_ACTIVE,password)

            val getApiResult = ServiceBuilder().apiService.editPasswordUser(ClassToken.ID,userDTO).execute()
            if(getApiResult.isSuccessful){
                return@submit true
            }
            return@submit false
        }

        val result = futureResult.get()
        if(result){
            return true
        }else{
            return false
        }
    }

    fun clickBack(view: View){
        finish()
    }
}