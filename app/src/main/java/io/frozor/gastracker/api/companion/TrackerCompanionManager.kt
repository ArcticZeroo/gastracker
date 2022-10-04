package io.frozor.gastracker.api.companion

import android.bluetooth.le.ScanFilter
import android.companion.AssociationInfo
import android.companion.AssociationRequest
import android.companion.BluetoothLeDeviceFilter
import android.companion.CompanionDeviceManager
import android.content.Context
import android.content.IntentSender
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.frozor.gastracker.constants.LoggingTag
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class TrackerCompanionManager(private val context: Context) {
    private val _companionDeviceManager: CompanionDeviceManager = context.getSystemService(CompanionDeviceManager::class.java)
    private var _currentAssociationInfo: AssociationInfo? = null
    private var _associatedDeviceId = MutableLiveData<String?>(companionAssociatedDeviceId())

    val associatedDeviceId: LiveData<String?> = _associatedDeviceId

    private fun companionAssociatedDeviceId(): String? {
        return _companionDeviceManager.associations.firstOrNull()
    }

    fun clearAssociations() {
        _companionDeviceManager.associations.forEach { _companionDeviceManager.disassociate(it) }
    }

    suspend fun associateDevice(deviceId: String) {
        if (companionAssociatedDeviceId() == deviceId) {
            return
        }

        clearAssociations()

        return suspendCancellableCoroutine {  continuation ->
            val associationCallback = object : CompanionDeviceManager.Callback() {
                override fun onAssociationPending(intentSender: IntentSender) {
                    Log.i(LoggingTag.Bluetooth, "Association pending with device $deviceId")
                }

                override fun onAssociationCreated(associationInfo: AssociationInfo) {
                    if (continuation.isActive) {
                        Log.i(LoggingTag.Bluetooth, "Created association with device $deviceId")
                        _currentAssociationInfo = associationInfo
                        _associatedDeviceId.value = deviceId
                        _companionDeviceManager.startObservingDevicePresence(deviceId)
                        continuation.resume(Unit)
                    }
                }

                override fun onFailure(errorMessage: CharSequence?) {
                    if (continuation.isActive) {
                        val message = "Failed to associate with device $deviceId: $errorMessage"
                        Log.e(
                            LoggingTag.Bluetooth,
                            message
                        )
                        continuation.resumeWithException(Exception(message))
                    }
                }
            }

            val scanFilter = ScanFilter.Builder()
                .setDeviceAddress(deviceId)
                .build()
            val btleFilter = BluetoothLeDeviceFilter.Builder()
                .setScanFilter(scanFilter)
                .build()

            val associationRequest = AssociationRequest.Builder()
                .addDeviceFilter(btleFilter)
                .setSingleDevice(true)
                .build()

            _companionDeviceManager.associate(associationRequest, associationCallback, null /*handler*/)

            continuation.invokeOnCancellation {
                _currentAssociationInfo = null
            }
        }
    }
}