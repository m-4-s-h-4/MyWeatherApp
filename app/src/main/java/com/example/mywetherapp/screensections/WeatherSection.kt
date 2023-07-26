package com.example.mywetherapp.screensections

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import com.example.mywetherapp.ForecastActivity
import com.example.mywetherapp.R
import com.example.mywetherapp.data.WeatherResponse

@Composable
fun WeatherSection(
    weatherResponse: LiveData<WeatherResponse>?,
    city: String,
    isLoading: Boolean,
    error: LiveData<String>?
) {
    val response = weatherResponse?.observeAsState()
    val weatherData = response?.value
    val errorMessage = error?.observeAsState()?.value

    val cityStyle = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
    val tempStyle = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurface)
    val buttonStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.surface)

    when {
        isLoading -> {
            Text("Fetching $city weather data...")
        }
        !errorMessage.isNullOrEmpty() -> {
            Text(errorMessage)
        }
        weatherData != null -> {
            // Convert temperature from Kelvin to Celsius
            val tempCelsius = weatherData.main.temp - 273.15

            val imageResource = when {
                tempCelsius < 15 -> R.drawable.cold
                tempCelsius < 24 -> R.drawable.warm
                else -> R.drawable.hot
            }
            Spacer(modifier = Modifier.height(15.dp))
            Text("City: ${if (city == "Search Result") weatherData.name else city}", style = cityStyle)
            Text("${"%.2f".format(tempCelsius)}°C", style = tempStyle)
            Image(painter = painterResource(id = imageResource), contentDescription = "weather drawing")

            WithCurrentContext { context ->
                Button(
                    onClick = {
                        val intent = Intent(context, ForecastActivity::class.java)
                        intent.putExtra("CITY_NAME", if (city == "Search Result") weatherData.name else city)
                        context.startActivity(intent)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text(text = "See Forecast", style = buttonStyle)
                }
            }
        }
    }
}
