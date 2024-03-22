package com.android.algorithms

import com.android.algorithms.data.local.AlgorithmDataEntity
import com.android.algorithms.data.local.AlgorithmEntity
import com.android.algorithms.data.local.AlgorithmInfoEntity
import com.android.algorithms.data.model.Algorithm
import com.android.algorithms.data.model.details.AlgorithmDetails
import com.android.algorithms.data.model.details.AlgorithmDetailsResult
import com.android.algorithms.data.model.details.data.DataElement
import com.android.algorithms.data.model.details.response.AlgorithmOutputs
import com.android.algorithms.data.model.details.response.AlgorithmResponse
import com.android.algorithms.data.remote.dto.AlgorithmDetailsDTO
import com.android.algorithms.data.remote.dto.AlgorithmResponseDTO
import com.android.algorithms.data.toAlgorithm
import com.android.algorithms.data.toAlgorithmDataEntity
import com.android.algorithms.data.toAlgorithmDetails
import com.android.algorithms.data.toAlgorithmResponse
import com.google.gson.Gson
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapperTests {
    @Test
    fun testAlgorithmResponseDTOtoAlgorithmResponse() {
        val dto = AlgorithmResponseDTO(result = AlgorithmOutputs(), errors = "")

        val result = dto.toAlgorithmResponse()

        assertEquals(AlgorithmResponse(AlgorithmOutputs(), ""), result)
    }

    @Test
    fun testAlgorithmDetailsDTOtoAlgorithmDetails() {
        val dto = AlgorithmDetailsDTO(result = AlgorithmDetailsResult(), errors = "")

        val result = dto.toAlgorithmDetails()

        assertEquals(AlgorithmDetails(AlgorithmDetailsResult(), ""), result)
    }

    @Test
    fun testAlgorithmInfoEntityToAlgorithm() {
        val entity = AlgorithmInfoEntity(name = "Test", title = "Test Title", description = "Test Description")

        val result = entity.toAlgorithm()

        assertEquals(Algorithm("Test", "Test Title", true), result)
    }

    @Test
    fun testAlgorithmEntityToAlgorithmDetails() {
        val entity = AlgorithmEntity(
            algorithm = AlgorithmInfoEntity(name = "Test", title = "Test Title", description = "Test Description"),
            parameters = emptyList(),
            outputs = emptyList()
        )

        val result = entity.toAlgorithmDetails()

        assertEquals(AlgorithmDetails(AlgorithmDetailsResult("Test", "Test Title", "Test Description", emptyList(), emptyList()), ""), result)
    }

    @Test
    fun testDataElementToAlgorithmDataEntity() {
        val dataElement = DataElement(
            name = "TestName",
            title = "Test Title",
            description = "Test Description",
            dataShape = "Test Shape",
            dataType = "Test Type",
            defaultValue = "Test Default"
        )

        val result = dataElement.toAlgorithmDataEntity("TestAlgorithm", isInput = true)

        val expected = AlgorithmDataEntity(
            algorithmName = "TestAlgorithm",
            isInput = true,
            fieldName = "TestName",
            title = "Test Title",
            description = "Test Description",
            dataShape = "Test Shape",
            dataType = "Test Type",
            defaultValue = Gson().toJson("Test Default")
        )
        assertEquals(expected, result)
    }
}