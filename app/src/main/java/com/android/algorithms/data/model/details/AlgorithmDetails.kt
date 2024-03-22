package com.android.algorithms.data.model.details

/**
 * Represents details about an algorithm, all the details are stored in the result property.
 *
 * @property result The algorithm details.
 * @property errors Any errors encountered during getting the result from the DTO.
 * @constructor Creates an AlgorithmDetails.
 */
data class AlgorithmDetails(
    val result: AlgorithmDetailsResult,
    val errors: String
)