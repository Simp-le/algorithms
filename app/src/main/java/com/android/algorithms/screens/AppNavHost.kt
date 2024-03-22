package com.android.algorithms.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = Screen.AlgorithmList.route,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.AlgorithmList.route) {
            val navBackResult = it.savedStateHandle.get<Boolean>("isChanged")
            AlgorithmListScreen(navController, navBackResult ?: false)
        }
        composable(
            route = Screen.Details.route + "/{algorithmName}",
            arguments = listOf(navArgument("algorithmName") { type = NavType.StringType })
        ) {
            AlgorithmDetailsScreen(navController)
        }
    }
}