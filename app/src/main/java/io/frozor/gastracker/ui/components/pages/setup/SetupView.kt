@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalPermissionsApi::class)

package io.frozor.gastracker.ui.components.pages.setup

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import io.frozor.gastracker.constants.LoggingTag
import io.frozor.gastracker.constants.Styles
import io.frozor.gastracker.constants.requiredForegroundPermissions
import io.frozor.gastracker.ui.components.pages.PageContainer
import io.frozor.gastracker.ui.components.pages.setup.bluetooth.le.BluetoothLeView
import io.frozor.gastracker.ui.state.AppState

@Composable
fun SetupView(appState: AppState) {
    Log.i(LoggingTag.App, "Rendering setup page")
    val requiredPermissionsState =
        rememberMultiplePermissionsState(permissions = requiredForegroundPermissions)

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        appState.retrieveDeviceId(context)
    }

    val hasDeviceIdBeenFetched by appState.hasDeviceIdBeenFetched.observeAsState()
    val deviceId by appState.deviceId.observeAsState()

    PageContainer {
        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(Styles.DefaultPadding)) {
            Column {
                if (!requiredPermissionsState.allPermissionsGranted) {
                    Text("Step 1 of 2: Grant permissions", fontSize = 24.sp)
                    PermissionsView(requiredPermissionsState = requiredPermissionsState)
                } else if (hasDeviceIdBeenFetched == false) {
                    Text("Checking if you've already set up a device...")
                } else if (deviceId == null) {
                    Text("Step 2 of 2: Choose your device", fontSize = 24.sp)
                    BluetoothLeView(appState)
                } else {
                    Text("That's everything! You have selected device $deviceId")
                }
            }
        }
    }
}