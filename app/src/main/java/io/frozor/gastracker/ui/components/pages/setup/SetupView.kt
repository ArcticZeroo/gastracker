@file:OptIn(ExperimentalPermissionsApi::class)

package io.frozor.gastracker.ui.components.pages.setup

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import io.frozor.gastracker.constants.LoggingTag
import io.frozor.gastracker.constants.ProgressState
import io.frozor.gastracker.constants.Styles
import io.frozor.gastracker.constants.requiredForegroundPermissions
import io.frozor.gastracker.ui.components.pages.PageContainer
import io.frozor.gastracker.ui.components.progress.ProgressBubble

@Composable
fun SetupView() {
    Log.i(LoggingTag.App, "Rendering setup page")
    val requiredPermissionsState =
        rememberMultiplePermissionsState(permissions = requiredForegroundPermissions)

    PageContainer {
        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(Styles.DefaultPadding)) {
            Column {
                if (!requiredPermissionsState.allPermissionsGranted) {
                    Text("Step 1 of 2: Grant permissions", fontSize = 24.sp)
                    PermissionsView(requiredPermissionsState = requiredPermissionsState)
                } else {
                    Text("Step 2 of 2: Pair your device", fontSize = 24.sp)
                    BluetoothView()
                }
            }
        }
    }
}