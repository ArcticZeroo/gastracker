package io.frozor.gastracker.ui.components.pages.setup.bluetooth.le

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import io.frozor.gastracker.ui.components.pages.setup.bluetooth.common.BluetoothDeviceListItem
import io.frozor.gastracker.ui.data.BleDevice

@SuppressLint("MissingPermission")
@Composable
fun BluetoothLeDeviceList(
    showOnlyNamedDevices: Boolean,
    foundDevices: Set<BleDevice>,
    onDeviceSelected: (device: BleDevice) -> Unit
) {
    var foundDevicesToShow by rememberSaveable { mutableStateOf(listOf<BleDevice>()) }

    LaunchedEffect(showOnlyNamedDevices, foundDevices) {
        foundDevicesToShow = (if (showOnlyNamedDevices) {
            foundDevices.filter { device -> device.name != null }
        } else {
            foundDevices.toList()
        }).sortedBy { if (it.rssi != null) -it.rssi else 0 }
    }

    LazyColumn {
        itemsIndexed(foundDevicesToShow) { index, device ->
            BluetoothLeDeviceListItem(
                bleDevice = device,
                isFirst = index == 0
            ) {
                onDeviceSelected(device)
            }
        }
    }
}