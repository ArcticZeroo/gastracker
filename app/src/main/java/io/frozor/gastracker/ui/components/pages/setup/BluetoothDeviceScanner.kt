package io.frozor.gastracker.ui.components.pages.setup

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat

@Composable
fun BluetoothDeviceScanner(onDeviceSelected: (BluetoothDevice) -> Unit) {
    val context = LocalContext.current

    var showOnlyNamedDevices by remember { mutableStateOf(false) }
    var foundDevices by remember { mutableStateOf(listOf<BluetoothDevice>()) }

    val bluetoothManager = remember {
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }

    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            bluetoothManager.adapter.startDiscovery()
        }
    }

    DisposableEffect(context) {
        val intentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val device: BluetoothDevice =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE) ?: return
                foundDevices = foundDevices.plus(device)
            }
        }

        context.registerReceiver(receiver, intentFilter)

        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    Row(
        modifier = Modifier
            .clickable { showOnlyNamedDevices = !showOnlyNamedDevices }
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Show only named devices:")
        Switch(
            checked = showOnlyNamedDevices,
            onCheckedChange = { value -> showOnlyNamedDevices = value })
    }
    BluetoothDeviceList(showOnlyNamedDevices, foundDevices, onDeviceSelected)
}