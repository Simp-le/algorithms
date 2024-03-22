package com.android.algorithms.data.repository

import com.android.algorithms.data.Result
import com.android.algorithms.data.model.Algorithm
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for handling lists of algorithms.
 */
interface AlgorithmListRepository {
    /**
     * Retrieves a list of algorithms main information.
     *
     * @return A Flow-emitting Result<List<Algorithm>> representing the result of the operation.
     */
    suspend fun getAlgorithmList(): Flow<Result<List<Algorithm>>>
}