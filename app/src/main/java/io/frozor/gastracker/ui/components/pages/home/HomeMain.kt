package io.frozor.gastracker.ui.components.pages.home

import android.util.Log
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import io.frozor.gastracker.constants.LoggingTag

@Composable
fun HomeMain() {
    Log.i(LoggingTag.App, "Rendering home")
    Text(text = "I am home")
}