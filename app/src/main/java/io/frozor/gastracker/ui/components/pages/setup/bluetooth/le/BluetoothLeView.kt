package io.frozor.gastracker.ui.components.pages.setup.bluetooth.le

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun BluetoothLeView() {
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Let's find your device!")
        Text("Please choose your beacon from the list below.")
        BluetoothLeDeviceScanner(context)
    }
}