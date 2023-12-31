package com.example.mywetherapp.api

import com.example.mywetherapp.data.ForecastResponse
import com.example.mywetherapp.data.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ): WeatherResponse

    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ): ForecastResponse
}

