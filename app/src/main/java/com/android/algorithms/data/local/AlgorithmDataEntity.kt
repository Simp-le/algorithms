package com.android.algorithms.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents an entity class for storing algorithm field data in the database.
 *
 * @property dataId The unique identifier for the algorithm data. The default value is 0.
 * @property algorithmName The name of the algorithm associated with the data.
 * @property isInput Indicates whether the data is an input for the algorithm.
 * @property fieldName The name of the field represented by the algorithm data.
 * @property title The title of the algorithm data.
 * @property description The description of the algorithm data.
 * @property dataShape The shape of the data (e.g., scalar, list, matrix).
 * @property dataType The type of the data (e.g., integer, float, string, boolean).
 * @property defaultValue The default value of the algorithm data.
 * @constructor Creates an instance of AlgorithmDataEntity.
 * @see Entity
 */
@Entity(tableName = "algorithm_data")
data class AlgorithmDataEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "data_id") val dataId: Int = 0,
    @ColumnInfo(name = "algorithm_name") val algorithmName: String,
    @ColumnInfo(name = "is_input") val isInput: Boolean,
    @ColumnInfo(name = "algorithm_field_name") val fieldName: String,

    val title: String,
    val description: String,
    @ColumnInfo(name = "data_shape") val dataShape: String,
    @ColumnInfo(name = "data_type") val dataType: String,
    @ColumnInfo(name = "default_value") val defaultValue: String
)
