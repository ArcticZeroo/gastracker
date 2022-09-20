package io.frozor.gastracker.ui.components.progress

import androidx.compose.animation.core.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun LoadingSpinner() {
    // Indeterminate progress
    CircularProgressIndicator()
}