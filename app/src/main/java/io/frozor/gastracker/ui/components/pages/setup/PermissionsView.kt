@file:OptIn(ExperimentalPermissionsApi::class)

package io.frozor.gastracker.ui.components.pages.setup

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import io.frozor.gastracker.constants.ProgressState
import io.frozor.gastracker.ui.components.progress.ProgressBubble
import io.frozor.gastracker.util.permissionFriendlyNames

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
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProgressBubble(status = getProgressStateFromPermissions(requiredPermissionsState))
            Text("We need to request some more permissions from you!")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Here's the permissions we still need:")
        for (missingPermission in requiredPermissionsState.revokedPermissions) {
            Text(
                "- ${
                    permissionFriendlyNames.getOrDefault(
                        missingPermission.permission,
                        missingPermission.permission
                    )
                }"
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            requiredPermissionsState.launchMultiplePermissionRequest()
        }) {
            Text("Launch permissions window")
        }
    }
}