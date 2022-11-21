package io.frozor.gastracker.ui.components.routing

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.frozor.gastracker.constants.Routes
import io.frozor.gastracker.ui.components.pages.initialload.InitialLoadView
import io.frozor.gastracker.state.AppState

fun NavGraphBuilder.initialLoadingPageGraph(navController: NavController, appState: AppState) {
    navigation(startDestination = Routes.InitialLoading.Main, route = Routes.Pages.InitialLoading) {
        composable(Routes.InitialLoading.Main) {
            LaunchedEffect(Unit) {
                navController.navigate(
                    if (appState.shouldRunSetup()) {
                        Routes.Pages.Setup
                    } else {
                        Routes.Pages.Home
                    }
                ) {
                    popUpTo(Routes.Pages.InitialLoading)
                }
            }
        }
    }
}