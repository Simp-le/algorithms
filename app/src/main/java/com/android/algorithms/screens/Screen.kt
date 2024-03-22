package com.android.algorithms.screens

/**
 * Represents the screens in the application.
 *
 * @property route The route associated with the screen.
 */
sealed class Screen(val route: String) {
    /**
     * Represents the algorithm list screen.
     */
    data object AlgorithmList : Screen("algorithmList")

    /**
     * Represents the details screen.
     */
    data object Details : Screen("details")
}