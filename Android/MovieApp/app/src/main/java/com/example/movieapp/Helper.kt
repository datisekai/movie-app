package com.example.movieapp

import android.content.Context

class Helper {

    object TokenManager {
        private const val TOKEN_PREFS_NAME = "TokenPrefs"
        private const val KEY_TOKEN = "token"

        fun saveToken(context: Context, token: String) {
            val sharedPreferences = context.getSharedPreferences(TOKEN_PREFS_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(KEY_TOKEN, token)
            editor.apply()
        }

        fun getToken(context: Context): String? {
            val sharedPreferences = context.getSharedPreferences(TOKEN_PREFS_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(KEY_TOKEN, null)
        }

        fun clearToken(context: Context) {
            val sharedPreferences = context.getSharedPreferences(TOKEN_PREFS_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.remove(KEY_TOKEN)
            editor.apply()
        }
    }
}