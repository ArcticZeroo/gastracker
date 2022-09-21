@file:OptIn(ExperimentalPermissionsApi::class)

package io.frozor.gastracker.ui.components.pages.setup

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import io.frozor.gastracker.constants.ProgressState
import io.frozor.gastracker.ui.components.progress.ProgressBubble

fun getProgressStateFromPermissions(state: MultiplePermissionsState): ProgressState {
    return if (state.allPermissionsGranted) {
        ProgressState.SUCCEEDED
    } else if (state.revokedPermissions.isNotEmpty()) {
        ProgressState.FAILED
    } else {
        ProgressState.NOT_STARTED
    }
}

@Composable
fun PermissionsView(requiredPermissionsState: MultiplePermissionsState) {
    ProgressBubble(status = getProgressStateFromPermissions(requiredPermissionsState))

    Text("We need to request some more permissions from you!")
    Text("")
    Text("Here's the permissions we still need:")
    for (missingPermission in requiredPermissionsState.revokedPermissions) {
        Text("- ${missingPermission.permission}")
    }
    Text("")

    Button(onClick = {
        requiredPermissionsState.launchMultiplePermissionRequest()
    }) {
        Text("Launch permissions window")
    }
}