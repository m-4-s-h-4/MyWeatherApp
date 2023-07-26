package com.example.mywetherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
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
    }
}
