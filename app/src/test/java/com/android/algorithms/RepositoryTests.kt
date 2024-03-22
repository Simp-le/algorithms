package com.android.algorithms

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.android.algorithms.data.Result
import com.android.algorithms.data.local.AlgorithmDAO
import com.android.algorithms.data.local.AlgorithmDatabase
import com.android.algorithms.data.local.AlgorithmInfoEntity
import com.android.algorithms.data.model.Algorithm
import com.android.algorithms.data.model.details.data.DataValue
import com.android.algorithms.data.model.details.data.DataValueList
import com.android.algorithms.data.model.details.response.AlgorithmOutputs
import com.android.algorithms.data.remote.AlgorithmApi
import com.android.algorithms.data.remote.dto.AlgorithmListDTO
import com.android.algorithms.data.remote.dto.AlgorithmResponseDTO
import com.android.algorithms.data.repository.AlgorithmListRepositoryImpl
import com.android.algorithms.data.repository.AlgorithmResultRepositoryImpl
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner


@RunWith(Suite::class)
@Suite.SuiteClasses(
    AlgorithmListRepositoryTests::class,
    AlgorithmListRepositoryImplTest::class,
    AlgorithmResultRepositoryImplTest::class
)
internal class RepositoryTests

@RunWith(MockitoJUnitRunner::class)
class AlgorithmListRepositoryTests {

    @Mock
    private lateinit var algorithmApi: AlgorithmApi

    @Mock
    private lateinit var algorithmDatabase: AlgorithmDatabase

    @Mock
    private lateinit var connectivityManager: ConnectivityManager

    private lateinit var repository: AlgorithmListRepositoryImpl

    @Before
    fun setUp() {
        repository = AlgorithmListRepositoryImpl(algorithmApi, algorithmDatabase, connectivityManager)
    }

    @Test
    fun `test retrieving algorithm list from local database`() = runBlocking {
        val algorithmDAO = mock(AlgorithmDAO::class.java)
        val localAlgorithmList = listOf(
            AlgorithmInfoEntity(name = "Test1", title = "Test Algorithm 1", description = "Test Description 1"),
            AlgorithmInfoEntity(name = "Test2", title = "Test Algorithm 2", description = "Test Description 2")
        )
        `when`(algorithmDatabase.algorithmDAO.getAlgorithmDeclarations()).thenReturn(localAlgorithmList)

        val result = repository.getAlgorithmList().first()

        assertTrue(result is Result.Success)
        assertEquals(localAlgorithmList, (result as Result.Success).data)
    }

    @Test
    fun `test retrieving algorithm list from API`() = runBlocking {
        val localAlgorithmList = emptyList<AlgorithmInfoEntity>()
        val remoteAlgorithmList = listOf(
            Algorithm(name = "Test1", title = "Test Algorithm 1", isDownloaded = true),
            Algorithm(name = "Test2", title = "Test Algorithm 2", isDownloaded = false)
        )
        `when`(algorithmDatabase.algorithmDAO.getAlgorithmDeclarations()).thenReturn(localAlgorithmList)
        `when`(algorithmApi.getAlgorithms()).thenReturn(AlgorithmListDTO(remoteAlgorithmList))

        val result = repository.getAlgorithmList().first()

        assertTrue(result is Result.Success)
        assertEquals(remoteAlgorithmList, (result as Result.Success).data)
    }

    @Test
    fun `test handling no internet connection and no local data`() = runBlocking {
        val localAlgorithmList = emptyList<AlgorithmInfoEntity>()
        `when`(algorithmDatabase.algorithmDAO.getAlgorithmDeclarations()).thenReturn(localAlgorithmList)
        `when`(connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)).thenReturn(null)

        val result = repository.getAlgorithmList().first()

        assertTrue(result is Result.Error)
        assertEquals("No internet connection and no local data available", (result as Result.Error).message)
    }
}



@RunWith(MockitoJUnitRunner::class)
class AlgorithmListRepositoryImplTest {

    @Mock
    private lateinit var algorithmApi: AlgorithmApi

    @Mock
    private lateinit var algorithmDatabase: AlgorithmDatabase

    @Mock
    private lateinit var connectivityManager: ConnectivityManager

    private lateinit var repository: AlgorithmListRepositoryImpl

    @Before
    fun setUp() {
        repository = AlgorithmListRepositoryImpl(algorithmApi, algorithmDatabase, connectivityManager)
    }

    @Test
    fun `test retrieving algorithm list from local database`() = runBlocking {
        val localAlgorithmList = listOf(
            AlgorithmInfoEntity(name = "Test1", title = "Test Algorithm 1", description = "Test Description 1"),
            AlgorithmInfoEntity(name = "Test2", title = "Test Algorithm 2", description = "Test Description 2")
        )
        `when`(algorithmDatabase.algorithmDAO.getAlgorithmDeclarations()).thenReturn(localAlgorithmList)

        val result = repository.getAlgorithmList().first()

        assertTrue(result is Result.Success)
        assertEquals(localAlgorithmList, (result as Result.Success).data)
    }

