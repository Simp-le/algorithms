package com.android.algorithms

import android.app.Application
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.android.algorithms.data.Result
import com.android.algorithms.data.model.Algorithm
import com.android.algorithms.data.model.details.AlgorithmDetailsResult
import com.android.algorithms.data.repository.AlgorithmDetailsRepository
import com.android.algorithms.data.repository.AlgorithmListRepository
import com.android.algorithms.data.repository.AlgorithmResultRepository
import com.android.algorithms.viewmodel.AlgorithmDetailsState
import com.android.algorithms.viewmodel.AlgorithmDetailsViewModel
import com.android.algorithms.viewmodel.AlgorithmListViewModel
import junit.framework.Assert.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@RunWith(Suite::class)
@Suite.SuiteClasses(
    AlgorithmDetailsViewModelTest::class,
    AlgorithmListViewModelTest::class
)
class ViewModelTests

@RunWith(MockitoJUnitRunner::class)
class AlgorithmDetailsViewModelTest {

    @Mock
    lateinit var algorithmDetailsRepository: AlgorithmDetailsRepository

    @Mock
    lateinit var algorithmResultRepository: AlgorithmResultRepository

    @Mock
    lateinit var application: Application

    @Mock
    lateinit var savedStateHandle: SavedStateHandle

    @Mock
    lateinit var observer: Observer<AlgorithmDetailsState>

    private lateinit var viewModel: AlgorithmDetailsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = AlgorithmDetailsViewModel(
            algorithmDetailsRepository,
            algorithmResultRepository,
            application,
            savedStateHandle
        )
        viewModel.algorithmDetailsState
    }

    @Test
    fun `test getAlgorithmDetails when success`() = runBlocking {
        val algorithmName = "TestAlgorithm"
        val algorithmDetailsResult = AlgorithmDetailsResult()
        val flow = flow {
            emit(Result.Success(algorithmDetailsResult))
        }
        `when`(algorithmDetailsRepository.getAlgorithmDetails(algorithmName)).thenReturn(flow)

        verify(observer).onChanged(AlgorithmDetailsState(isLoading = true))
        verify(observer).onChanged(
            AlgorithmDetailsState(
                algorithmDetailsResult = algorithmDetailsResult,
                isLoading = false
            )
        )
    }

    @Test
    fun `test getAlgorithmDetails when error`() = runBlocking {
        val algorithmName = "TestAlgorithm"
        val errorMessage = "Error occurred"
        val flow = flow {
            emit(Result.Error<String>(message = errorMessage.toString()))
        }
        `when`(algorithmDetailsRepository.getAlgorithmDetails(algorithmName))

        verify(observer).onChanged(AlgorithmDetailsState(isLoading = true))
        verify(observer).onChanged(
            AlgorithmDetailsState(
                errorMessage = errorMessage,
                isLoading = false
            )
        )
    }

    @Test
    fun `test getAlgorithmDetails when loading`() = runBlocking {
        val algorithmName = "TestAlgorithm"
        val flow = flow {
            emit(Result.Loading<Boolean>(true))
        }
        `when`(algorithmDetailsRepository.getAlgorithmDetails(algorithmName))

        verify(observer).onChanged(AlgorithmDetailsState(isLoading = true))
    }
}



@RunWith(MockitoJUnitRunner::class)
class AlgorithmListViewModelTest {

    @Mock
    private lateinit var algorithmListRepository: AlgorithmListRepository

    private lateinit var viewModel: AlgorithmListViewModel

    @Before
    fun setup() {
        viewModel = AlgorithmListViewModel(algorithmListRepository)
    }

    @Test
    fun `test fetching algorithm list`() = runBlocking {
        val mockAlgorithmList = listOf(
            Algorithm(name = "Test1", title = "Test Algorithm 1", isDownloaded = true),
            Algorithm(name = "Test2", title = "Test Algorithm 2", isDownloaded = false)
        )
        `when`(algorithmListRepository.getAlgorithmList()).thenReturn(flowOf(Result.Success(mockAlgorithmList)))

        assertEquals(viewModel.algorithmListState.value.algorithmList, mockAlgorithmList)
        assertFalse(viewModel.algorithmListState.value.isLoading)
        assertNull(viewModel.algorithmListState.value.errorMessage)
    }
}