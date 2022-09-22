package io.frozor.gastracker.ui.components.routing

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.frozor.gastracker.constants.Routes
import io.frozor.gastracker.ui.components.pages.initialload.InitialLoadView
import io.frozor.gastracker.ui.state.AppState
import io.frozor.gastracker.util.navigateAndReplace

fun NavGraphBuilder.initialLoadingPageGraph(navController: NavController, appState: AppState) {
    navigation(startDestination = Routes.InitialLoading.Main, route = Routes.Pages.InitialLoading) {
        composable(Routes.InitialLoading.Main) {
            if (appState.isLoading()) {
                InitialLoadView()
            } else {
                navController.navigateAndReplace(
                    if (appState.shouldRunSetup()) {
                        Routes.Pages.Setup
                    } else {
                        Routes.Pages.Home
                    }
                )
            }
        }
    }
}