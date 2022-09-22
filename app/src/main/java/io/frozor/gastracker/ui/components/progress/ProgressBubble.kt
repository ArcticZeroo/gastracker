package io.frozor.gastracker.ui.components.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.frozor.gastracker.constants.ProgressState
import io.frozor.gastracker.ui.theme.md_green_400

val BUBBLE_SIZE = 24.dp
val PROGRESS_STROKE_WIDTH = BUBBLE_SIZE / 8

@Composable
fun ProgressBubble(status: ProgressState) {
    val backgroundColor = when (status) {
        ProgressState.FAILED -> Color.Red
        ProgressState.NOT_STARTED -> Color.LightGray
        ProgressState.IN_PROGRESS -> Color.LightGray
        ProgressState.SUCCEEDED -> md_green_400
    }

    Box(
        modifier = Modifier
            .size(BUBBLE_SIZE)
            .clip(CircleShape)
            .background(backgroundColor)
    ) {
        when (status) {
            ProgressState.FAILED -> {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Action failed",
                    modifier = Modifier.fillMaxSize()
                )
            }
            ProgressState.NOT_STARTED -> {
                Icon(Icons.Outlined.AddCircle,
                    contentDescription = "Action not started",
                    modifier = Modifier.fillMaxSize())
            }
            ProgressState.IN_PROGRESS -> {
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxSize(),
                    strokeWidth = PROGRESS_STROKE_WIDTH
                )
            }
            ProgressState.SUCCEEDED -> {
                Icon(
                    Icons.Filled.Done,
                    contentDescription = "Action succeeded",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}