package io.frozor.gastracker.util

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.PopUpToBuilder

fun NavOptionsBuilder.popToParent(navController: NavController, builder: PopUpToBuilder.() -> Unit = {}) {
    popUpTo(navController.graph.findStartDestination().id, builder)
}

fun NavController.navigateAndReplace(route: String) {
    navigate(route) {
        popUpTo(0)
    }
}