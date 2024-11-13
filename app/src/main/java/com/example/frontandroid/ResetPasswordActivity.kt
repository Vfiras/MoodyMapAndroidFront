package com.example.frontandroid

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontandroid.Services.AuthServiceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// ResetPasswordActivity
class ResetPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve email from intent
        val email = intent.getStringExtra("email") ?: ""

        setContent {
            ResetPasswordScreen(email = email)
        }
    }
}

@Composable
fun ResetPasswordScreen(email: String) {
    var userId by remember { mutableStateOf<String?>(null) }
    var resetCode by remember { mutableStateOf(TextFieldValue("")) }
    var newPassword by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val authService = AuthServiceImpl(context)

    // Fetch userId when the screen loads
    LaunchedEffect(email) {
        if (email.isNotEmpty()) {
            authService.getUserIdByEmail(
                email = email,
                onSuccess = { fetchedUserId ->
                    userId = fetchedUserId
                    Log.d("ResetPassword", "User ID fetched: $userId")
                },
                onError = { error ->
                    errorMessage = "Error fetching user ID: $error"
                    Log.e("ResetPassword", "Error fetching user ID: $error")
                }
            )
        } else {
            errorMessage = "Email cannot be empty."
            Log.e("ResetPassword", "Email is empty")
        }
    }

    // Handle the reset password logic
    fun handleResetPassword() {
        if (userId == null) {
            errorMessage = "User ID not found."
            return
        }
        if (resetCode.text.isEmpty() || newPassword.text.isEmpty()) {
            errorMessage = "All fields are required."
            return
        }

        isLoading = true
        errorMessage = ""
        successMessage = ""

        authService.resetPassword(
            userId = userId!!,
            resetCode = resetCode.text,
            newPassword = newPassword.text,
            onSuccess = {
                isLoading = false
                successMessage = "Password has been successfully reset."
            },
            onError = { error ->
                isLoading = false
                errorMessage = error
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Reset Password",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = resetCode,
            onValueChange = { resetCode = it },
            label = { Text("Enter reset code") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("Enter new password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { handleResetPassword() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Reset Password")
            }
        }

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
        }
        if (successMessage.isNotEmpty()) {
            Text(text = successMessage, color = Color.Green, fontSize = 14.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewResetPasswordScreen() {
    ResetPasswordScreen(email = "test@example.com")
}
