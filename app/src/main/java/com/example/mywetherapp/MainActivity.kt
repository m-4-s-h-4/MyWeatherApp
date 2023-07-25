package com.example.mywetherapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen() {
    val viewModel: WeatherViewModel = viewModel()
    var locationInput by remember { mutableStateOf("") }

    fun onSearchButtonClick() {
        viewModel.getWeather(locationInput, "c72510a22fc769a119e1f602964d5835", isSearch = true)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            color = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.secondary,
            shape = RoundedCornerShape(40.dp),
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = locationInput,
                    onValueChange = { locationInput = it },
                    label = { Text(text = "Enter a location", color = MaterialTheme.colorScheme.secondary) },
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.secondary)
                )

                Button(
                    onClick = { onSearchButtonClick() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text(text = "Search", color = MaterialTheme.colorScheme.surface)
                }
            }
        }

        WeatherSection(
            viewModel.weatherResponseSearch,
            "Search Result",
            viewModel.isLoading.value ?: false,
            viewModel.error
        )
    }
}


@Composable
fun WithCurrentContext(content: @Composable (context: Context) -> Unit) {
    val context = LocalContext.current
    content(context)
}

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
