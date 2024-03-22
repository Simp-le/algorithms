package com.android.algorithms.data.model.details.data

/**
 * Represents a data value with its name and corresponding value.
 *
 * @property name The name of the data value.
 * @property value The value of the data value.
 * @constructor Creates a DataValue.
 */
data class DataValue(
    val name: String,
    val value: Any
)