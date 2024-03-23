package com.example.movieapp.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.movieapp.Helper

import com.example.movieapp.data.model.LoginDTO
import java.util.concurrent.Executors
import com.example.movieapp.ui.activity.MainActivity
import com.example.movieapp.databinding.ActivityLoginBinding

import com.example.movieapp.R
import com.example.movieapp.data.model.ClassToken
import com.example.movieapp.data.model.DataDTO
import com.example.movieapp.service.ServiceBuilder
import com.example.movieapp.ui.activity.RegisterActivity
import java.util.Date

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val token = Helper.TokenManager.getToken(this)
        val f = Helper.TokenManager.getFullName(this)
        Log.e("TOKEN", token.toString())
        Log.e("TOKEN", f.toString())
        if (token != null && !isTokenExpired(token)) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(applicationContext, "Token is invalid or expired. Please log in again.", Toast.LENGTH_SHORT).show()
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val login = binding.login
        val loading = binding.loading

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->{
                        performNetWorkRequest(username.text.toString(), password.text.toString())
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                    }

                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                performNetWorkRequest(username.text.toString(), password.text.toString())
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }
        val underlinedTextView: TextView = findViewById(R.id.linkRegister)
        underlinedTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    fun isTokenExpired(jwtToken: String): Boolean {
        val algorithm = Algorithm.HMAC256("datisekai")
        val jwtVerifier = JWT.require(algorithm).build()
        val decodedJWT: DecodedJWT = jwtVerifier.verify(jwtToken)

        val expirationTime: Date? = decodedJWT.expiresAt
        if (expirationTime != null) {
            val currentTime = Date()
            return currentTime.after(expirationTime)
        }

        return true
    }
    private fun performNetWorkRequest(email: String, password: String) {
        val excutor = Executors.newSingleThreadExecutor()
        excutor.execute{
            val login = LoginDTO("datly030102@gmail.com", "datisekai")
            val result = ServiceBuilder().apiService.login(login).execute()
            if (result.isSuccessful){
                val token : String =result.body().data.accessToken
                val data : DataDTO =result.body().data
                ClassToken.MY_TOKEN= token
                ClassToken.DATA.user = data.user
                Helper.TokenManager.saveToken(this, token, data.user)
            }
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }


    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
