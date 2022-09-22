package io.frozor.gastracker.ui.components.pages.setup.bluetooth.le

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.frozor.gastracker.ui.data.BleDevice

@Composable
fun BluetoothLeDeviceListItem(bleDevice: BleDevice, isFirst: Boolean) {
    Column {
        Spacer(modifier = Modifier.height(16.dp))

        if (!isFirst) {
            Divider()
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(bleDevice.name ?: bleDevice.address)
        if (bleDevice.rssi != null) {
            Text("${bleDevice.rssi} dB")
        }
    }
}