    @Test
    fun `test retrieving algorithm list from API`() = runBlocking {
        val localAlgorithmList = emptyList<AlgorithmInfoEntity>()
        val remoteAlgorithmList = listOf(
            Algorithm(name = "Test1", title = "Test Algorithm 1", isDownloaded = true),
            Algorithm(name = "Test2", title = "Test Algorithm 2", isDownloaded = false)
        )
        `when`(algorithmDatabase.algorithmDAO.getAlgorithmDeclarations()).thenReturn(localAlgorithmList)
        `when`(algorithmApi.getAlgorithms()).thenReturn(AlgorithmListDTO(remoteAlgorithmList))

        val result = repository.getAlgorithmList().first()

        assertTrue(result is Result.Success)
        assertEquals(remoteAlgorithmList, (result as Result.Success).data)
    }

    @Test
    fun `test handling no internet connection and no local data`() = runBlocking {
        val localAlgorithmList = emptyList<AlgorithmInfoEntity>()
        `when`(algorithmDatabase.algorithmDAO.getAlgorithmDeclarations()).thenReturn(localAlgorithmList)
        `when`(connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)).thenReturn(null)

        val result = repository.getAlgorithmList().first()

        assertTrue(result is Result.Error)
        assertEquals("No internet connection and no local data available", (result as Result.Error).message)
    }

    @Test
    fun `test merging local and remote algorithm lists`() = runBlocking {
        val localAlgorithmList = listOf(
            AlgorithmInfoEntity(name = "Test1", title = "Test Algorithm 1", description = "Test Description 1"),
            AlgorithmInfoEntity(name = "Test2", title = "Test Algorithm 2", description = "Test Description 2")
        )
        `when`(algorithmDatabase.algorithmDAO.getAlgorithmDeclarations()).thenReturn(localAlgorithmList)

        val remoteAlgorithmList = listOf(
            Algorithm(name = "Test3", title = "Test Algorithm 3", isDownloaded = false),
            Algorithm(name = "Test4", title = "Test Algorithm 4", isDownloaded = true)
        )
        `when`(algorithmApi.getAlgorithms()).thenReturn(AlgorithmListDTO(remoteAlgorithmList))

        val result = repository.getAlgorithmList().first()

        assertTrue(result is Result.Success)
        val combinedList = localAlgorithmList.toMutableList().apply {
            remoteAlgorithmList.forEach { remoteAlgorithm ->
                if (!localAlgorithmList.any { it.name == remoteAlgorithm.name }) {
                    add(remoteAlgorithm as AlgorithmInfoEntity)
                }
            }
        }
        assertEquals(combinedList, (result as Result.Success).data)
    }
}



@RunWith(MockitoJUnitRunner::class)
class AlgorithmResultRepositoryImplTest {

    @Mock
    private lateinit var algorithmApi: AlgorithmApi

    @Mock
    private lateinit var connectivityManager: ConnectivityManager

    @Mock
    private lateinit var context: Context

    private lateinit var repository: AlgorithmResultRepositoryImpl

    @Before
    fun setUp() {
        repository = AlgorithmResultRepositoryImpl(algorithmApi, connectivityManager, context)
    }

    @Test
    fun `test retrieving algorithm result from API`() = runBlocking {
        val algorithmName = "TestAlgorithm"
        val parameters = DataValueList(listOf(DataValue("param1", "value1"), DataValue("param2", "value2")))
        val apiResponse = AlgorithmResponseDTO(
            AlgorithmOutputs(outputs = listOf(DataValue("output1", "outputValue1"), DataValue("output2", "outputValue2"))),
            errors = ""
        )
        `when`(connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)).thenReturn(mock(NetworkCapabilities::class.java))
        `when`(algorithmApi.getAlgorithmResult(algorithmName, parameters)).thenReturn(apiResponse)

        val result = repository.getAlgorithmResult(algorithmName, parameters).first()

        assertTrue(result is Result.Success)
        val expectedOutput = listOf(DataValue("output1", "outputValue1"), DataValue("output2", "outputValue2"))
        assertEquals(expectedOutput, (result as Result.Success).data)
    }

    @Test
    fun `test running algorithm script locally`() = runBlocking {
        val algorithmName = "TestAlgorithm"
        val parameters = listOf(DataValue("param1", "value1"), DataValue("param2", "value2"))

        val result = repository.getAlgorithmResult(algorithmName, DataValueList(parameters)).first()

        assertTrue(result is Result.Success)
    }

    @Test
    fun `test handling exceptions during algorithm execution`() = runBlocking {
        val algorithmName = "TestAlgorithm"
        val parameters = DataValueList(listOf(DataValue("param1", "value1"), DataValue("param2", "value2")))
        `when`(connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)).thenReturn(null)

        val result = repository.getAlgorithmResult(algorithmName, parameters).first()

        assertTrue(result is Result.Error)
    }
}