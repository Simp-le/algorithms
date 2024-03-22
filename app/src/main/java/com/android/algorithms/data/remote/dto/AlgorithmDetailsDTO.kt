package com.android.algorithms.data.remote.dto

import com.android.algorithms.data.model.details.AlgorithmDetailsResult

/**
 * Represents the Data Transfer Object (DTO) for algorithm details,
 * including the result with details and any errors encountered.
 *
 * @property result The algorithm details.
 * @property errors Any errors encountered during the operation.
 * @constructor Creates an AlgorithmDetailsDTO.
 */
data class AlgorithmDetailsDTO(
    val result: AlgorithmDetailsResult?,
    val errors: String?
)