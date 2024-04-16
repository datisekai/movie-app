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
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.lifecycle.ViewModelProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.movieapp.Helper
import com.example.movieapp.R
import com.example.movieapp.config
import com.example.movieapp.data.model.ClassToken
import com.example.movieapp.data.model.RequestFcmToken
import com.example.movieapp.databinding.ActivityLoginBinding
import com.example.movieapp.service.FcmTokenViewModel
import com.example.movieapp.ui.activity.HomePage_Activity
import com.example.movieapp.ui.activity.MainActivity
import com.example.movieapp.ui.activity.RegisterActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import java.util.Date


class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    lateinit var gso: GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient
    private val contextView= this
    lateinit var googleBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras
        if (bundle != null) {
            val username = bundle.getString("username")
            val password = bundle.getString("password")
            loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
                .get(LoginViewModel::class.java)
            loginViewModel.login(
                username.toString(),
                password.toString(),
                this,
                "LOGIN",
                ""
            )
        }
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val login = binding.login
        val loading = binding.loading

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

        googleBtn = findViewById(R.id.loginGoogle)

        googleBtn.setOnClickListener {
            signIn()
        }

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(config.WEB_CLIENT_ID)
            .build()

        gsc = GoogleSignIn.getClient(this, gso)

//        val acct = GoogleSignIn.getLastSignedInAccount(this)
//        if (acct != null) {
//            navigateToSecondActivity()
//        }


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
                            contextView,
                            "LOGIN",
                            ""
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
                    contextView,
                    "LOGIN",
                    ""
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
        googleBtn.isEnabled = true
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
                    val idToken =account.idToken
                    loginViewModel.login(
                        "",
                        "",
                        contextView,
                        "GOOGLE",
                        idToken.toString()
                        )
                }
                googleBtn.isEnabled = false
            } catch (e: ApiException) {
                Log.e("ERROR",e.toString())
                e.printStackTrace()

                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
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

        getFCMToken()

    }

    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("MAINACTIVITY", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d("MAINACTIVITY", token)
            val viewModel = ViewModelProvider(this).get(FcmTokenViewModel::class.java)
            viewModel.putFCMToken(RequestFcmToken(token))


        })
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
