package io.frozor.gastracker.ui.components.pages.setup.bluetooth.le

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.frozor.gastracker.state.AppState

@Composable
fun BluetoothLeView(appState: AppState) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Let's find your device!")
        Text("Please choose your beacon from the list below.")
        BluetoothLeDeviceScanner(appState)
    }
}