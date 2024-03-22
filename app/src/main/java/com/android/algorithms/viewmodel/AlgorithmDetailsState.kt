package com.android.algorithms.viewmodel

import com.android.algorithms.data.model.details.AlgorithmDetailsResult

/**
 * Data class representing the state of algorithm details.
 *
 * @property isLoading Indicates if the details are currently being loaded.
 * @property isDownloaded Indicates if the details have been successfully downloaded.
 * @property errorMessage The error message, if any.
 * @property algorithmDetailsResult The algorithm details result.
 */
data class AlgorithmDetailsState(
    val isLoading: Boolean = false,
    val isDownloaded: Boolean = false,
    val errorMessage: String = "",
    val algorithmDetailsResult: AlgorithmDetailsResult = AlgorithmDetailsResult(),
    var isChanged: Boolean = false
)