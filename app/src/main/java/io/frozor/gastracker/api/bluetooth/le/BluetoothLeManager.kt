package io.frozor.gastracker.api.bluetooth.le

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.frozor.gastracker.constants.LoggingTag
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.time.Duration.Companion.seconds

val SCAN_LENGTH = 5.seconds
val SCAN_INTERVAL = 60.seconds

class BluetoothLeManager(private val context: Context) {
    private val _bluetoothManager = context.getSystemService(BluetoothManager::class.java)
    private val _bluetoothLeScanner = _bluetoothManager.adapter.bluetoothLeScanner
    private val _isBluetoothEnabled = MutableLiveData(_bluetoothManager.adapter.isEnabled)

    private val _btAdapterBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val state = intent?.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                ?: return

            when (state) {
                BluetoothAdapter.STATE_OFF -> _isBluetoothEnabled.value = false
                BluetoothAdapter.STATE_ON -> _isBluetoothEnabled.value = true
            }
        }
    }

    val isEnabled: LiveData<Boolean> = _isBluetoothEnabled

    fun onStart() {
        context.registerReceiver(
            _btAdapterBroadcastReceiver,
            IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        )
    }

    fun onStop() {
        context.unregisterReceiver(_btAdapterBroadcastReceiver)
    }

    private suspend fun doNearbyCheckNoTimeout(deviceId: String): Boolean {
        Log.i(LoggingTag.Bluetooth, "Checking if device is nearby with id $deviceId")

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }

        return suspendCancellableCoroutine { continuation ->
            var isScanOver = false

            val scanCallback = object : ScanCallback() {
                @SuppressLint("MissingPermission")
                override fun onScanResult(callbackType: Int, result: ScanResult?) {
                    if (result?.device?.address != deviceId) {
                        Log.e(
                            LoggingTag.Bluetooth,
                            "Scan found a device that isn't ours! Filter is broken?"
                        )
                        return
                    }

                    Log.i(LoggingTag.Bluetooth, "Device was found in scan!")

                    if (!isScanOver) {
                        isScanOver = true
                        continuation.resume(true)
                        _bluetoothLeScanner.stopScan(this)
                    }
                }

                @SuppressLint("MissingPermission")
                override fun onScanFailed(errorCode: Int) {
                    if (!isScanOver) {
                        isScanOver = true
                        continuation.resumeWithException(Exception("Scan failed with error code $errorCode"))
                        _bluetoothLeScanner.stopScan(this)
                    }
                }
            }

            val scanFilter = ScanFilter.Builder()
                .setDeviceAddress(deviceId)
                .build()

            val scanSettings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build()

            _bluetoothLeScanner.startScan(listOf(scanFilter), scanSettings, scanCallback)

            continuation.invokeOnCancellation {
                if (!isScanOver) {
                    isScanOver = true
                    _bluetoothLeScanner.stopScan(scanCallback)
                }
            }
        }
    }

    suspend fun isDeviceNearby(deviceId: String): Boolean {
        val foundDeviceOrNull = withTimeoutOrNull(SCAN_LENGTH) {
            doNearbyCheckNoTimeout(deviceId)
        }
        return foundDeviceOrNull ?: false
    }
}