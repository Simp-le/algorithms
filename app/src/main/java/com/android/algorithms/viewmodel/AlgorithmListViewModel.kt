package com.android.algorithms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.algorithms.data.Result
import com.android.algorithms.data.repository.AlgorithmListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the algorithm list screen.
 *
 * @property algorithmListRepository The repository to interact with algorithm list data.
 */
@HiltViewModel
class AlgorithmListViewModel @Inject constructor(
    private val algorithmListRepository: AlgorithmListRepository
) : ViewModel() {

    private var _algorithmListState = MutableStateFlow(AlgorithmListState())
    val algorithmListState = _algorithmListState.asStateFlow()

    init {
        getAlgorithmList()
    }

    /**
     * Fetches the algorithm list.
     */
    private fun getAlgorithmList() {
        viewModelScope.launch {
            _algorithmListState.update { it.copy(isLoading = true) }
            algorithmListRepository.getAlgorithmList().collectLatest { result ->
                when (result) {
                    is Result.Error -> {
                        result.message?.let { errorMessage ->
                            _algorithmListState.update {
                                it.copy(
                                    errorMessage = errorMessage
                                )
                            }
                        }
                        _algorithmListState.update { it.copy(isLoading = false) }
                    }

                    is Result.Success -> {
                        result.data?.let { algorithmList ->
                            _algorithmListState.update {
                                it.copy(algorithmList = algorithmList, errorMessage = "")
                            }
                        }
                    }

                    is Result.Loading -> {
                        _algorithmListState.update { it.copy(isLoading = result.isLoading) }
                    }
                }
            }
        }
    }


    /**
     * Public method for updating algorithm list page.
     */
    fun update() {
        getAlgorithmList()
    }
}