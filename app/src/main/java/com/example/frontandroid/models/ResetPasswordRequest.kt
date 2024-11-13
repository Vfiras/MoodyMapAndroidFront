package com.example.frontandroid.models

data class ResetPasswordRequest(
    val userId: String,
    val resetCode: String,
    val newPassword: String
)
