package io.frozor.gastracker.ui.components.routing

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.frozor.gastracker.constants.LoggingTag
import io.frozor.gastracker.constants.Routes
import io.frozor.gastracker.state.AppState

@Composable
fun NavigationRoot(appState: AppState) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Pages.InitialLoading) {
        initialLoadingPageGraph(navController, appState)
        homeGraph(navController, appState)
        setupGraph(navController, appState)
    }
}