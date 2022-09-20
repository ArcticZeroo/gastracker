package io.frozor.gastracker.ui.components.routing

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.frozor.gastracker.constants.LoggingTag
import io.frozor.gastracker.constants.Routes
import io.frozor.gastracker.ui.components.pages.setup.SetupView

fun NavGraphBuilder.setupGraph(navController: NavController) {
    navigation(startDestination = Routes.Setup.Main, route = Routes.Pages.Setup) {
        composable(Routes.Setup.Main) {
            SetupView()
        }
    }
}