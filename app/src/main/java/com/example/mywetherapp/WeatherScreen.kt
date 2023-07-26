package com.example.mywetherapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel

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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AutocompleteTextField(
    value: String,
    onValueChange: (String) -> Unit,
    suggestions: List<String>,
    onSearchClick: () -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val filteredSuggestions = suggestions.filter { it.contains(value, ignoreCase = true) }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            TextField(
                value = value,
                onValueChange = { newValue ->
                    onValueChange(newValue)
                    isExpanded = newValue.isNotEmpty() && filteredSuggestions.isNotEmpty()
                },
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                    isExpanded = false
                }),
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .height(IntrinsicSize.Min),
//                textColor = Color.Red
            )
            Button(
                onClick = onSearchClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier
                    .height(IntrinsicSize.Min),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_search_24),
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.size(35.dp)
                )
            }
            Box(modifier = Modifier.padding(top = 55.dp)) {
                if (isExpanded) {
                    Popup(
                        alignment = Alignment.TopStart,
                        onDismissRequest = {},
                    ) {
                        Surface(
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Column(
                                modifier = Modifier.padding(top = 10.dp)
                            ) {
                                filteredSuggestions.forEach { suggestion ->
                                    DropdownMenuItem(onClick = {
                                        onValueChange(suggestion)
                                        isExpanded = false
                                    }) {
                                        Text(text = suggestion, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
