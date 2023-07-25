package com.example.mywetherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mywetherapp.ui.theme.MyWetherAppTheme
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

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

@Composable
fun ForecastScreen(forecastResponse: LiveData<ForecastResponse>) {
    val response = forecastResponse.observeAsState()
    val forecastData = response.value

    // Define your custom styles
    val dayStyle = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
    val tempStyle = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurface)

    // Display forecast data
    if (forecastData != null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val groupedForecasts = forecastData.list.groupBy { it.dt_txt.substringBefore(" ") }

            groupedForecasts.forEach { (date, forecasts) ->
                val firstForecastOfDay = forecasts.first()

                // Convert timestamp to day of week
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dt = sdf.parse(date)
                val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(dt!!)

                // Convert temperature from Kelvin to Celsius
                val tempCelsius = firstForecastOfDay.main.temp - 273.15

                val imageResource = when {
                    tempCelsius < 15 -> R.drawable.cold
                    tempCelsius < 24 -> R.drawable.warm
                    else -> R.drawable.hot
                }

                Card(
                    modifier = Modifier
                        .padding(1.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Image(
                            painter = painterResource(id = imageResource),
                            contentDescription = "weather drawing",
                            modifier = Modifier.size(110.dp)
                        )
                        Spacer(modifier = Modifier.width(80.dp))
                        Column {
                            Text(text = dayOfWeek, style = dayStyle)
                            Text(text = "${"%.2f".format(tempCelsius)}°C", style = tempStyle)
                        }
                    }
                }
            }
        }
    }
}
