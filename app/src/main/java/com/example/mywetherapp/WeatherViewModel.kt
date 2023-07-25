package com.example.mywetherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}

