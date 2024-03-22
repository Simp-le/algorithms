package com.android.algorithms.data

import com.android.algorithms.data.local.AlgorithmDataEntity
import com.android.algorithms.data.local.AlgorithmEntity
import com.android.algorithms.data.local.AlgorithmInfoEntity
import com.android.algorithms.data.model.Algorithm
import com.android.algorithms.data.model.details.AlgorithmDetails
import com.android.algorithms.data.model.details.AlgorithmDetailsResult
import com.android.algorithms.data.model.details.data.DataElement
import com.android.algorithms.data.model.details.response.AlgorithmOutputs
import com.android.algorithms.data.model.details.response.AlgorithmResponse
import com.android.algorithms.data.remote.dto.AlgorithmDetailsDTO
import com.android.algorithms.data.remote.dto.AlgorithmResponseDTO
import com.google.gson.Gson

//region API
/**
 * Turns [AlgorithmResponseDTO] into an [AlgorithmResponse] instance.
 * @return [AlgorithmResponse] instance with a non-nullable fields.
 */
fun AlgorithmResponseDTO.toAlgorithmResponse(): AlgorithmResponse {
    return AlgorithmResponse(
        result = result ?: AlgorithmOutputs(),
        errors = errors ?: ""
    )
}

/**
 * Turns [AlgorithmDetailsDTO] into an [AlgorithmDetails] instance.
 * @return [AlgorithmDetails] instance with a non-nullable fields.
 */
fun AlgorithmDetailsDTO.toAlgorithmDetails(): AlgorithmDetails {
    return AlgorithmDetails(
        result = result ?: AlgorithmDetailsResult(),
        errors = errors ?: ""
    )
}
//endregion


//region DATABASE
// GET A LIST OF NAMES

/**
 * Turns [AlgorithmInfoEntity] from local DB into an [Algorithm] instance.
 * @return [Algorithm] instance with non-nullable fields.
 */
fun AlgorithmInfoEntity.toAlgorithm(): Algorithm {
    return Algorithm(
        name = name,
        title = title,
        isDownloaded = true
    )
}


// GET DETAILS

/**
 * Turns [AlgorithmEntity] from the local DB into an [AlgorithmDetails] instance.
 * @return [AlgorithmDetails] instance with a non-nullable fields.
 * The result field is automatically parsed from the [AlgorithmEntity] data into [AlgorithmDetailsResult] instance.
 */
fun AlgorithmEntity.toAlgorithmDetails(): AlgorithmDetails {
    return AlgorithmDetails(
        result = AlgorithmDetailsResult(
            name = algorithm.name,
            title = algorithm.title,
            description = algorithm.description,
            parameters = parameters.filter { it.isInput }.map { it.toDataElement() },
            outputs = outputs.filter { !it.isInput }.map { it.toDataElement() }
        ),
        errors = ""
    )
}

/**
 * Turns [AlgorithmDataEntity] from the local DB into [DataElement] instance.
 * @return [DataElement] instance with a non-nullable fields.
 */
fun AlgorithmDataEntity.toDataElement(): DataElement {
    return DataElement(
        name = fieldName,
        title = title,
        description = description,

        dataShape = dataShape,
        dataType = dataType,
        defaultValue = Gson().fromJson(defaultValue, Any::class.java) // PASSING STRING!!! NOT ANY?
    )
}

// WRITE ENTITY FROM DETAILS
/**
 * Turns [AlgorithmDetailsResult] into an [AlgorithmInfoEntity] instance.
 * @return [AlgorithmInfoEntity] instance with a non-nullable fields.
 */
fun AlgorithmDetailsResult.toAlgorithmInfoEntity(): AlgorithmInfoEntity {
    return AlgorithmInfoEntity(
        name = name,
        title = title,
        description = description
    )
}

/**
 * Turns [AlgorithmDetailsResult] into a list of parameters'
 * and outputs' [AlgorithmDataEntity] instances.
 * Sets isInput field of each parameter's [DataElement] instance to *true*
 * to separate parameter entities and output entities.
 * @return List of AlgorithmDataEntity with a non-nullable fields.
 * The result field is automatically create [AlgorithmDetailsResult] instance from the [AlgorithmEntity] data.
 */
fun AlgorithmDetailsResult.toAlgorithmDataEntityList(): List<AlgorithmDataEntity> {
    val parameterEntities =
        parameters.map { it.toAlgorithmDataEntity(algorithmName = name, isInput = true) }
    val outputEntities = outputs.map { it.toAlgorithmDataEntity(algorithmName = name) }
    return parameterEntities + outputEntities
}

/**
 * Turns [DataElement] into an [AlgorithmDataEntity] instance.
 * @param algorithmName The name of the algorithm this field element belongs to.
 * @param isInput Sets to *true* if it is an input parameter. It's necessary for further storage in the local DB.
 * @return [AlgorithmDataEntity] instance with a non-nullable fields.
 */
fun DataElement.toAlgorithmDataEntity(
    algorithmName: String,
    isInput: Boolean = false
): AlgorithmDataEntity {
    return AlgorithmDataEntity(
        algorithmName = algorithmName,
        isInput = isInput,
        fieldName = name,
        title = title,
        description = description,
        dataShape = dataShape,
        dataType = dataType,
        defaultValue = Gson().toJson(defaultValue)
    )
}
//endregion
