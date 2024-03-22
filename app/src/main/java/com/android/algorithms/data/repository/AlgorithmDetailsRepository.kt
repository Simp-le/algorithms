package com.android.algorithms.data.repository

import com.android.algorithms.data.Result
import com.android.algorithms.data.model.details.AlgorithmDetailsResult
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for handling algorithm details.
 */
interface AlgorithmDetailsRepository {
    /**
     * Retrieves details of a specific algorithm.
     *
     * @param algorithmName The name of the algorithm to retrieve details for.
     * @return A Flow-emitting Result<AlgorithmDetailsResult> representing the result of the operation.
     */
    suspend fun getAlgorithmDetails(algorithmName: String): Flow<Result<AlgorithmDetailsResult>>

    /**
     * Downloads details of a specific algorithm.
     *
     * @param algorithmName The name of the algorithm to download details for.
     * @return A Flow-emitting Result<String> representing the result of the operation.
     */
    suspend fun downloadAlgorithmDetails(algorithmName: String): Flow<Result<String>>

    /**
     * Deletes details of a specific algorithm.
     *
     * @param algorithmName The name of the algorithm to delete details for.
     * @return A Flow-emitting Result<String> representing the result of the operation.
     */
    suspend fun deleteAlgorithmDetails(algorithmName: String): Flow<Result<String>>
}