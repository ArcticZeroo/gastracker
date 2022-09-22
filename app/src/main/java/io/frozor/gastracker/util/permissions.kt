package io.frozor.gastracker.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun hasPermission(context: Context, permission: String) =
    ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

fun hasPermissions(context: Context, permissions: List<String>) =
    permissions.all { hasPermission(context, it) }

val permissionFriendlyNames = mapOf(
    Manifest.permission.BLUETOOTH_SCAN to "Scan for Bluetooth Devices",
    Manifest.permission.BLUETOOTH_CONNECT to "Connect to Bluetooth Devices",
    Manifest.permission.ACCESS_FINE_LOCATION to "Location access (fine)"
)