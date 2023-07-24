package com.example.mywetherapp

data class ForecastResponse(
    val city: City,
    val list: List<Forecast>
)

data class City(
    val name: String
)

data class Forecast(
    val dt_txt: String,
    val main: Main
)


