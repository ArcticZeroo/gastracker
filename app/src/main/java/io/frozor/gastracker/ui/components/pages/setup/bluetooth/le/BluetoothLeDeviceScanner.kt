package io.frozor.gastracker.ui.components.pages.setup.bluetooth.le

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import io.frozor.gastracker.constants.LoggingTag
import io.frozor.gastracker.ui.data.BleDevice
import io.frozor.gastracker.util.isIBeacon

// probably not needed, I can just use the keBeacon app
//val batteryServiceId = 0x180f
//val batteryLevelId = 0x2a19

@SuppressLint("MissingPermission")
@Composable
fun BluetoothLeDeviceScanner(context: Context) {
    var showOnlyNamedDevices by rememberSaveable { mutableStateOf(false) }
    var foundDevices by rememberSaveable { mutableStateOf<Set<BleDevice>>(setOf()) }
    var didScanFail by rememberSaveable { mutableStateOf(false) }

    val bluetoothScanner = remember {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter.bluetoothLeScanner
    }

    val scanResultCallback = remember {
        object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                val device = result?.device ?: return

                if (!isIBeacon(result.scanRecord)) {
                    return
                }

                if (result.scanRecord?.deviceName == "Neigh") {
                    Log.i(LoggingTag.App, "Found neigh!")
                }

                val bleDevice = BleDevice(
                    address = device.address,
                    name = result.scanRecord?.deviceName,
                    rssi = result.rssi
                )

                foundDevices = foundDevices.plus(bleDevice)
            }

            override fun onScanFailed(errorCode: Int) {
                didScanFail = true
            }
        }
    }

    fun startScan() {
        foundDevices = setOf()
        bluetoothScanner.startScan(scanResultCallback)
    }

    DisposableEffect(bluetoothScanner, context) {
        Log.i(LoggingTag.App, "Running btle scan")
        startScan()

        onDispose {
            bluetoothScanner.stopScan(scanResultCallback)
        }
    }

    Column {
        if (didScanFail) {
            Text("Scan failed!")
            Button(onClick = { startScan() }) {
                Text("Scan Again")
            }
            Divider()
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Show only named devices:")
            Switch(checked = showOnlyNamedDevices, onCheckedChange = { showOnlyNamedDevices = it })
        }

        Divider()

        BluetoothLeDeviceList(showOnlyNamedDevices, foundDevices) {
            Toast.makeText(
                context,
                "Selected ${it.name ?: it.address ?: "Unknown"}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}