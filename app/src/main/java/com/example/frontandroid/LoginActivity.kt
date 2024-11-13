package com.example.frontandroid

import com.example.frontandroid.Services.SharedPreferencesHelper
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontandroid.models.LoginData
import com.example.frontandroid.Services.AuthServiceImpl
import com.example.frontandroid.ui.theme.CustomTypography

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferencesHelper = SharedPreferencesHelper(this)
        val savedToken = sharedPreferencesHelper.getAuthToken()

        if (savedToken != null) {
            // If the user is already logged in, navigate to the main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // Otherwise, set the composable content for the login screen
            setContent {
                LoginScreen()
            }
        }
    }
}

@Composable
fun LoginScreen() {
    // State for email and password fields
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }
    val context = LocalContext.current  // Access context inside composable


    val authService = AuthServiceImpl(context = LocalContext.current)

    // Function to handle login
    fun handleLogin() {
        if (email.text.isEmpty() || password.text.isEmpty()) {
            errorMessage = "Email and password cannot be empty"
            Log.e("Login", "Error: Email or Password is empty.")
            return
        }

        val loginData = LoginData(email = email.text, password = password.text)
        isLoading = true

        authService.signIn(loginData, rememberMe, onSuccess = { token ->
            isLoading = false
            errorMessage = "Login successful, token: $token"
            Log.d("Login", "Login successful. Token: $token")
        }, onError = { error ->
            isLoading = false
            errorMessage = error
            Log.e("Login", "Error: $error")
        })
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
    ) {
        // Top-left circles (same as original)
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

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome back!",
                color = colorResource(id = R.color.black),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = CustomTypography.bodyLarge.fontFamily
            )

            Spacer(modifier = Modifier.height(40.dp))

            Image(
                painter = painterResource(id = R.drawable.mini_logo),
                contentDescription = "Logo",
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Email and Password fields
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Enter your E-mail") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(color = colorResource(id = R.color.textColorPrimary), shape = RoundedCornerShape(12.dp)),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Enter password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(color = colorResource(id = R.color.textColorPrimary), shape = RoundedCornerShape(12.dp)),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Display error message if any
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Forgot password text, now directly using LocalContext within clickable
            Text(
                text = "Forgot password?",
                color = colorResource(id = R.color.black),
                fontSize = 14.sp,
                fontFamily = CustomTypography.bodyLarge.fontFamily,
                modifier = Modifier
                    .clickable {
                        val intent = Intent(context, ForgotPasswordActivity::class.java)
                        context.startActivity(intent)
                    }
            )

            Spacer(modifier = Modifier.height(16.dp))
            // Remember Me Checkbox
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Remember me")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Login button
            Button(
                onClick = { handleLogin() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.buttonColor),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Log in",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = CustomTypography.bodyLarge.fontFamily,
                    color = colorResource(id = R.color.textColorPrimary)
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Loading Spinner
            if (isLoading) {
                CircularProgressIndicator(color = colorResource(id = R.color.buttonColor))
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Social Login buttons
            Text(text = "Or log in with", color = colorResource(id = R.color.black), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(16.dp))

            // Google button
            SocialLoginButton(
                iconId = R.drawable.google,
                text = "Login with Google",
                buttonColor = Color(0xFFDB4437)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Facebook button
            SocialLoginButton(
                iconId = R.drawable.facebooklogo,
                text = "Login with Facebook",
                buttonColor = Color(0xFF4267B2)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // LinkedIn button
            SocialLoginButton(
                iconId = R.drawable.linkedinlogoo,
                text = "Login with LinkedIn",
                buttonColor = Color(0xFF2867B2)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Don't have an account?",
                color = colorResource(id = R.color.black),
                fontSize = 14.sp,
                fontFamily = CustomTypography.bodyLarge.fontFamily
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Sign up",
                color = colorResource(id = R.color.buttonColor),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = CustomTypography.bodyLarge.fontFamily,
                modifier = Modifier
                    .clickable {
                        val intent = Intent(context, SignUpActivity::class.java)
                        context.startActivity(intent)
                    }
            )
        }
    }
}



// Composable function for Social Login Button
@Composable
fun SocialLoginButton(iconId: Int, text: String, buttonColor: Color) {
    Button(
        onClick = {},
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(50.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen()
}
