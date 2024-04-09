package com.example.movieapp.ui.login

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.movieapp.Helper
import com.example.movieapp.R
import com.example.movieapp.data.model.ClassToken
import com.example.movieapp.databinding.ActivityLoginBinding
import com.example.movieapp.ui.activity.HomePage_Activity
import com.example.movieapp.ui.activity.MainActivity
import com.example.movieapp.ui.activity.RegisterActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import java.util.Date


class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    lateinit var gso: GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contextView= this
        val bundle = intent.extras
        if (bundle != null) {
            val username = bundle.getString("username")
            val password = bundle.getString("password")
            Log.e("check1", username.toString())
            loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
                .get(LoginViewModel::class.java)
            loginViewModel.login(
                username.toString(),
                password.toString(),
                this
            )
        }
        try {
            val token = Helper.TokenManager.getToken(this)
            if (token != null && !isTokenExpired(token)) {
                val id = Helper.TokenManager.getId(this)
                val email = Helper.TokenManager.getEmail(this)
                val fullname = Helper.TokenManager.getFullName(this)
                val isActive = Helper.TokenManager.getIsActive(this)
                val role = Helper.TokenManager.getRoles(this)


                ClassToken.MY_TOKEN= token.toString()
                ClassToken.ID= id?: 0
                ClassToken.EMAIL= email.toString()
                ClassToken.FULLNAME= fullname.toString()
                ClassToken.IS_ACTIVE = isActive!!
                ClassToken.ROLES = role!!
//                val checkToken ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOjIsImlhdCI6MTcxMTI2MTU5MiwiZXhwIjoxNzExMjY4NzkyfQ.ijPEfRK325BP_3ubNSHkoxUWtbxfvPkntaav-zIeL-k"
//                Helper.TokenManager.saveToken(this, checkToken, ClassToken.ID, ClassToken.EMAIL, ClassToken.FULLNAME, ClassToken.IS_ACTIVE, ArrayList())
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        } catch (e: TokenExpiredException) {
            Helper.TokenManager.clearToken(this)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val login = binding.login
        val loading = binding.loading
        val googleBtn: Button = findViewById(R.id.loginGoogle)
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(this, gso)

        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            navigateToSecondActivity()
        }

        googleBtn.setOnClickListener {
            signIn()
        }

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if(loginState.isDataValid){
                login.setBackgroundColor(resources.getColor(android.R.color.black))
            }else{
                login.setBackgroundColor(resources.getColor(android.R.color.transparent))
            }

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
                showLoginError(loginResult.error)
                setResult(Activity.RESULT_OK)

                //Complete and destroy login activity once successful
                finish()
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
                setResult(Activity.RESULT_OK)

                //Complete and destroy login activity once successful
                finish()
            }
            if(loginResult.fail !== null){
                Log.e("haiduong","checllls")
                showLoginFailed(loginResult.fail)
            }

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
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString(),
                            contextView
                        )
                    }

                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE

                loginViewModel.login(
                    username.text.toString(),
                    password.text.toString(),
                    contextView
                )
            }
        }
        val underlinedTextView: TextView = findViewById(R.id.linkRegister)
        underlinedTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
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
    fun signIn() {
        val signInIntent = gsc.signInIntent
        startActivityForResult(signInIntent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    // Lấy thông tin cần thiết từ account
                    val displayName = account.displayName
                    val email = account.email
                    // ...
                    Log.e("CHECKED", email.toString())
                }
                navigateToSecondActivity()
            } catch (e: ApiException) {
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun navigateToSecondActivity() {
        finish()
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.fullname
        // TODO : initiate successful logged in experience
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginError(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }


    private fun showLoginFailed(errorString: String) {
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
