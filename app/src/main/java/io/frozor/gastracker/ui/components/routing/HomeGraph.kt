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
import io.frozor.gastracker.util.hasPermission
import io.frozor.gastracker.util.popToParent

fun NavGraphBuilder.homeGraph(navController: NavController) {
    navigation(startDestination = Routes.Home.Main, route = Routes.Pages.Home) {
        Log.i(LoggingTag.App, "Rendering home graph")
        composable(Routes.Home.Main) {
            Log.i(LoggingTag.App, "Rendering home page")
            // TODO: Put this somehow in state
            val shouldRunSetup = true
            if (shouldRunSetup) {
                navController.navigate(Routes.Pages.Setup) {
                    popUpTo(0)
                }
            }
            else {
                HomeMain()
            }
        }
    }
}