package com.android.algorithms.data.model.details.response

/**
 * Represents the response from an algorithm execution,
 * including the outputs and any errors encountered.
 *
 * @property result The outputs produced by the algorithm.
 * @property errors Any errors encountered during the execution of the algorithm.
 * @constructor Creates an AlgorithmResponse.
 */
data class AlgorithmResponse(
    val result: AlgorithmOutputs,
    val errors: String
)
