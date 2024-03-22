package com.android.algorithms.data.remote.dto

import com.android.algorithms.data.model.details.response.AlgorithmOutputs

/**
 * Represents the Data Transfer Object (DTO) for algorithm response,
 * including the outputs and any errors encountered.
 *
 * @property result The outputs produced by the algorithm.
 * @property errors Any errors encountered during the execution of the algorithm.
 * @constructor Creates an AlgorithmResponseDTO.
 */
data class AlgorithmResponseDTO(
    val result: AlgorithmOutputs?,
    val errors: String?
)