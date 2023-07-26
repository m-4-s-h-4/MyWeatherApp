package com.example.mywetherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mywetherapp.screens.ForecastScreen
import com.example.mywetherapp.ui.theme.MyWetherAppTheme
import com.example.mywetherapp.viewmodel.WeatherViewModel

class ForecastActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cityName = intent.getStringExtra("CITY_NAME") ?: ""
        setContent {
            MyWetherAppTheme {
                val viewModel: WeatherViewModel = viewModel()
                viewModel.getForecast(cityName, "c72510a22fc769a119e1f602964d5835")
                ForecastScreen(viewModel.forecastResponse)
            }
        }
    }
}
