package com.android.algorithms.data.model.details

import com.android.algorithms.data.model.details.data.DataElement

/**
 * Represents the result of algorithm details, including its name, title, description,
 * parameters, and outputs.
 *
 * @property name The unique name of the algorithm.
 * @property title The title of the algorithm.
 * @property description The description of the algorithm.
 * @property parameters The list of parameters associated with the algorithm.
 * @property outputs The list of outputs produced by the algorithm.
 * @constructor Creates an AlgorithmDetailsResult.
 */
data class AlgorithmDetailsResult(
    val name: String,
    val title: String,
    val description: String,

    val parameters: List<DataElement>,
    val outputs: List<DataElement>
) {
    /**
     * Secondary constructor used for creating an empty instance with default values.
     */
    constructor() : this("", "", "", listOf(DataElement()), listOf(DataElement()))
}