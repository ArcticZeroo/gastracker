package io.frozor.gastracker.ui.components.pages.setup.bluetooth.obd

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.frozor.gastracker.ui.components.pages.setup.bluetooth.common.BluetoothDeviceListItem

@SuppressLint("MissingPermission")
@Composable
fun BluetoothDeviceList(
    showOnlyNamedDevices: Boolean,
    pairedDevices: List<BluetoothDevice>,
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

    Column(modifier = Modifier.padding(16.dp)) {
        if (pairedDevices.isNotEmpty()) {
            Text("Paired Devices")

            LazyColumn {
                items(pairedDevices) { bluetoothDevice ->
                    BluetoothDeviceListItem(bluetoothDevice, onDeviceSelected)
                }
            }

            Divider()
        }

        Text("New Devices")
        LazyColumn {
            items(devicesToShow) { bluetoothDevice ->
                BluetoothDeviceListItem(bluetoothDevice, onDeviceSelected)
            }
        }
    }
}