package com.example.frontandroid.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontandroid.Services.AuthServiceImpl
import kotlinx.coroutines.launch

class AuthViewModel(private val authService: AuthServiceImpl) : ViewModel() {

    fun changePassword(
        oldPassword: String,
        newPassword: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            authService.changePassword(oldPassword, newPassword, onSuccess, onError)
        }
    }

    fun forgotPassword(
        email: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            authService.forgotPassword(email, onSuccess, onError)
        }
    }

    fun resetPassword(
        userId: String,
        newPassword: String,
        resetCode: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            authService.resetPassword(userId, newPassword, resetCode, onSuccess, onError)
        }
    }

}
