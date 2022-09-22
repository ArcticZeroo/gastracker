package io.frozor.gastracker.ui.components.routing

import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.frozor.gastracker.constants.LoggingTag
import io.frozor.gastracker.constants.Routes
import io.frozor.gastracker.ui.components.pages.home.HomeMain
import io.frozor.gastracker.ui.state.AppState
import io.frozor.gastracker.util.hasPermission
import io.frozor.gastracker.util.popToParent

fun NavGraphBuilder.homeGraph(navController: NavController, appState: AppState) {
    navigation(startDestination = Routes.Home.Main, route = Routes.Pages.Home) {
        composable(Routes.Home.Main) {
            HomeMain()
        }
    }
}