package com.android.algorithms.data.remote.dto

import com.android.algorithms.data.model.Algorithm

/**
 * Represents the Data Transfer Object (DTO) for a list of algorithms.
 *
 * @property algorithms The list of algorithms.
 * @constructor Creates an AlgorithmListDTO.
 */
data class AlgorithmListDTO(
    val algorithms: List<Algorithm>
)