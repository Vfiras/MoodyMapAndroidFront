package com.example.frontandroid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.frontandroid.Services.SharedPreferencesHelper
import com.example.frontandroid.ui.theme.FrontAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FrontAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    // Function to clear the token and start the LoginActivity
    fun logout() {
        val sharedPreferencesHelper = SharedPreferencesHelper(this)
        sharedPreferencesHelper.saveAuthToken("") // Clear token

        Log.d("Log out", "Logout success")

        // Start LoginActivity as a new task and clear previous activity stack
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)

        // Finish MainActivity to ensure it is removed from the back stack
        finish()
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val activity = context as? MainActivity

    Column(modifier = modifier) {
        Text(
            text = "Hello $name!"
        )

        // Logout button
        Button(
            onClick = {
                activity?.logout() // Trigger logout in MainActivity
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Logout")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FrontAndroidTheme {
        Greeting("Android")
    }
}
