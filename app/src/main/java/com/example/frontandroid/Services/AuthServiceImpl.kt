package com.example.frontandroid.Services


import android.content.Context
import com.example.frontandroid.models.LoginData
import com.example.frontandroid.models.SignupData
import com.example.frontandroid.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.frontandroid.models.ResetPasswordRequest
import com.example.frontandroid.models.ForgotPasswordRequest
import com.example.frontandroid.Services.SharedPreferencesHelper
import com.example.frontandroid.models.ChangePasswordRequest
import android.util.Log

class AuthServiceImpl(private val context: Context) {

    private val api = RetrofitClient.instance
    private val sharedPreferencesHelper = SharedPreferencesHelper(context)

    // Login function with SharedPreferences integration
    fun signIn(loginData: LoginData, rememberMe: Boolean, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        api.login(loginData).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    val token = response.headers()["Authorization"] ?: "No Token Found"

                    // Save token if "Remember me" is checked
                    if (rememberMe) {
                        sharedPreferencesHelper.saveAuthToken(token)
                    }
                    onSuccess(token)
                } else {
                    onError("Login failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onError("Error: ${t.message}")
            }
        })
    }

    // Signup function
    fun signUp(signupData: SignupData, onSuccess: () -> Unit, onError: (String) -> Unit) {
        api.signUp(signupData).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Signup failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onError("Error: ${t.message}")
            }
        })
    }



    fun changePassword(
        oldPassword: String,
        newPassword: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val request = ChangePasswordRequest(oldPassword, newPassword)
        api.changePassword(request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Failed to change password")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onError(t.message ?: "Unknown error")
            }
        })
    }

    fun forgotPassword(
        email: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val request = ForgotPasswordRequest(email)
        api.forgotPassword(request).enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(
                call: Call<Map<String, String>>,
                response: Response<Map<String, String>>
            ) {
                if (response.isSuccessful) {
                    val message = response.body()?.get("message") ?: "Success"
                    onSuccess(message)
                } else {
                    onError("Failed to send reset email")
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                onError(t.message ?: "Unknown error")
            }
        })
    }

    fun resetPassword(
        userId: String,
        newPassword: String,
        resetCode: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val request = ResetPasswordRequest(userId = userId, resetCode = resetCode, newPassword = newPassword)
        api.resetPassword(userId, request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Failed to reset password: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onError("Network error: ${t.message}")
            }
        })
    }

    fun getUserIdByEmail(email: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        api.getUserIdByEmail(email).enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    Log.d("AuthService", "Fetching user ID for email: $email")

                    val userId = response.body()?.get("userId") ?: ""
                    if (userId.isNotEmpty()) {
                        onSuccess(userId)
                    } else {
                        onError("User not found with provided email")
                    }
                } else {
                    onError("Failed to fetch user ID: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                onError("Network error: ${t.message}")
            }
        })
    }

}