@file:OptIn(ExperimentalPermissionsApi::class)

package io.frozor.gastracker.ui.state

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import io.frozor.gastracker.api.storage.settings.getDeviceId
import io.frozor.gastracker.api.storage.settings.setDeviceId
import io.frozor.gastracker.constants.requiredForegroundPermissions
import io.frozor.gastracker.util.hasPermissions
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AppState(private val context: Context) {
    private var _isDeviceIdBeingFetched = false
    private val _hasDeviceIdBeenFetched = MutableLiveData(false)
    private val _deviceId = MutableLiveData<String?>(null)

    val hasDeviceIdBeenFetched: LiveData<Boolean> = _hasDeviceIdBeenFetched
    val deviceId: LiveData<String?> = _deviceId

    init {
        retrieveDeviceId()
    }

    private fun deviceIdLock() = this

    fun onDeviceIdChanged(deviceId: String?) {
        _hasDeviceIdBeenFetched.value = true
        _deviceId.value = deviceId
        runBlocking {
            launch {
                setDeviceId(context, deviceId)
            }
        }
    }

    private fun retrieveDeviceId() {
        synchronized(deviceIdLock()) {
            if (_isDeviceIdBeingFetched || _hasDeviceIdBeenFetched.value == true) {
                return
            }
            _isDeviceIdBeingFetched = true
        }

        // Bleh. JS vibes.
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

    @Composable
    fun shouldRunSetup(): Boolean {
        val deviceId by this.deviceId.observeAsState()
        val permissionsState = rememberMultiplePermissionsState(permissions = requiredForegroundPermissions)

        return !permissionsState.allPermissionsGranted || deviceId == null
    }
}