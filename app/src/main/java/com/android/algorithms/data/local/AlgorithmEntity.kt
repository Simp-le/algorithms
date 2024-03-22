package com.android.algorithms.data.local

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Represents an entity that combines algorithm information with its parameters and outputs.
 *
 * @property algorithm The main information about the algorithm.
 * @property parameters The list of parameters associated with the algorithm.
 * @property outputs The list of outputs produced by the algorithm.
 * @constructor Creates an instance of AlgorithmEntity.
 */
data class AlgorithmEntity(
    @Embedded val algorithm: AlgorithmInfoEntity,
    @Relation(
        parentColumn = "algorithm_name",
        entityColumn = "algorithm_name"
    ) val parameters: List<AlgorithmDataEntity>,
    @Relation(
        parentColumn = "algorithm_name",
        entityColumn = "algorithm_name"
    ) val outputs: List<AlgorithmDataEntity>
)

