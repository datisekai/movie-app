package com.example.movieapp.ui.login

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.movieapp.data.LoginDataSource
import com.example.movieapp.data.LoginRepository
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
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import java.util.Date


class LoginActivity : AppCompatActivity(){

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    lateinit var gso: GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient
    private val contextView= this
    lateinit var googleBtn: Button
    private lateinit var logoutLogin: LoginRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(config.WEB_CLIENT_ID)
            .build()

        gsc = GoogleSignIn.getClient(this, gso)

        FirebaseApp.initializeApp(this)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        val bundle = intent.extras
        if (bundle != null) {
            val username = bundle.getString("username")
            val password = bundle.getString("password")

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



        googleBtn = findViewById(R.id.loginGoogle)

        googleBtn.setOnClickListener {
            if (isNetworkConnected(this)){
                signIn()
            }else{
                customeToast("Không có kết nối mạng")
            }

        }


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
                        if (isNetworkConnected(this@LoginActivity)){
                            loginViewModel.login(
                                username.text.toString(),
                                password.text.toString(),
                                contextView,
                                "LOGIN",
                                ""
                            )
                        }else{
                            customeToast("Không có kết nối mạng")
                        }
                    }

                }
                false
            }

            login.setOnClickListener {
              if (isNetworkConnected(this@LoginActivity)){
                  loading.visibility = View.VISIBLE

                  loginViewModel.login(
                      username.text.toString(),
                      password.text.toString(),
                      contextView,
                      "LOGIN",
                      ""
                  )
              }else{
                  customeToast("Không có kết nối mạng")
              }
            }
        }
        val underlinedTextView: TextView = findViewById(R.id.linkRegister)
        underlinedTextView.setOnClickListener {
           if (isNetworkConnected(this)){
               val intent = Intent(this, RegisterActivity::class.java)
               startActivity(intent)
           }else{
               customeToast("Không có kết nối mạng")
           }
        }
    }

    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }

    private fun customeToast(message : String){
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.custome_toast,this.findViewById(R.id.CustomToast))
        val toast = Toast(this)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = view
        val txt : TextView = view.findViewById(R.id.txtMessage)
        txt.text = message
        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 100)
        toast.show()
    }


    fun signIn() {
        googleBtn.isEnabled = true
        val signInIntent = gsc.signInIntent
        startActivityForResult(signInIntent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    if (account != null) {
                        val idToken = account.idToken
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
                    Log.e("ERROR", e.toString())
                    e.printStackTrace()
                    Toast.makeText(applicationContext, "Đã có lỗi xảy ra", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "Hãy chọn tài khoản Google", Toast.LENGTH_SHORT).show()
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

