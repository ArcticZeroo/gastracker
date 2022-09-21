package io.frozor.gastracker.ui.components.pages.setup

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@SuppressLint("MissingPermission")
@Composable
fun BluetoothView() {
    val context = LocalContext.current
    BluetoothDeviceScanner {
        Toast.makeText(context, "Selected ${it.name ?: "Unknown device"}", Toast.LENGTH_LONG).show()
    }
}