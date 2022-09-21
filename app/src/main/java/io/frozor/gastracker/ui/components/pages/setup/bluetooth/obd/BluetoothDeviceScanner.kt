package io.frozor.gastracker.ui.components.pages.setup.bluetooth.obd

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
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat

@Composable
fun BluetoothDeviceScanner(onDeviceSelected: (BluetoothDevice, Boolean) -> Unit) {
    val context = LocalContext.current

    var showOnlyNamedDevices by rememberSaveable { mutableStateOf(false) }
    var allFoundDevices by rememberSaveable { mutableStateOf(listOf<BluetoothDevice>()) }
    var foundDevicesToShow by remember { mutableStateOf(listOf<BluetoothDevice>()) }

    val bluetoothManager = remember {
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }

    val allPairedDevices = remember { bluetoothManager.adapter.bondedDevices }
    var pairedDevicesToShow by remember { mutableStateOf(listOf<BluetoothDevice>()) }

    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            bluetoothManager.adapter.startDiscovery()
        }
    }

    LaunchedEffect(allFoundDevices, allPairedDevices) {
        val newFoundDevicesToShow = ArrayList<BluetoothDevice>()
        val newPairedDevicesToShow = ArrayList<BluetoothDevice>()
        for (foundDevice in allFoundDevices) {
            if (allPairedDevices.contains(foundDevice)) {
                newPairedDevicesToShow.add(foundDevice)
            } else {
                newFoundDevicesToShow.add(foundDevice)
            }
        }
        foundDevicesToShow = newFoundDevicesToShow
        pairedDevicesToShow = newPairedDevicesToShow
    }

    DisposableEffect(context) {
        val intentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val device: BluetoothDevice =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE) ?: return
                allFoundDevices = allFoundDevices.plus(device)
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
    BluetoothDeviceList(
        showOnlyNamedDevices,
        pairedDevices = pairedDevicesToShow,
        foundDevices = foundDevicesToShow,
        onDeviceSelected = { device -> onDeviceSelected(device, allPairedDevices.contains(device)) }
    )
}