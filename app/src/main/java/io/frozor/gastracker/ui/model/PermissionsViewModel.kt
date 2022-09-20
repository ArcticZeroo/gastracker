package io.frozor.gastracker.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PermissionsViewModel : ViewModel() {
    private val _canControlBluetooth = MutableLiveData(false)
    private val _bleBeaconId = MutableLiveData("")

    val canControlBluetooth: LiveData<Boolean> = _canControlBluetooth
    val bleBeaconId: LiveData<String> = _bleBeaconId

    fun onCanControlBluetoothChanged(canControlBluetooth: Boolean) {
        _canControlBluetooth.value = canControlBluetooth
    }

    fun onBleBeaconIdChanged(bleBeaconId: String) {
        _bleBeaconId.value = bleBeaconId
    }

    fun requiresSetup() {

    }
}