package io.frozor.gastracker.util

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanRecord

const val iBeaconDataTag = 0x004c

@SuppressLint("MissingPermission")
fun getDisplayName(bluetoothDevice: BluetoothDevice): String = bluetoothDevice.name
    ?: bluetoothDevice.alias
    ?: bluetoothDevice.address
    ?: "Unknown device"

fun isIBeacon(scanRecord: ScanRecord?) =
    scanRecord?.getManufacturerSpecificData(iBeaconDataTag) != null