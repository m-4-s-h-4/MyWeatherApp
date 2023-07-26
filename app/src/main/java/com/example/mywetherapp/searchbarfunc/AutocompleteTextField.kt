package com.example.mywetherapp.searchbarfunc

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.mywetherapp.R

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
