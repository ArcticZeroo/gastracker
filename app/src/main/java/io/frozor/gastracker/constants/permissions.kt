package io.frozor.gastracker.constants

import android.Manifest

val requiredForegroundPermissions = listOf(
    Manifest.permission.BLUETOOTH_SCAN,
    Manifest.permission.BLUETOOTH_CONNECT,
    Manifest.permission.ACCESS_FINE_LOCATION,
)

val requiredBackgroundPermissions = listOf(
    Manifest.permission.ACCESS_BACKGROUND_LOCATION
)