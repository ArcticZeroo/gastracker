package io.frozor.gastracker

import android.companion.CompanionDeviceService
import android.util.Log
import io.frozor.gastracker.constants.LoggingTag
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class TripTrackerService : CompanionDeviceService() {
    private var _appearTime: Instant? = null

    override fun onDeviceAppeared(address: String) {
        super.onDeviceAppeared(address)
        _appearTime = Clock.System.now()
    }

    override fun onDeviceDisappeared(address: String) {
        super.onDeviceDisappeared(address)
        val appearTime = _appearTime
        if (appearTime == null) {
            Log.e(LoggingTag.CompanionService, "Something went wrong, appear time was not logged!")
            return
        }
        Log.i(LoggingTag.CompanionService, "Device disappeared, it was in range for: ${Clock.System.now().minus(appearTime)}")
    }
}