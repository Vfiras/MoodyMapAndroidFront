package com.example.frontandroid.Services

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        sharedPreferences.edit().putString("AUTH_TOKEN", token).apply()
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString("AUTH_TOKEN", null)
    }
}
