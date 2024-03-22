package com.android.algorithms.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.android.algorithms.BuildConfig
import com.android.algorithms.data.Result
import com.android.algorithms.data.local.AlgorithmDatabase
import com.android.algorithms.data.model.details.AlgorithmDetailsResult
import com.android.algorithms.data.remote.AlgorithmApi
import com.android.algorithms.data.toAlgorithmDataEntityList
import com.android.algorithms.data.toAlgorithmDetails
import com.android.algorithms.data.toAlgorithmInfoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import java.net.URL
import javax.inject.Inject

/**
 * Implementation of the AlgorithmDetailsRepository.
 *
 * @property algorithmApi The Retrofit service for accessing algorithm-related API endpoints.
 * @property algorithmDatabase The Room database for storing algorithm-related data.
 * @property connectivityManager The system service for managing network connectivity.
 * @property context The application context.
 * @constructor Creates an AlgorithmDetailsRepositoryImpl.
 */
class AlgorithmDetailsRepositoryImpl @Inject constructor(
    private val algorithmApi: AlgorithmApi,
    private val algorithmDatabase: AlgorithmDatabase,
    private val connectivityManager: ConnectivityManager,
    private val context: Context
) : AlgorithmDetailsRepository {

    /**
     * Retrieves details of a specific algorithm.
     * Priority for loading from the server with updating local saves.
     *
     * @param algorithmName The name of the algorithm to retrieve details for.
     * @return A Flow-emitting Result<AlgorithmDetailsResult> representing the result of the operation.
     */
    override suspend fun getAlgorithmDetails(algorithmName: String): Flow<Result<AlgorithmDetailsResult>> {
        return flow {
            emit(Result.Loading(true))

            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

            if (capabilities != null) {
                try {
                    val algorithmDetails =
                        algorithmApi.getAlgorithmDetails(algorithmName).toAlgorithmDetails()
                    if (algorithmDetails.errors == "") {
                        algorithmDatabase.algorithmDAO
                            .updateAlgorithmInfo(algorithmDetails.result.toAlgorithmInfoEntity())
                        algorithmDatabase.algorithmDAO
                            .updateAlgorithmData(algorithmDetails.result.toAlgorithmDataEntityList())
                        emit(Result.Success(algorithmDetails.result))
                    } else {
                        emit(
                            Result.Error(
                                message = algorithmDetails.errors, data = algorithmDetails.result
                            )
                        )
                    }
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
                val localAlgorithmDetails = algorithmDatabase.algorithmDAO
                    .getAlgorithmDetails(algorithmName)?.toAlgorithmDetails()?.result
                if (localAlgorithmDetails != null) {
                    emit(Result.Success(localAlgorithmDetails))
                } else {
                    emit(Result.Error(message = "Algorithm wasn't downloaded"))
                }

            }

            emit(Result.Loading(false))
        }
    }

    /**
     * Downloads a specific algorithm for further local use.
     *
     * @param algorithmName The name of the algorithm to download details for.
     * @return A Flow-emitting Result<String> representing the result of the operation.
     */
    override suspend fun downloadAlgorithmDetails(algorithmName: String): Flow<Result<String>> {
        return flow {
            emit(Result.Loading(true))

            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

            if (capabilities != null) {
                try {
                    val existingLocally =
                        algorithmDatabase.algorithmDAO.getAlgorithmDetails(algorithmName)
                    if (existingLocally != null) {
                        emit(Result.Error("Algorithm already downloaded."))
                    } else {
                        val algorithmDetails =
                            algorithmApi.getAlgorithmDetails(algorithmName).toAlgorithmDetails()
                        if (algorithmDetails.errors == "") {
                            algorithmDatabase.algorithmDAO
                                .insertAlgorithmInfo(algorithmDetails.result.toAlgorithmInfoEntity())
                            algorithmDatabase.algorithmDAO
                                .insertAlgorithmData(algorithmDetails.result.toAlgorithmDataEntityList())
                            downloadAlgorithm(algorithmName)
                            emit(Result.Success("Algorithm downloaded"))
                        } else {
                            emit(
                                Result.Error(
                                    message = algorithmDetails.errors, data = "Couldn't download"
                                )
                            )
                        }
                    }
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
            }
            emit(Result.Loading(false))
        }
    }

    /**
     * Downloads the algorithm file.
     *
     * @param algorithmName The name of the algorithm to download.
     */
    private suspend fun downloadAlgorithm(algorithmName: String) {
        val url = BuildConfig.BASE_URL + "algorithms/$algorithmName/download"

        val destinationFolder = File(context.filesDir, "python_scripts")
        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs()
        }

        val destinationFile = File(destinationFolder, "$algorithmName.py")
        if (!destinationFile.exists()) {
            try {
                downloadFile(url, destinationFile)
            } catch (e: Exception) {
                Log.e("Download", "Error downloading and saving file: ${e.message}", e)
            }
        } else {
            Log.d("Download", "File already exists")
        }
    }

    /**
     * Downloads a file from the specified URL and saves it to the target file.
     *
     * @param url The URL of the file to download.
     * @param targetFile The target file to save the downloaded file.
     * @throws IOException If an I/O error occurs during the download.
     */
    private suspend fun downloadFile(url: String, targetFile: File) {
        withContext(Dispatchers.IO) {
            try {
                val urlConnection = URL(url).openConnection()
                urlConnection.connect()
                urlConnection.getInputStream().use { input ->
                    targetFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                Log.d("Download", "File downloaded successfully")
            } catch (e: Exception) {
                Log.e("Download", "Error downloading file: ${e.message}", e)
            }
        }
    }

    /**
     * Deletes details of a specific algorithm from local storage.
     *
     * @param algorithmName The name of the algorithm to delete details for.
     * @return A Flow-emitting Result<String> representing the result of the operation.
     */
    override suspend fun deleteAlgorithmDetails(algorithmName: String): Flow<Result<String>> {
        return flow {
            emit(Result.Loading(true))

            val localAlgorithmDetails = algorithmDatabase.algorithmDAO
                .getAlgorithmDetails(algorithmName)
            if (localAlgorithmDetails != null) {
                deleteFilesByName(algorithmName)
                algorithmDatabase.algorithmDAO.deleteAlgorithmInfo(localAlgorithmDetails.algorithm)
                algorithmDatabase.algorithmDAO
                    .deleteAlgorithmData(localAlgorithmDetails.parameters + localAlgorithmDetails.outputs)
                emit(Result.Success("${localAlgorithmDetails.algorithm.name} deleted successfully"))
            } else {
                emit(Result.Error(message = "Algorithm isn't downloaded"))
            }


            emit(Result.Loading(false))
        }
    }

    /**
     * Deletes files associated with a specific algorithm.
     *
     * @param fileName The name of the algorithm file.
     */
    private fun deleteFilesByName(fileName: String) {
        val fileDir = File(context.filesDir, "python_scripts")
        val cacheDir = File(fileDir, "__pycache__")

        // Deleting .py
        val pyFile = File(fileDir, "$fileName.py")
        if (pyFile.exists() && pyFile.isFile) {
            pyFile.delete()
            Log.d("Deleting .py script", "File ${fileName}.py deleted successfully.")
        } else {
            Log.d("Deleting .py script", "File ${fileName}.py not found.")
        }

        // Deleting .pyc from cache
        val pycFile = File(cacheDir, "$fileName.cpython-311.pyc")
        if (pycFile.exists() && pycFile.isFile) {
            pycFile.delete()
            Log.d("Deleting .pyc script", "File ${fileName}.cpython-311.pyc deleted successfully.")
        } else {
            Log.d("Deleting .pyc script", "File ${fileName}.cpython-311.pyc not found.")
        }
    }
}