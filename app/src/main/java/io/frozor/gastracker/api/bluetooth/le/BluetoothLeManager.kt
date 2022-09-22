package io.frozor.gastracker.api.bluetooth.le

import android.Manifest
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.time.Duration.Companion.seconds

val SCAN_LENGTH = 15.seconds
val SCAN_INTERVAL = 60.seconds

class BluetoothLeManager(private val context: Context) {
    private val _bluetoothManager = context.getSystemService(BluetoothManager::class.java)
    private val _bluetoothLeScanner = _bluetoothManager.adapter.bluetoothLeScanner
    private var _currentScanJob: Deferred<Boolean>? = null

    private suspend fun doIsDeviceNearbyScan(deviceId: String): Boolean {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }

        var wasDeviceFound = false

        val scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                if (result?.device?.address == deviceId) {
                    wasDeviceFound = true
                }
            }

            override fun onScanFailed(errorCode: Int) {
                throw Exception("Scan failed with error code $errorCode")
            }
        }

        _bluetoothLeScanner.startScan(scanCallback)
        delay(SCAN_LENGTH)
        _bluetoothLeScanner.stopScan(scanCallback)

        return wasDeviceFound
    }

    suspend fun isDeviceNearby(deviceId: String): Boolean {
        var scanJob: Deferred<Boolean>? = null

        synchronized(this) {
            val currentScanJob = _currentScanJob
            if (currentScanJob != null) {
                scanJob = currentScanJob
            }
        }

        if (scanJob == null) {
            coroutineScope {
                synchronized(this) {
                    scanJob = async { doIsDeviceNearbyScan(deviceId) }
                    _currentScanJob = scanJob
                }
            }
        }

        if (scanJob == null) {
            throw Exception("Threading problem: scan job is null")
        }

        val foundDevice = scanJob?.await() ?: false
        synchronized(this) {
            _currentScanJob = null
        }
        return foundDevice
    }
}