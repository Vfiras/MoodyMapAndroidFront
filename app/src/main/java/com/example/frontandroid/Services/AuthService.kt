package com.example.frontandroid.Services

import com.example.frontandroid.models.ChangePasswordRequest
import com.example.frontandroid.models.ForgotPasswordRequest
import com.example.frontandroid.models.LoginData
import com.example.frontandroid.models.ResetPasswordRequest
import com.example.frontandroid.models.SignupData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface AuthService {

    @POST("auth/signup")
    fun signUp(@Body signupData: SignupData): Call<Void>

    @POST("auth/login")
    fun login(@Body loginData: LoginData): Call<Void>

    @PUT("auth/change-password")
    fun changePassword(@Body request: ChangePasswordRequest): Call<Void>

    @POST("auth/forgot-password")
    fun forgotPassword(@Body request: ForgotPasswordRequest): Call<Map<String, String>>

    @POST("auth/reset-password/{userId}")
    fun resetPassword(
        @Path("userId") userId: String,
        @Body resetPasswordRequest: ResetPasswordRequest
    ): Call<Void>
    @GET("auth/get-user-id")
    fun getUserIdByEmail(@Query("email") email: String): Call<Map<String, String>>
}

