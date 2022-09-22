package io.frozor.gastracker.ui.components.pages.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.frozor.gastracker.api.bluetooth.le.SCAN_INTERVAL
import io.frozor.gastracker.constants.LoggingTag
import io.frozor.gastracker.constants.ProgressState
import io.frozor.gastracker.ui.components.pages.PageContainer
import io.frozor.gastracker.ui.components.progress.ProgressBubble
import io.frozor.gastracker.ui.state.AppState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

fun getProgressFromScanState(
    hasEverScanned: Boolean,
    isScanning: Boolean,
    isDeviceNearby: Boolean
): ProgressState {
    return if (isDeviceNearby) {
        ProgressState.SUCCEEDED
    } else if (isScanning) {
        ProgressState.IN_PROGRESS
    } else if (!hasEverScanned) {
        ProgressState.NOT_STARTED
    } else {
        ProgressState.FAILED
    }
}

@Composable
fun HomeDeviceScanView(appState: AppState) {
    val deviceId by appState.deviceId.observeAsState()
    var hasEverScanned by remember { mutableStateOf(false) }
    var isScanning by remember { mutableStateOf(false) }
    var isDeviceNearby by remember { mutableStateOf(false) }
    var nextScan by remember { mutableStateOf(0.seconds) }

    if (deviceId == null) {
        Text("Something is wrong, you shouldn't be here without a device id set up!")
        return
    }

    LaunchedEffect(Unit) {
        while (true) {
            if (nextScan.isPositive()) {
                nextScan = nextScan.minus(1.seconds)
            } else {
                val currentDeviceId = deviceId
                if (currentDeviceId != null) {
                    isScanning = true
                    Log.i(LoggingTag.App, "Searching for nearby device with id $currentDeviceId")
                    isDeviceNearby = appState.bluetoothLeManager.isDeviceNearby(currentDeviceId)
                    Log.i(LoggingTag.App, "Was found nearby? $isDeviceNearby")
                    isScanning = false
                    hasEverScanned = true
                }
                nextScan = SCAN_INTERVAL
            }
            delay(1.seconds)
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        if (isScanning) {
            Text("Scanning for your device...")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ProgressBubble(
                status = getProgressFromScanState(
                    hasEverScanned = hasEverScanned,
                    isScanning = isScanning,
                    isDeviceNearby = isDeviceNearby
                )
            )
            if (isDeviceNearby) {
                Text("Your device is nearby!")
            } else if (!isScanning) {
                Text("Your device was not found nearby.")
            }
        }

        if (hasEverScanned && !isScanning) {
            Text("Next scan in ${nextScan.inWholeSeconds} second(s)")
        }
    }
}