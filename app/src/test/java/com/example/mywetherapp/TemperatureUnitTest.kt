package com.example.mywetherapp

import org.junit.Assert
import org.junit.Test

//added suggested tests
class TemperatureUnitTest {

    //successful test
    @Test
    fun testKelvinToCelsius_Successful() {
        val kelvin = 303.15
        val expectedCelsius = 30.00
        val actualCelsius = Temperature.kelvinToCelsius(kelvin)
        Assert.assertEquals(expectedCelsius, actualCelsius, 0.01)
    }

    //unsuccessful test
    @Test
    fun testKelvinToCelsius_Unsuccessful() {
        val kelvin = 303.15
        val expectedCelsius = 15.00
        val actualCelsius = Temperature.kelvinToCelsius(kelvin)
        Assert.assertEquals(expectedCelsius, actualCelsius, 0.01)
    }
}