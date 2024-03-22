package com.android.algorithms.data.repository

import android.net.ConnectivityManager
import com.android.algorithms.data.Result
import com.android.algorithms.data.local.AlgorithmDatabase
import com.android.algorithms.data.model.Algorithm
import com.android.algorithms.data.remote.AlgorithmApi
import com.android.algorithms.data.toAlgorithm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Implementation of the AlgorithmListRepository.
 *
 * @property algorithmApi The Retrofit service for accessing algorithm-related API endpoints.
 * @property algorithmDatabase The Room database for storing algorithm-related data.
 * @property connectivityManager The system service for managing network connectivity.
 * @constructor Creates an AlgorithmListRepositoryImpl.
 */
class AlgorithmListRepositoryImpl @Inject constructor(
    private val algorithmApi: AlgorithmApi,
    private val algorithmDatabase: AlgorithmDatabase,
    private val connectivityManager: ConnectivityManager
) : AlgorithmListRepository {
    /**
     * Retrieves a list of algorithms.
     *
     * @return A Flow-emitting Result<List<Algorithm>> representing the result of the operation.
     */
    override suspend fun getAlgorithmList(): Flow<Result<List<Algorithm>>> {
        return flow {
            emit(Result.Loading(true))

            val localAlgorithmList = algorithmDatabase.algorithmDAO.getAlgorithmDeclarations()
                .map { algorithmDeclaration -> algorithmDeclaration.toAlgorithm() }

            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

            if (capabilities != null) {
                try {
                    val remoteAlgorithmList = algorithmApi.getAlgorithms().algorithms

                    // Merge local and remote lists
                    val combinedList = localAlgorithmList.toMutableList()
                    // Adding the rest from the api. So we don't duplicate algorithms
                    remoteAlgorithmList.forEach { remoteAlgorithm ->
                        if (!localAlgorithmList.any { it.name == remoteAlgorithm.name }) {
                            combinedList.add(remoteAlgorithm)
                        }
                    }
                    emit(Result.Success(combinedList))
                } catch (e: IOException) {
                    e.printStackTrace()
                    emit(Result.Error(message = "IOException: loading algorithms"))
                } catch (e: HttpException) {
                    e.printStackTrace()
                    emit(Result.Error(message = "HttpException: loading algorithms"))
                } catch (e: Exception) {
                    e.printStackTrace()
                    emit(Result.Error(message = "Undefined Exception: loading algorithms"))
                }
            } else {
                if (localAlgorithmList.isNotEmpty()) {
                    emit(Result.Success(localAlgorithmList))
                } else {
                    emit(Result.Error(message = "No internet connection and no local data available"))
                }
            }

            emit(Result.Loading(false))
        }
    }
}
