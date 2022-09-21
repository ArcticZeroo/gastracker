package io.frozor.gastracker.ui.components.pages.setup

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@SuppressLint("MissingPermission")
@Composable
fun BluetoothDeviceList(
    showOnlyNamedDevices: Boolean,
    foundDevices: List<BluetoothDevice>,
    onDeviceSelected: (BluetoothDevice) -> Unit
) {
    var devicesToShow by remember { mutableStateOf(listOf<BluetoothDevice>()) }

    LaunchedEffect(showOnlyNamedDevices, foundDevices) {
        devicesToShow = if (showOnlyNamedDevices) {
            foundDevices.filter { device -> device.name != null }.sortedBy { device -> device.name }
        } else {
            foundDevices
        }
    }

    LazyColumn {
        items(devicesToShow) { bluetoothDevice ->
            val displayName = bluetoothDevice.name
                ?: bluetoothDevice.alias
                ?: bluetoothDevice.address
                ?: "Unknown device"

            Text(
                displayName,
                modifier = Modifier
                    .clickable {
                        onDeviceSelected(bluetoothDevice)
                    }
                    .padding(16.dp)
                    .fillMaxWidth()

            )
        }
    }
}