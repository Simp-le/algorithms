package com.android.algorithms.data.repository

import com.android.algorithms.data.Result
import com.android.algorithms.data.model.details.data.DataValue
import com.android.algorithms.data.model.details.data.DataValueList
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for handling algorithm results.
 */
interface AlgorithmResultRepository {
    /**
     * Retrieves the result of executing a specific algorithm with the given parameters.
     *
     * @param algorithmName The name of the algorithm.
     * @param parameters The parameters for the algorithm execution.
     * @return A Flow-emitting Result<List<DataValue>> representing the result of the operation.
     */
    suspend fun getAlgorithmResult(
        algorithmName: String,
        parameters: DataValueList
    ): Flow<Result<List<DataValue>>>
}