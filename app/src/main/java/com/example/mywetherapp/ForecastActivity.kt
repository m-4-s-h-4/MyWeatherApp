package com.example.mywetherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mywetherapp.ui.theme.MyWetherAppTheme
import java.text.SimpleDateFormat
import java.util.Locale

class ForecastActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cityName = intent.getStringExtra("CITY_NAME") ?: ""
        setContent {
            val viewModel: WeatherViewModel = viewModel()
            viewModel.getForecast(cityName, "c72510a22fc769a119e1f602964d5835")
            ForecastScreen(viewModel.forecastResponse)
        }
    }
}


@Composable
fun ForecastScreen(forecastResponse: LiveData<ForecastResponse>) {
    val response = forecastResponse.observeAsState()
    val forecastData = response.value

    // Display forecast data
    if (forecastData != null) {
        Column {
            Text(text = "City: ${forecastData.city.name}")

            // Group forecasts by date
            val groupedForecasts = forecastData.list.groupBy { it.dt_txt.substringBefore(" ") }

            // Display only first forecast of each day
            groupedForecasts.forEach { (date, forecasts) ->
                val firstForecastOfDay = forecasts.first()

                // Convert timestamp to day of week
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dt = sdf.parse(date)
                val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(dt!!)

                // Convert temperature from Kelvin to Celsius
                val tempCelsius = firstForecastOfDay.main.temp - 273.15

                Text(text = "$dayOfWeek: ${"%.2f".format(tempCelsius)}°C")
            }
        }
    }
}

