package io.frozor.gastracker.ui.components.pages.setup

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@SuppressLint("MissingPermission")
@Composable
fun BluetoothDeviceListItem(bluetoothDevice: BluetoothDevice, onDeviceSelected: (BluetoothDevice) -> Unit) {
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