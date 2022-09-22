package io.frozor.gastracker.ui.state

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.frozor.gastracker.api.storage.settings.getDeviceId
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AppState {
    private var _isDeviceIdBeingFetched = false
    private val _isDeviceIdFetched = MutableLiveData(false)
    private val _deviceId = MutableLiveData<String?>(null)

    val hasDeviceIdBeenFetched: LiveData<Boolean> = _isDeviceIdFetched
    val deviceId: LiveData<String?> = _deviceId

    fun onDeviceIdChanged(deviceId: String?) {
        _isDeviceIdFetched.value = true
        _deviceId.value = deviceId
    }

    fun retrieveDeviceId(context: Context) {
        synchronized(this) {
            if (_isDeviceIdBeingFetched || _isDeviceIdFetched.value == true) {
                return
            }

            _isDeviceIdBeingFetched = true
        }

        // Bleh. JS vibes.
        val self = this

        runBlocking {
            launch {
                _deviceId.value = getDeviceId(context).first()
                synchronized(self) {
                    _isDeviceIdBeingFetched = false
                    _isDeviceIdFetched.value = true
                }
            }
        }
    }
}