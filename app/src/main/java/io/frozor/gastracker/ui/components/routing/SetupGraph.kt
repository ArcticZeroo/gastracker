package io.frozor.gastracker.ui.components.routing

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.frozor.gastracker.constants.Routes
import io.frozor.gastracker.ui.components.pages.setup.SetupView
import io.frozor.gastracker.state.AppState

fun NavGraphBuilder.setupGraph(navController: NavController, appState: AppState) {
    navigation(startDestination = Routes.Setup.Main, route = Routes.Pages.Setup) {
        composable(Routes.Setup.Main) {
            SetupView(navController, appState)
        }
    }
}