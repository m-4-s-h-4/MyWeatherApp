package com.example.mywetherapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val _weatherResponseSearch = MutableLiveData<WeatherResponse>()
    val weatherResponseSearch: LiveData<WeatherResponse> get() = _weatherResponseSearch

    private val _forecastResponse = MutableLiveData<ForecastResponse>()
    val forecastResponse: LiveData<ForecastResponse> get() = _forecastResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun getWeather(city: String, apiKey: String, isSearch: Boolean = false) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val response = RetrofitInstance.weatherApi.getWeather(city, apiKey)
                _weatherResponseSearch.value = response
            } catch(e: Exception) {
                _error.value = "No such location found."
            }

            _isLoading.value = false
        }
    }

    fun getForecast(city: String, apiKey: String) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val response = RetrofitInstance.weatherApi.getForecast(city, apiKey)
                _forecastResponse.value = response
            } catch(e: Exception) {
                _error.value = "No such location found."
            }

            _isLoading.value = false
        }
    }

    fun setErrorMessage(message: String) {
        _error.value = message
    }
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
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
            color = Color.Gray,
            contentColor = Color.White,
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
                    label = { Text(text = "Enter a location", color = Color.Gray) },
                    textStyle = TextStyle(color = Color.Black)
                )

                Button(
                    onClick = { onSearchButtonClick() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray,
                        contentColor = Color.Black)
                ) {
                    Text(text = "Search", color = Color.White)
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

    when {
        isLoading -> {
            Text("Fetching $city weather data...")
        }
        !errorMessage.isNullOrEmpty() -> {
            Text(errorMessage)
        }
        weatherData != null -> {
            Text("City: ${if (city == "Search Result") weatherData.name else city}")
            Text("Temperature: ${String.format("%.2f", weatherData.main.temp - 273.15)} °C")  // Convert temperature to Celsius here

            WithCurrentContext { context ->
                Button(onClick = {
                    val intent = Intent(context, ForecastActivity::class.java)
                    intent.putExtra("CITY_NAME", if (city == "Search Result") weatherData.name else city)
                    context.startActivity(intent)
                }) {
                    Text(text = "See Forecast")
                }
            }
        }
    }
}
