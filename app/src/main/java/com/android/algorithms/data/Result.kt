package com.android.algorithms.data

/**
 * A sealed class representing the result of an operation.
 *
 * @param data The result data.
 * @param message The message associated with the result, typically used for errors.
 */
sealed class Result<T>(
    val data: T? = null,
    val message: String? = null
) {
    /**
     * Represents a successful result.
     *
     * @param data The result data.
     */
    class Success<T>(data: T?) : Result<T>(data)

    /**
     * Represents an error result.
     *
     * @param message The error message.
     * @param data The result data, if any.
     */
    class Error<T>(message: String, data: T? = null) : Result<T>(data, message)

    /**
     * Represents a loading state.
     *
     * @param isLoading Indicates whether the operation is still loading.
     */
    class Loading<T>(val isLoading: Boolean = true) : Result<T>(null)
}