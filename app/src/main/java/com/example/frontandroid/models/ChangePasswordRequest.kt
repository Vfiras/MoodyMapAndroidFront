package com.example.frontandroid.models

data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)