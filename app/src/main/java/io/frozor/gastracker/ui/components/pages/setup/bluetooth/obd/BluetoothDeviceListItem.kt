package io.frozor.gastracker.ui.components.pages.setup.bluetooth.common

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.frozor.gastracker.util.getDisplayName

@SuppressLint("MissingPermission")
@Composable
fun BluetoothDeviceListItem(bluetoothDevice: BluetoothDevice, onDeviceSelected: (BluetoothDevice) -> Unit) {
    Text(
        getDisplayName(bluetoothDevice),
        modifier = Modifier
            .clickable {
                onDeviceSelected(bluetoothDevice)
            }
            .padding(16.dp)
            .fillMaxWidth()

    )
}