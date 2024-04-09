package com.example.movieapp.ui.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.movieapp.Helper
import com.example.movieapp.R
import com.example.movieapp.data.Result
import com.example.movieapp.data.model.ClassToken
import com.example.movieapp.data.model.LoginDTO
import com.example.movieapp.data.model.RegisterDTO
import com.example.movieapp.data.model.RegisterUserDTO
import com.example.movieapp.data.model.TokenDTO
import com.example.movieapp.data.model.UserDTO
import com.example.movieapp.service.ServiceBuilder
import com.example.movieapp.ui.login.LoginActivity
import com.example.movieapp.ui.login.LoginFormState
import com.example.movieapp.ui.login.LoginViewModel
import com.example.movieapp.ui.login.afterTextChanged
import java.io.IOException
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future

data class registerFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val fullNameError: Int? = null,
    val isDataValid: Boolean= false
)
class RegisterActivity : AppCompatActivity() {
    private val _registerForm = MutableLiveData<registerFormState>()
    val registerFormState: LiveData<registerFormState> = _registerForm
    private lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val textUserName: EditText = findViewById(R.id.usernameRegister)
        val textPassword: EditText = findViewById(R.id.passwordRegister)
        val textFullName: EditText = findViewById(R.id.fullname)
        val btn_register: Button = findViewById(R.id.register)
        registerFormState.observe(this@RegisterActivity, Observer {
            val registerState = it ?: return@Observer

            // disable login button unless both username / password is valid
            btn_register.isEnabled = registerState.isDataValid

            if(registerState.isDataValid){
                btn_register.setBackgroundColor(resources.getColor(android.R.color.black))
            }else{
                btn_register.setBackgroundColor(resources.getColor(android.R.color.transparent))
            }


            if (registerState.usernameError != null) {
                textUserName.error = getString(registerState.usernameError)
            }
            if (registerState.passwordError != null) {
                textPassword.error = getString(registerState.passwordError)
            }
            if (registerState.fullNameError != null) {
                textFullName.error = getString(registerState.fullNameError)
            }
        })

        textUserName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                registerDataChanged(
                    textUserName.text.toString(),
                    textPassword.text.toString(),
                    textFullName.text.toString())
            }
        })


        textFullName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                registerDataChanged(
                    textUserName.text.toString(),
                    textPassword.text.toString(),
                    textFullName.text.toString())
            }
        })

        textPassword.apply {
            textPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    registerDataChanged(
                        textUserName.text.toString(),
                        textPassword.text.toString(),
                        textFullName.text.toString())
                }
            })
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        register(
                            textUserName.text.toString(),
                            textPassword.text.toString(),
                            textFullName.text.toString()
                        )
                    }
                }
                false
            }
        }


        val underlinedTextView: TextView = findViewById(R.id.linkLogin)
        underlinedTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
    fun register(username: String, password: String, fullName: String){
        var user= RegisterUserDTO()
        try {
            // TODO: handle loggedInUser authentication
            val executor = Executors.newSingleThreadExecutor()
            val userFuture: Future<RegisterUserDTO> = executor.submit(Callable<RegisterUserDTO> {
                val register = RegisterDTO(username, password, fullName)
                val result = ServiceBuilder().apiService.register(register).execute()
                if (result.isSuccessful) {
                    user = result.body().data
                }
                user
            })

            val user = userFuture.get() // Chờ cho đến khi kết quả của future có sẵn
            if (user.id == 0 && user.email.isEmpty() && user.fullname.isEmpty() && !user.is_active) {
                showMessage("User already registered with email")
            }else{
                showMessage("Successful registered")
                Log.e("check3", username)
                val intent = Intent(this, LoginActivity::class.java)
                val bundle = Bundle()
                bundle.putString("username", username)
                bundle.putString("password", password)
                intent.putExtras(bundle)
                startActivity(intent)
                loginViewModel.login(username, password, this)
            }

        } catch (e: Throwable) {
            showMessage("Error registered")
        }
    }
    private fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
    fun registerDataChanged(username: String, password: String, fullName: String) {
        if (!isUserNameValid(username)) {
            _registerForm.value = registerFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _registerForm.value = registerFormState(passwordError = R.string.invalid_password)
        } else if(!isFullNameValid(fullName)) {
            _registerForm.value = registerFormState(fullNameError = R.string.invalid_fullname)
        }else {
            _registerForm.value = registerFormState(isDataValid = true)
        }
    }
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
    private fun isFullNameValid(fullName: String): Boolean {
        return fullName.isNotEmpty()
    }

}