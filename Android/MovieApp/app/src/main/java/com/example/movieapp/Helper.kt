package com.example.movieapp

import android.content.Context
import com.example.movieapp.data.model.UserDTO

class Helper {

    object TokenManager {
        private const val TOKEN_PREFS_NAME = "TokenPrefs"
        private const val KEY_TOKEN = "token"
        private const val KEY_ID = "user"
        private const val KEY_EMAIL = "email"
        private const val KEY_FULLNAME = "fullname"
        private const val KEY_IS_ACTIVE = "is_active"
        private const val KEY_ROLES ="roles"
        fun saveToken(context: Context, token: String, id: Int, email: String, fullname: String, isActive:Boolean,roles:ArrayList<String>) {
            val sharedPreferences = context.getSharedPreferences(TOKEN_PREFS_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(KEY_TOKEN, token)
            editor.putInt(KEY_ID,id)
            editor.putString(KEY_EMAIL,email)
            editor.putString(KEY_FULLNAME,fullname)
            editor.putBoolean(KEY_IS_ACTIVE,isActive)
            editor.putStringSet(KEY_ROLES,roles.toSet())
            editor.apply()
        }

        fun getToken(context: Context): String? {
            val sharedPreferences = context.getSharedPreferences(TOKEN_PREFS_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(KEY_TOKEN, null)
        }
        fun getId(context: Context): Int? {
            val sharedPreferences = context.getSharedPreferences(TOKEN_PREFS_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getInt(KEY_ID, 0)
        }
        fun getEmail(context: Context): String? {
            val sharedPreferences = context.getSharedPreferences(TOKEN_PREFS_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(KEY_EMAIL, null)
        }
        fun getFullName(context: Context): String? {
            val sharedPreferences = context.getSharedPreferences(TOKEN_PREFS_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(KEY_FULLNAME, null)
        }
        fun getIsActive(context: Context): Boolean? {
            val sharedPreferences = context.getSharedPreferences(TOKEN_PREFS_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(KEY_IS_ACTIVE, false)
        }

        fun getRoles(context: Context) : ArrayList<String>?{
            val sharedPreferences = context.getSharedPreferences(KEY_ROLES,Context.MODE_PRIVATE)
            val tmp = sharedPreferences.getStringSet(KEY_ROLES,null)
            val result = ArrayList(tmp)
            return result
        }


        fun clearToken(context: Context) {
            val sharedPreferences = context.getSharedPreferences(TOKEN_PREFS_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.remove(KEY_TOKEN)
            editor.remove(KEY_ID)
            editor.remove(KEY_EMAIL)
            editor.remove(KEY_FULLNAME)
            editor.remove(KEY_IS_ACTIVE)
            editor.remove(KEY_ROLES)
            editor.apply()
        }
    }
}