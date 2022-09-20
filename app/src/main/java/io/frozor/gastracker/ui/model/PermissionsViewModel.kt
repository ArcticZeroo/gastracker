package io.frozor.gastracker.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PermissionsViewModel : ViewModel() {
    private val _hasRequiredPermissions = MutableLiveData(false)
    private val _bluetoothDeviceId = MutableLiveData("")

    val hasRequiredPermissions: LiveData<Boolean> = _hasRequiredPermissions
    val bluetoothDeviceId: LiveData<String> = _bluetoothDeviceId

    fun onCanControlBluetoothChanged(canControlBluetooth: Boolean) {
        _hasRequiredPermissions.value = canControlBluetooth
    }

    fun onBleBeaconIdChanged(bleBeaconId: String) {
        _bluetoothDeviceId.value = bleBeaconId
    }

    fun requiresSetup(): Boolean {
        return (_hasRequiredPermissions.value ?: false)
                && (_bluetoothDeviceId.value?.isNotEmpty() ?: false)
    }
}