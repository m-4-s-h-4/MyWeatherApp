package com.example.mywetherapp

object Temperature {
    fun kelvinToCelsius(kelvin: Double): Double {
        return kelvin - 273.15
    }
}