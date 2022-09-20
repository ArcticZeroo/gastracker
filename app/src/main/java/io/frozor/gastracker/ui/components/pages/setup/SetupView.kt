@file:OptIn(ExperimentalPermissionsApi::class)

package io.frozor.gastracker.ui.components.pages.setup

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import io.frozor.gastracker.constants.LoggingTag
import io.frozor.gastracker.constants.ProgressState
import io.frozor.gastracker.constants.Styles
import io.frozor.gastracker.constants.requiredForegroundPermissions
import io.frozor.gastracker.ui.components.pages.PageContainer
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
fun SetupView() {
    val requiredPermissionsState =
        rememberMultiplePermissionsState(permissions = requiredForegroundPermissions)

    PageContainer {
        Card(modifier = Modifier.padding(Styles.DefaultPadding)) {
            Column {
                ProgressBubble(status = getProgressStateFromPermissions(requiredPermissionsState))
                if (!requiredPermissionsState.allPermissionsGranted) {
                    Log.i(LoggingTag.App, "Are all permissions granted: ${requiredPermissionsState.allPermissionsGranted}, revoked count: ${requiredPermissionsState.revokedPermissions.map { it.permission }}")
                    Text("We need to request some more permissions from you!")
                    Button(onClick = {
                        requiredPermissionsState.launchMultiplePermissionRequest()
                    }) {
                        Text("Gimme permissions")
                    }
                }
            }
        }
    }
}