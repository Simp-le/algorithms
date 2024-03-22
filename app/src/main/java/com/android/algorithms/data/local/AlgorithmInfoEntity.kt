package com.android.algorithms.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents an entity class for storing the main information about algorithms in the database.
 *
 * @property name The unique name of the algorithm.
 * @property title The title of the algorithm.
 * @property description The description of the algorithm.
 * @constructor Creates an AlgorithmInfoEntity.
 * @see Entity
 */
@Entity(tableName = "algorithms")
data class AlgorithmInfoEntity(
    @PrimaryKey
    // There can't be duplicates, but they can change over time
    @ColumnInfo(name = "algorithm_name") val name: String,

    val title: String,
    val description: String
)
