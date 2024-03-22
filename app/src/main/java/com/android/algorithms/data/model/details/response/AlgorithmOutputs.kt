package com.android.algorithms.data.model.details.response

import com.android.algorithms.data.model.details.data.DataValue

/**
 * Represents the outputs of an algorithm, typically in the form of a data values list.
 *
 * @property outputs The list of data values representing the outputs.
 * @constructor Creates an AlgorithmOutputs.
 */
data class AlgorithmOutputs(
    val outputs: List<DataValue>
) {
    constructor() : this(emptyList())
}
