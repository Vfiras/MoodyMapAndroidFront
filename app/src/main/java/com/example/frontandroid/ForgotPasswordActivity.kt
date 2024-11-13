package com.example.frontandroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontandroid.Services.AuthServiceImpl
import com.example.frontandroid.ui.theme.CustomTypography

class ForgotPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ForgotPasswordScreen() }
    }
}

@Composable
fun ForgotPasswordScreen() {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val authService = AuthServiceImpl(context = LocalContext.current)

    fun handleForgotPassword() {
        if (email.text.isEmpty()) {
            errorMessage = "Email cannot be empty"
            return
        }

        isLoading = true
        errorMessage = ""
        successMessage = ""

        authService.forgotPassword(
            email = email.text,
            onSuccess = {
                isLoading = false
                successMessage = "Password reset code sent to your email."
                val intent = Intent(context, ResetPasswordActivity::class.java)
                intent.putExtra("email", email.text)
                context.startActivity(intent)
            },
            onError = { error ->
                isLoading = false
                errorMessage = error
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
    ) {
        // Top-left circles
        Box(
            modifier = Modifier
                .offset(x = 26.dp, y = (-90).dp)
                .size(180.dp)
                .background(colorResource(id = R.color.buttonColor).copy(alpha = 0.4f), CircleShape)
        )
        Box(
            modifier = Modifier
                .offset(x = (-60).dp, y = (-50).dp)
                .size(180.dp)
                .background(colorResource(id = R.color.buttonColor).copy(alpha = 0.4f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Forgot Password",
                color = colorResource(id = R.color.black),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = CustomTypography.bodyLarge.fontFamily
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Enter your email to reset your password.",
                color = colorResource(id = R.color.black),
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = CustomTypography.bodyLarge.fontFamily
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Enter your email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(color = colorResource(id = R.color.textColorPrimary), shape = RoundedCornerShape(12.dp)),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { handleForgotPassword() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.buttonColor),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(text = "Send Reset Code", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
            }
            if (successMessage.isNotEmpty()) {
                Text(text = successMessage, color = Color.Green, fontSize = 14.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewForgotPasswordScreen() {
    ForgotPasswordScreen()
}
