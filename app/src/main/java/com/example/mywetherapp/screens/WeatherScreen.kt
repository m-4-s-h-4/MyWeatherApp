package com.example.mywetherapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mywetherapp.searchbarfunc.AutocompleteTextField
import com.example.mywetherapp.searchbarfunc.PreferencesHelper
import com.example.mywetherapp.screensections.WeatherSection
import com.example.mywetherapp.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen() {
    val viewModel: WeatherViewModel = viewModel()
    val context = LocalContext.current
    val prefsHelper = PreferencesHelper(context)

    LaunchedEffect(key1 = true) {
        viewModel.getWeather("Barcelona", "c72510a22fc769a119e1f602964d5835", isSearch = true)
    }

    var searchText by remember { mutableStateOf("") }

    fun onSearchButtonClick() {
        viewModel.getWeather(searchText, "c72510a22fc769a119e1f602964d5835", isSearch = true)
        prefsHelper.saveSearchQuery(searchText)
    }

    val searchHistory = prefsHelper.getSearchHistory()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            contentColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                AutocompleteTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    suggestions = searchHistory,
                    onSearchClick = { onSearchButtonClick() }
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
