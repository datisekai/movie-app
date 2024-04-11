package com.example.movieapp.data

import android.content.Context
import android.util.Log
import com.example.movieapp.data.model.LoggedInUser
import com.example.movieapp.data.model.UserDTO

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: UserDTO? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout( context: Context) {
        user = null
        dataSource.logout(context)
    }

    fun login(username: String, password: String, context: Context, type: String, idToken: String): Result<UserDTO> {
        // handle login
        val result = dataSource.login(username, password, context, type, idToken)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedInUser(userDTO: UserDTO) {
        this.user = userDTO
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}