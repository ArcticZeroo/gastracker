package io.frozor.gastracker.ui.components.pages.setup.bluetooth.le

import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.frozor.gastracker.ui.data.BleDevice

@Composable
fun BluetoothLeDeviceListItem(bleDevice: BleDevice, isFirst: Boolean, onDeviceSelected: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(16.dp))

        if (!isFirst) {
            Divider()
            Spacer(modifier = Modifier.height(16.dp))
        }

        Column(modifier = Modifier.fillMaxWidth().clickable { onDeviceSelected() }) {
            Text(bleDevice.name ?: bleDevice.address)
            if (bleDevice.rssi != null) {
                Text("${bleDevice.rssi} dB")
            }
        }
    }
}