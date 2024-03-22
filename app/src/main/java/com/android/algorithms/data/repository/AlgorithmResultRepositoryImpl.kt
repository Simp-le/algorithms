package com.android.algorithms.data.repository

import android.content.Context
import android.net.ConnectivityManager
import com.android.algorithms.data.Result
import com.android.algorithms.data.model.details.data.DataValue
import com.android.algorithms.data.model.details.data.DataValueList
import com.android.algorithms.data.remote.AlgorithmApi
import com.android.algorithms.data.toAlgorithmResponse
import com.chaquo.python.PyException
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Implementation of the AlgorithmResultRepository.
 *
 * @property algorithmApi The Retrofit service for accessing algorithm-related API endpoints.
 * @property connectivityManager The system service for managing network connectivity.
 * @property context The application context.
 * @constructor Creates an AlgorithmResultRepositoryImpl.
 */
class AlgorithmResultRepositoryImpl @Inject constructor(
    private val algorithmApi: AlgorithmApi,
    private val connectivityManager: ConnectivityManager,
    private val context: Context
) : AlgorithmResultRepository {

    /**
     * Retrieves the result of executing a specific algorithm with the given parameters.
     * Priority for execution on the server.
     *
     * @param algorithmName The name of the algorithm.
     * @param parameters The parameters for the algorithm execution.
     * @return A Flow-emitting Result<List<DataValue>> representing the result of the operation.
     */
    override suspend fun getAlgorithmResult(
        algorithmName: String, parameters: DataValueList
    ): Flow<Result<List<DataValue>>> {
        return flow {
            emit(Result.Loading(true))

            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            try {
                if (capabilities != null) {
                    val algorithmResultFromApi =
                        algorithmApi.getAlgorithmResult(algorithmName, parameters)
                    val response = algorithmResultFromApi.toAlgorithmResponse()
                    if (response.errors == "") emit(Result.Success(response.result.outputs))
                    else emit(
                        Result.Error(
                            message = response.errors, data = response.result.outputs
                        )
                    )
                } else {
                    val algorithmResultFromLocal =
                        runPythonScript(algorithmName, parameters.parameters)
                    emit(Result.Success(algorithmResultFromLocal))
                }
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Result.Error(message = "IOException: getting result"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Result.Error(message = "HttpException: getting result"))
            } catch (e: PyException) {
                e.printStackTrace()
                emit(Result.Error(message = "PyException: getting result. ${e.message}"))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(message = "Undefined Exception: getting result. ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }

    /**
     * Runs a Python script and returns the result.
     *
     * @param algorithmName The name algorithm to run the Python script.
     * @param inputParameters The input parameters for the algorithm.
     * @return The result of the algorithm.
     */
    private suspend fun runPythonScript(
        algorithmName: String, inputParameters: List<DataValue>
    ): List<DataValue> {
        //region module
        // TODO: search for org.python.util.PythonInterpreter
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(context))
        }
        val py = Python.getInstance()

        // Get folder with algorithm files visible for the library
        val sys = py.getModule("sys")
        val pythonScriptsPath = context.filesDir.path + "/python_scripts"
        if (!(sys["path"].toString().contains(pythonScriptsPath))) {
            sys["path"]?.callAttr("append", pythonScriptsPath)
        }

        val module = py.getModule(algorithmName)

        val mainFunction = module["main"]
        //endregion

        var answer: Any? = null

        // executing func.func_code.co_varnames[:func.func_code.co_argcount] to get function's parameters
        // could also use [name for name in func.__annotations__.keys()] or
        // [param.name for param in inspect.signature(func).parameters.values()]
        val mainFunctionCode = mainFunction?.get("__code__")
        val argNumber = mainFunctionCode?.get("co_argcount")?.toInt() ?: 0
        val paramNames = mainFunctionCode?.get("co_varnames")?.asList()?.subList(0, argNumber)

        val paramValues = mutableListOf<Any?>()
        paramNames?.forEach { paramName ->
            val dataValue = inputParameters.find { it.name == paramName.toString() }
            dataValue?.let {
                paramValues.add(convertToPythonFormat(py.builtins, dataValue.value))
            }
        }

        answer = withTimeoutOrNull(3000L) {
            withContext(Dispatchers.Default) {
                return@withContext mainFunction?.call(*paramValues.toTypedArray())
            }
        } ?: throw Exception("Execution timed out")

        answer = Gson().fromJson(answer.toString(), Any::class.java) as Map<*, *>

        val dataList = mutableListOf<DataValue>()
        answer.entries.forEach { entry ->
            val name = entry.key.toString()
            val value = entry.value
            dataList.add(DataValue(name, value ?: ""))
        }

        return dataList
    }

    /**
     * Converts a Kotlin value to a Python-compatible format.
     *
     * @param builtin The Python built-in module.
     * @param value The value to convert.
     * @return The converted value.
     */
    private fun convertToPythonFormat(builtin: PyObject, value: Any?): Any? {
        return when (value) {
            is List<*> -> builtin.callAttr(
                "list", value.map { convertToPythonFormat(builtin, it) }.toTypedArray()
            )

            else -> value // If not a list
        }
    }
}

