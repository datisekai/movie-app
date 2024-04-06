package com.example.movieapp.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.movieapp.Helper
import com.example.movieapp.data.model.ClassToken
import com.example.movieapp.data.model.LoggedInUser
import com.example.movieapp.data.model.LoginDTO
import com.example.movieapp.data.model.UserDTO
import com.example.movieapp.service.ServiceBuilder
import com.example.movieapp.ui.login.LoginActivity
import java.io.IOException
import java.util.concurrent.Executors
import java.util.concurrent.Callable
import java.util.concurrent.Future
/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String, context: Context): Result<UserDTO> {
        var user = UserDTO()
        try {
            // TODO: handle loggedInUser authentication
            val executor = Executors.newSingleThreadExecutor()
            val userFuture: Future<UserDTO> = executor.submit(Callable<UserDTO> {

                var emailData = username
                var passwordData = password

                val login = LoginDTO(emailData, passwordData)
                val result = ServiceBuilder().apiService.login(login).execute()
                if (result.isSuccessful) {
                    val token: String = result.body().data.accessToken
                    user = result.body().data.user

                    ClassToken.MY_TOKEN = token
                    ClassToken.ID = user.id
                    ClassToken.EMAIL = user.email
                    ClassToken.FULLNAME = user.fullname
                    ClassToken.IS_ACTIVE = user.is_active
                    ClassToken.ROLES = user.roles

                    Helper.TokenManager.saveToken(
                        context,
                        token,
                        ClassToken.ID,
                        ClassToken.EMAIL,
                        ClassToken.FULLNAME,
                        ClassToken.IS_ACTIVE,
                        ClassToken.ROLES
                    )
                }
                user
            })

            val user = userFuture.get() // Chờ cho đến khi kết quả của future có sẵn
            if (user.id == 0 && user.email.isEmpty() && user.fullname.isEmpty() && !user.is_active) {
                return Result.Fail(user)
            }
            return Result.Success(user)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout(context: Context) {
        // TODO: revoke authentication
        Helper.TokenManager.clearToken(context)
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        // Kết thúc các hoạt động khác và đặt LoginActivity làm hoạt động gốc mới
        (context as Activity).finish()
    }
}