package io.frozor.gastracker.ui.components.routing

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.frozor.gastracker.constants.Routes
import io.frozor.gastracker.ui.components.pages.home.HomeView
import io.frozor.gastracker.ui.state.AppState

fun NavGraphBuilder.homeGraph(navController: NavController, appState: AppState) {
    navigation(startDestination = Routes.Home.Main, route = Routes.Pages.Home) {
        composable(Routes.Home.Main) {
            HomeView(appState)
        }
    }
}