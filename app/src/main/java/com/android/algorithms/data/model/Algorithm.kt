package com.android.algorithms.data.model

/**
 * Represents an algorithm.
 *
 * @property name The unique name of the algorithm.
 * @property title The title of the algorithm.
 * @property isDownloaded Indicates whether the algorithm is downloaded. Default value is false.
 * @constructor Creates an Algorithm instance.
 */
data class Algorithm(
    val name: String,
    val title: String,
    val isDownloaded: Boolean = false
)