package io.frozor.gastracker.ui.components.pages.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.frozor.gastracker.ui.components.pages.PageContainer
import io.frozor.gastracker.ui.state.AppState

@Composable
fun HomeView(appState: AppState) {
    val isBluetoothEnabled by appState.bluetoothLeManager.isEnabled.observeAsState()

    PageContainer {
        Card(modifier = Modifier.padding(16.dp)) {
            if (isBluetoothEnabled != true) {
                HomeBluetoothNotEnabledView()
            } else {
                HomeDeviceScanView(appState)
            }
        }
    }
}