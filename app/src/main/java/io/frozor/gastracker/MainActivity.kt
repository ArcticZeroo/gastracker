package io.frozor.gastracker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.frozor.gastracker.constants.LoggingTag
import io.frozor.gastracker.ui.components.routing.NavigationRoot
import io.frozor.gastracker.state.AppState
import io.frozor.gastracker.ui.theme.GasTrackerTheme

class MainActivity : ComponentActivity() {
    var appState: AppState? = null

    override fun onStart() {
        super.onStart()
        appState = AppState(context = applicationContext)
        appState?.onStart()
    }

    override fun onStop() {
        super.onStop()
        appState?.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (appState == null) {
            appState = AppState(applicationContext)
        }
        setContent {
            GasTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavigationRoot(appState!!)
                }
            }
        }
    }
}