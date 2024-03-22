package com.android.algorithms.viewmodel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.algorithms.data.Result
import com.android.algorithms.data.model.details.data.DataValue
import com.android.algorithms.data.model.details.data.DataValueList
import com.android.algorithms.data.repository.AlgorithmDetailsRepository
import com.android.algorithms.data.repository.AlgorithmResultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/**
 * ViewModel for managing algorithm details.
 *
 * @property algorithmDetailsRepository The repository for algorithm details.
 * @property algorithmResultRepository The repository for algorithm results.
 * @property application The application context.
 * @constructor Creates an AlgorithmDetailsViewModel.
 */
@HiltViewModel
class AlgorithmDetailsViewModel @Inject constructor(
    private val algorithmDetailsRepository: AlgorithmDetailsRepository,
    private val algorithmResultRepository: AlgorithmResultRepository,
    private val application: Application,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val algorithmName = savedStateHandle.get<String>("algorithmName") ?: "" // Not nullable
    private val appContext = application.applicationContext

    private var _algorithmDetailsState = MutableStateFlow(AlgorithmDetailsState()) // For writing
    val algorithmDetailsState = _algorithmDetailsState.asStateFlow() // read-only state flow

    init {
        val downloadedFile = File(appContext.filesDir.path + "/python_scripts", "$algorithmName.py")
        _algorithmDetailsState.update { it.copy(isDownloaded = downloadedFile.exists() && downloadedFile.isFile) }
        getAlgorithmDetails(algorithmName)
    }

    /**
     * Retrieves algorithm details and updates the state accordingly.
     *
     * @param algorithmName The name of the algorithm.
     */
    private fun getAlgorithmDetails(algorithmName: String) {
        viewModelScope.launch {
            _algorithmDetailsState.update { it.copy(isLoading = true) }

            algorithmDetailsRepository.getAlgorithmDetails(algorithmName).collectLatest { result ->
                when (result) {
                    is Result.Error -> {
                        result.message?.let { errorMessage ->
                            _algorithmDetailsState.update {
                                it.copy(
                                    errorMessage = errorMessage
                                )
                            }
                        }
                        _algorithmDetailsState.update { it.copy(isLoading = false) }
                    }

                    is Result.Loading -> {
                        _algorithmDetailsState.update { it.copy(isLoading = result.isLoading) }
                    }

                    is Result.Success -> {
                        result.data?.let { algorithmDetailsResult ->
                            _algorithmDetailsState.update { it.copy(algorithmDetailsResult = algorithmDetailsResult) }
                        }
                    }
                }
            }
        }
    }

    // Result
    /**
     * Retrieves algorithm result and updates the state accordingly.
     *
     * @param context The context.
     * @param algorithmName The name of the algorithm.
     * @param inputParameters The input parameters for the algorithm.
     */
    private fun getAlgorithmResult(
        context: Context,
        algorithmName: String,
        inputParameters: DataValueList
    ) {
        // view
        viewModelScope.launch {
            _algorithmDetailsState.update { it.copy(isLoading = true) }

            algorithmResultRepository.getAlgorithmResult(algorithmName, inputParameters)
                .collectLatest { result ->
                    when (result) {
                        is Result.Error -> {
                            result.message?.let { message ->
                                if (message != "") {
                                    showToast(context, message)
                                }
                            }
                            _algorithmDetailsState.update { it.copy(isLoading = false) }
                        }

                        is Result.Loading -> {
                            _algorithmDetailsState.update { it.copy(isLoading = result.isLoading) }
                        }

                        is Result.Success -> {
                            result.data?.let { resultList ->
                                if (resultList.isNotEmpty()) {
                                    resultList.forEach { fillElement(it) }
                                }
                            }
                        }
                    }
                }
        }
    }

    /**
     * Fills the data element with the provided data value.
     *
     * @param dataValue The data value to fill.
     */
    private fun fillElement(dataValue: DataValue) {
        val dataElement =
            algorithmDetailsState.value.algorithmDetailsResult.outputs.find { it.name == dataValue.name }
        if (dataElement != null) {
            dataElement.value = dataValue.value
        }
    }

    /**
     * Retrieves the input parameters.
     *
     * @return The list of input parameters.
     * @throws Exception if any field is empty.
     */
    private fun getInputParameters(): List<DataValue> {
        val parameters =
            algorithmDetailsState.value.algorithmDetailsResult.parameters.map { it.copy() }

        return if (parameters.any {
                it.value == null || it.value.toString().isEmpty()
            }) throw Exception("All fields should be filled")
        else parameters.map {
            it.value = parseDataFromString(it.value.toString(), it.dataShape, it.dataType)
            it.toDataValue()
        }
    }

    /**
     * Downloads the algorithm and updates the state accordingly.
     *
     * @param context The context.
     * @param algorithmName The name of the algorithm to download.
     */
    private fun downloadAlgorithm(context: Context, algorithmName: String) {
        viewModelScope.launch {
            _algorithmDetailsState.update { it.copy(isLoading = true) }

            algorithmDetailsRepository.downloadAlgorithmDetails(algorithmName)
                .collectLatest { result ->
                    when (result) {
                        is Result.Error -> {
                            result.message?.let { errorMessage ->
                                if (errorMessage.isNotEmpty()) showToast(context, errorMessage)
                            }
                            _algorithmDetailsState.update { it.copy(isLoading = false) }
                        }

                        is Result.Loading -> {
                            _algorithmDetailsState.update { it.copy(isLoading = result.isLoading) }
                        }

                        is Result.Success -> {
                            result.data?.let {
                                if (it.isNotEmpty()) {
                                    showToast(context, it)
                                    _algorithmDetailsState.update { state ->
                                        state.copy(
                                            isDownloaded = true,
                                            isChanged = !state.isChanged
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }

    /**
     * Deletes the algorithm and updates the state accordingly.
     *
     * @param context The context.
     * @param algorithmName The name of the algorithm to delete.
     */
    private fun deleteAlgorithm(context: Context, algorithmName: String) {
        viewModelScope.launch {
            _algorithmDetailsState.update { it.copy(isLoading = true) }

            algorithmDetailsRepository.deleteAlgorithmDetails(algorithmName)
                .collectLatest { result ->
                    when (result) {
                        is Result.Error -> {
                            result.message?.let { errorMessage ->
                                if (errorMessage.isNotEmpty()) showToast(context, errorMessage)
                            }
                            _algorithmDetailsState.update { it.copy(isLoading = false) }
                        }

                        is Result.Loading -> {
                            _algorithmDetailsState.update { it.copy(isLoading = result.isLoading) }
                        }

                        is Result.Success -> {
                            result.data?.let {
                                if (it.isNotEmpty()) {
                                    showToast(context, it)
                                    _algorithmDetailsState.update { state ->
                                        state.copy(
                                            isDownloaded = false,
                                            isChanged = !state.isChanged
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }

    /**
     * Handles the click event for executing.
     *
     * @param context The context.
     */
    fun buttonClick(context: Context) {
        try {
            getAlgorithmResult(
                context,
                algorithmName,
                DataValueList(parameters = getInputParameters())
            )
        } catch (e: Exception) {
            return showToast(context, e.message.toString())
        }
    }

    /**
     * Handles the click event for downloading algorithm.
     *
     * @param context The context.
     */
    fun actionButtonClick(context: Context) {
        downloadAlgorithm(context, algorithmName)
    }

    /**
     * Handles the click event for deleting algorithm.
     *
     * @param context The context.
     */
    fun actionButton2Click(context: Context) {
        deleteAlgorithm(context, algorithmName)
    }

    /**
     * Displays a toast message.
     *
     * @param context The context.
     * @param message The message to display.
     */
    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

//region Parsers
/**
 * Parses data from a string according to its shape and type.
 *
 * @param dataString The string containing the data.
 * @param dataShape The shape of the data (e.g., scalar, list, matrix).
 * @param dataType The type of the data (e.g., integer, float, string, boolean).
 * @return The parsed data.
 * @throws IllegalArgumentException if the data shape is unsupported.
 */
private fun parseDataFromString(dataString: String?, dataShape: String, dataType: String): Any {
    return when (dataShape) {
        "scalar" -> parseScalar(dataString, dataType)
        "list" -> parseList(dataString, dataType)
        "matrix" -> parseMatrix(dataString, dataType)
        else -> throw IllegalArgumentException("Unsupported dataShape: $dataShape")
    }
}

/**
 * Parses scalar data from a string.
 *
 * @param dataString The string containing the scalar data.
 * @param dataType The type of the data (e.g., integer, float, string, boolean).
 * @param noValueMessage The message to throw if the dataString is null or empty.
 * @return The parsed scalar data.
 * @throws Exception if the dataString is null or empty, or if parsing fails.
 */
private fun parseScalar(
    dataString: String?,
    dataType: String,
    noValueMessage: String = "No value"
): Any {
    if (dataString.isNullOrEmpty()) {
        throw Exception(noValueMessage)
    }

    val data = dataString.trim()
    return when (dataType) {
        "string" -> data.ifBlank { throw Exception(noValueMessage) }
        "int" -> {
            val rawNumber = data.toFloatOrNull()
            (if (rawNumber != null && rawNumber.rem(1) == 0f) rawNumber.toInt() else null)
                ?: throw Exception("Couldn't parse at \"${data}\"")
        }

        "float" -> data.toFloatOrNull() ?: throw Exception("Couldn't parse at \"${data}\"")
        "bool" -> data.toBooleanStrictOrNull()
            ?: throw Exception("Couldn't parse at \"${data}\"")

        else -> throw IllegalArgumentException("Unsupported dataType: $dataType")
    }
}

/**
 * Parses list data from a string.
 *
 * @param dataString The string containing the list data.
 * @param dataType The type of the data (e.g., integer, float, string, boolean).
 * @param noValueMessage The message to throw if the dataString is null, empty, or contains no values.
 * @return The parsed list data.
 * @throws Exception if the dataString is null, empty, contains no values, or parsing fails.
 */
private fun parseList(
    dataString: String?,
    dataType: String,
    noValueMessage: String = "No value"
): List<Any> {
    // Check if null or just blank
    if (dataString.isNullOrBlank()) {
        throw Exception(noValueMessage)
    }
    // Check if there are no values passed
    if (dataString.replace(",", "").isBlank()) {
        throw Exception(noValueMessage)
    }

    return dataString.trim().split(",").map {
        parseScalar(it, dataType, "Missing value")
    }
}

/**
 * Parses matrix data from a string.
 *
 * @param dataString The string containing the matrix data.
 * @param dataType The type of the data (e.g., integer, float, string, boolean).
 * @param noValueMessage The message to throw if the dataString is null, empty, or contains no values.
 * @return The parsed matrix data.
 * @throws Exception if the dataString is null, empty, contains no values, or parsing fails.
 */
private fun parseMatrix(
    dataString: String?,
    dataType: String,
    noValueMessage: String = "No value"
): Any {
    if (dataString.isNullOrBlank()) {
        throw Exception(noValueMessage)
    }
    if (dataString.replace("[,;]".toRegex(), "").isBlank()) {
        throw Exception(noValueMessage)
    }

    return dataString.trim().split(";").map {
        parseList(it, dataType, "Empty row")
    }
}
//endregion