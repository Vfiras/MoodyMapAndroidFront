package com.example.frontandroid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontandroid.models.SignupData
import com.example.frontandroid.remote.RetrofitClient
import com.example.frontandroid.ui.theme.CustomTypography
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.util.Log
import android.content.Intent


class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignUpScreen(
                navigateToLogin = {
                    // Navigate to LoginActivity when signup is successful
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()  // Optional: close SignUpActivity so it's removed from the back stack
                },
            )
        }
    }
}

@Composable
fun SignUpScreen(
    navigateToLogin: () -> Unit = {},  // Callback for navigation
) {
    val nameState = remember { mutableStateOf(TextFieldValue("")) }
    val emailState = remember { mutableStateOf(TextFieldValue("")) }
    val passwordState = remember { mutableStateOf(TextFieldValue("")) }

    // Using RetrofitClient.instance as AuthService
    val authService = RetrofitClient.instance

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

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome On Board !",
                color = colorResource(id = R.color.black),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,  // Semi-Bold font weight
                fontFamily = CustomTypography.bodyLarge.fontFamily // Use CustomTypography instead of Typography
            )



            // Add margin between elements like the XML layout
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Let’s help you meet up your tasks.",
                color = colorResource(id = R.color.black),
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,  // Semi-Bold font weight
                fontFamily = CustomTypography.bodyLarge.fontFamily // Use CustomTypography instead of Typography
            )

            Spacer(modifier = Modifier.height(24.dp))


            // Full Name input
            OutlinedTextField(
                value = nameState.value,
                onValueChange = { nameState.value = it },
                label = { Text("Enter your Full Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(color = colorResource(id = R.color.textColorPrimary), shape = RoundedCornerShape(12.dp)),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email input
            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text("Enter your E-mail") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(color = colorResource(id = R.color.textColorPrimary), shape = RoundedCornerShape(12.dp)),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password input
            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = { Text("Enter password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(color = colorResource(id = R.color.textColorPrimary), shape = RoundedCornerShape(12.dp)),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))



            Spacer(modifier = Modifier.height(50.dp))



// The Sign Up Button
            Button(
                onClick = {
                    // Debugging print statement to check if the button is clicked
                    Log.d("SignUp", "Sign Up button clicked!")

                    // Ensure all fields are filled
                    if (nameState.value.text.isNotEmpty() && emailState.value.text.isNotEmpty() && passwordState.value.text.isNotEmpty()) {
                        val signupData = SignupData(
                            name = nameState.value.text,
                            email = emailState.value.text,
                            password = passwordState.value.text
                        )

                        // Assuming authService is correctly initialized
                        authService.signUp(signupData).enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.isSuccessful) {

                                    Log.d("SignUp", "Success: ${response.message()}")
                                    // Navigate to the login screen (make sure navigateToLogin() is defined)
                                    navigateToLogin()
                                } else {
                                    Log.d("SignUp", "Failed: ${response.message()}")
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Log.e("SignUp", "Error: ${t.message}", t)
                            }
                        })
                    } else {
                        Log.d("SignUp", "Missing fields")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(top = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.buttonColor),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "Sign Up", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "Already have an account?",
                color = colorResource(id = R.color.black),
                fontSize = 14.sp,
                fontFamily = CustomTypography.bodyLarge.fontFamily,
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "Log in",
                color = colorResource(id = R.color.buttonColor),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = CustomTypography.bodyLarge.fontFamily,
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(-25.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignUpScreen() {
    SignUpScreen()
}
