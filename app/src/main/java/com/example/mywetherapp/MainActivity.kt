package com.example.mywetherapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.example.mywetherapp.screens.WeatherScreen
import com.google.firebase.messaging.FirebaseMessaging
import com.example.mywetherapp.ui.theme.MyWetherAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyWetherAppTheme {
                Surface {
                    WeatherScreen()
                }
            }
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM token failed", task.exception)
                return@addOnCompleteListener
            }

            val token = task.result
            Log.d(TAG, "FCM Token: $token")
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
