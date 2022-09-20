package io.frozor.gastracker.ui.components.pages.setup

import android.Manifest
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.frozor.gastracker.constants.LoggingTag
import io.frozor.gastracker.constants.ProgressState
import io.frozor.gastracker.constants.Styles
import io.frozor.gastracker.constants.requiredPermissions
import io.frozor.gastracker.ui.components.pages.PageContainer
import io.frozor.gastracker.ui.components.progress.ProgressBubble
import io.frozor.gastracker.util.hasPermission

@Composable
fun SetupView() {
    PageContainer {
        Card(modifier = Modifier.padding(Styles.DefaultPadding)) {
            Column {
                val remainingRequiredPermissions =
                    requiredPermissions.filter { requiredPermission ->
                        !hasPermission(
                            LocalContext.current, requiredPermission
                        )
                    }

                if (remainingRequiredPermissions.isNotEmpty()) {
                    Text("We need to get some more permissions from you.")
                    Text("We need to use bluetooth to find your beacon, and location to track your gas gauge usage.")
                    Button(onClick = {  }) {

                    }
                }

                for (requiredPermission in requiredPermissions) {
                    val hasRequiredPermission =
                        hasPermission(LocalContext.current, requiredPermission)
                    if (!hasRequiredPermission) {

                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    ProgressBubble(status = ProgressState.SUCCEEDED)
                    Text("Step 1: Grant Permissions")
                }
            }
        }
    }
}