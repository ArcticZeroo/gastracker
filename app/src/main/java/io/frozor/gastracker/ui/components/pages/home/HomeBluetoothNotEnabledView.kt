package io.frozor.gastracker.ui.components.pages.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeBluetoothNotEnabledView() {
    Column {
        Text("You need to enable bluetooth!")
        Button(onClick = {  }) {
            Text("Enable Bluetooth")
        }
    }
}