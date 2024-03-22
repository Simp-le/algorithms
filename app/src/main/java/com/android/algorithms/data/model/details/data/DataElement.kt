package com.android.algorithms.data.model.details.data

import com.google.gson.annotations.SerializedName

/**
 * Represents a data element with its properties including name, title, description,
 * data shape, data type, default value, and optionally user-provided or response value.
 *
 * @property name The name of the data element.
 * @property title The title of the data element.
 * @property description The description of the data element.
 * @property dataShape The shape of the data element.  (e.g., scalar, list, matrix).
 * @property dataType The type of the data element. (e.g., integer, float, string, boolean).
 * @property defaultValue The default value of the data element.
 * @property value The user's input for parameters or response value for outputs.
 * @constructor Creates a DataElement.
 */
data class DataElement(
    val name: String,
    val title: String,
    val description: String,

    @SerializedName("data_shape") val dataShape: String,
    @SerializedName("data_type") val dataType: String,
    @SerializedName("default_value") val defaultValue: Any,

    var value: Any? = null // User's input (for parameters) and respond value (for outputs) here
) {
    /**
     * Secondary constructor used for creating an empty instance with default values.
     */
    constructor() : this("", "", "", "", "", "")

    /**
     * Converts the data element into a DataValue object.
     *
     * @return A DataValue object representing the data element.
     */
    fun toDataValue(): DataValue {
        return DataValue(name = name, value = value ?: defaultValue)
    }
}