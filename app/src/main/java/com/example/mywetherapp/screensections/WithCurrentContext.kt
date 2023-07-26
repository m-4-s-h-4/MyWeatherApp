package com.example.mywetherapp.screensections

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun WithCurrentContext(content: @Composable (context: Context) -> Unit) {
    val context = LocalContext.current
    content(context)
}