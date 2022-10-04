@file:OptIn(ExperimentalPermissionsApi::class)

package io.frozor.gastracker.state

import android.companion.AssociationInfo
import android.companion.AssociationRequest
import android.companion.CompanionDeviceManager
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import io.frozor.gastracker.api.bluetooth.le.BluetoothLeManager
import io.frozor.gastracker.api.companion.TrackerCompanionManager
import io.frozor.gastracker.api.storage.settings.getDeviceId
import io.frozor.gastracker.api.storage.settings.setDeviceId
import io.frozor.gastracker.constants.LoggingTag
import io.frozor.gastracker.constants.requiredForegroundPermissions
import io.frozor.gastracker.util.hasPermissions
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

class AppState(private val context: Context) {
    val bluetoothLeManager = BluetoothLeManager(context)
    val trackerCompanionManager = TrackerCompanionManager(context)

    private val _areAllPermissionsGranted = MutableLiveData(false)

    val areAllPermissionsGranted: LiveData<Boolean> = _areAllPermissionsGranted

    init {
        updatePermissionStatus()
    }

    fun onStart() {
        bluetoothLeManager.onStart()
    }

    fun onStop() {
        bluetoothLeManager.onStop()
    }

    fun onDeviceIdChanged(deviceId: String?) {
        if (deviceId == null) {
            trackerCompanionManager.clearAssociations()
            return
        }

        // We want to do this async, when the association occurs we'll associate the device in here
        // and the UI will update accordingly.
        CoroutineScope(Dispatchers.Default).launch {
            trackerCompanionManager.associateDevice(deviceId)
        }
    }

    fun updatePermissionStatus() {
        Log.i(LoggingTag.App, "Updating permission state")
        _areAllPermissionsGranted.value = hasPermissions(context, requiredForegroundPermissions)
        Log.i(LoggingTag.App, "Are all permissions granted: ${_areAllPermissionsGranted.value}")
    }

    fun shouldRunSetup(): Boolean =
        areAllPermissionsGranted.value != true
                || this.trackerCompanionManager.associatedDeviceId.value == null
}