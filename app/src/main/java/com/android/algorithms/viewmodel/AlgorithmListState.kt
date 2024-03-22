package com.android.algorithms.viewmodel

import com.android.algorithms.data.model.Algorithm

/**
 * Represents the state of the algorithm list screen.
 *
 * @property isLoading Indicates whether the algorithm list is currently being loaded.
 * @property errorMessage The error message, if any, occurred during the loading process.
 * @property algorithmList The list of algorithms displayed on the screen.
 */
data class AlgorithmListState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val algorithmList: List<Algorithm> = emptyList()
)