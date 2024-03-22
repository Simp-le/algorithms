package com.android.algorithms.data.remote

import com.android.algorithms.BuildConfig
import com.android.algorithms.data.model.details.data.DataValueList
import com.android.algorithms.data.remote.dto.AlgorithmDetailsDTO
import com.android.algorithms.data.remote.dto.AlgorithmListDTO
import com.android.algorithms.data.remote.dto.AlgorithmResponseDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Interface defining the API endpoints for interacting with algorithms.
 */
interface AlgorithmApi {
    companion object {
        const val BASE_URL = BuildConfig.BASE_URL
    }

    /**
     * Retrieves a list of algorithms main info.
     *
     * @return AlgorithmListDTO representing the list of algorithms.
     */
    @GET("algorithms")
    suspend fun getAlgorithms(): AlgorithmListDTO

    /**
     * Retrieves details of a specific algorithm by its name.
     *
     * @param algorithmName The name of the algorithm to retrieve details for.
     * @return AlgorithmDetailsDTO representing the details of the algorithm.
     */
    @GET("algorithms/{algorithm_name}")
    suspend fun getAlgorithmDetails(
        @Path("algorithm_name") algorithmName: String
    ): AlgorithmDetailsDTO

    /**
     * Executes an algorithm with the provided parameters and retrieves the result.
     *
     * @param algorithmName The name of the algorithm to execute.
     * @param parameters The parameters for the algorithm execution.
     * @return AlgorithmResponseDTO representing the response from executing the algorithm.
     */
    @POST("algorithms/{algorithm_name}")
    suspend fun getAlgorithmResult(
        @Path("algorithm_name") algorithmName: String,
        @Body parameters: DataValueList
    ): AlgorithmResponseDTO
}
