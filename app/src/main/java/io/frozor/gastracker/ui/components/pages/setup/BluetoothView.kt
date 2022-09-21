package io.frozor.gastracker.ui.components.pages.setup

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@SuppressLint("MissingPermission")
@Composable
fun BluetoothView() {
    val context = LocalContext.current
    BluetoothDeviceScanner { device, isAlreadyPaired ->
        Toast.makeText(context, "Selected ${device.name ?: "Unknown device"}${if (isAlreadyPaired) ", already paired" else ", not yet paired"}", Toast.LENGTH_LONG).show()
    }
}