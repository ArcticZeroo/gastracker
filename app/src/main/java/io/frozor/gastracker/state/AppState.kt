@file:OptIn(ExperimentalPermissionsApi::class)

package io.frozor.gastracker.state

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import io.frozor.gastracker.api.bluetooth.le.BluetoothLeManager
import io.frozor.gastracker.api.storage.settings.getDeviceId
import io.frozor.gastracker.api.storage.settings.setDeviceId
import io.frozor.gastracker.constants.LoggingTag
import io.frozor.gastracker.constants.requiredForegroundPermissions
import io.frozor.gastracker.util.hasPermissions
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AppState(private val context: Context) {
    val bluetoothLeManager = BluetoothLeManager(context)

    private var _isDeviceIdBeingFetched = false
    private val _hasDeviceIdBeenFetched = MutableLiveData(false)
    private val _deviceId = MutableLiveData<String?>(null)
    private val _areAllPermissionsGranted = MutableLiveData(false)

    val hasDeviceIdBeenFetched: LiveData<Boolean> = _hasDeviceIdBeenFetched
    val deviceId: LiveData<String?> = _deviceId
    val areAllPermissionsGranted: LiveData<Boolean> = _areAllPermissionsGranted

    init {
        retrieveDeviceId()
        updatePermissionStatus()
    }

    private fun deviceIdLock() = this

    fun onStart() {
        bluetoothLeManager.onStart()
    }

    fun onStop() {
        bluetoothLeManager.onStop()
    }

    fun onDeviceIdChanged(deviceId: String?) {
        _hasDeviceIdBeenFetched.value = true
        _deviceId.value = deviceId
        runBlocking {
            launch {
                setDeviceId(context, deviceId)
            }
        }
    }

    fun updatePermissionStatus() {
        Log.i(LoggingTag.App, "Updating permission state")
        _areAllPermissionsGranted.value = hasPermissions(context, requiredForegroundPermissions)
        Log.i(LoggingTag.App, "Are all permissions granted: ${_areAllPermissionsGranted.value}")
    }

    private fun retrieveDeviceId() {
        synchronized(deviceIdLock()) {
            if (_isDeviceIdBeingFetched || _hasDeviceIdBeenFetched.value == true) {
                return
            }
            _isDeviceIdBeingFetched = true
        }

        runBlocking {
            launch {
                val deviceId = getDeviceId(context).first()
                synchronized(deviceIdLock()) {
                    _isDeviceIdBeingFetched = false
                    // We're the only caller of this method, so if hasDeviceIdBeenFetched is true,
                    // something else in the app set it, and we can discard the value we were
                    // already grabbing from storage
                    if (_hasDeviceIdBeenFetched.value != true) {
                        _deviceId.value = deviceId
                    }
                    _hasDeviceIdBeenFetched.value = true
                }
            }
        }
    }

    @Composable
    fun isLoading(): Boolean {
        val hasDeviceIdBeenFetched by this.hasDeviceIdBeenFetched.observeAsState()
        return hasDeviceIdBeenFetched != true
    }

    fun shouldRunSetup(): Boolean =
        areAllPermissionsGranted.value != true || this.deviceId.value == null

    @Composable
    fun shouldRunSetupAsState(): Boolean {
        val deviceId by this.deviceId.observeAsState()
        val areAllPermissionsGranted by this.areAllPermissionsGranted.observeAsState()

        return areAllPermissionsGranted != true || deviceId == null
    }
}