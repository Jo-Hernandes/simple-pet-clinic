package com.jonathas.petclinic

import com.jonathas.httpservice.model.ApiResponse
import com.jonathas.httpservice.model.PetModel
import com.jonathas.httpservice.restRepository.PetClinicRepository
import com.jonathas.petclinic.ui.ui.main.domain.FetchPetListUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Assert.*
import org.junit.*

class FetchPetListUseCaseTest {

    lateinit var useCase: FetchPetListUseCase

    @MockK
    lateinit var dataSource: PetClinicRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        useCase = FetchPetListUseCase(dataSource)
    }

    @Test
    fun `when invoke, use the data source to fetch settings data`() {
        every { dataSource.fetchPetsData() } returns ApiResponse()
        useCase()
        verify { dataSource.fetchPetsData() }
    }

    @Test
    fun `when invoke use case, return data based on datasource`() {
        val apiResponse = ApiResponse(
            data = listOf(
                PetModel(
                    contentUrl = "https://www.nyan.cat/",
                    dateAdded = "today",
                    imageUrl = "",
                    title = "nyan cat"

                )
            )
        )

        every { dataSource.fetchPetsData() } returns apiResponse

        val useCaseResponse = useCase()

        assertEquals(
            "the api response should be the same as usecase response",
            apiResponse,
            useCaseResponse
        )
    }

    @Test
    fun `when mapping data, should map accordingly`() {
        val apiResponse = ApiResponse(
            data = listOf(
                PetModel(
                    contentUrl = "https://www.nyan.cat/",
                    dateAdded = "today",
                    imageUrl = "",
                    title = "nyan cat"

                )
            )
        )

        every { dataSource.fetchPetsData() } returns apiResponse

        val apiData = useCase().data ?: listOf()
        val mappedData = useCase.mapData(apiData)

        assertEquals(
            "the api and mapped items should contain the same content url",
            apiData.first().contentUrl,
            mappedData.first().contentUrl
        )

        assertEquals(
            "the api and mapped items should contain the same imageUrl url",
            apiData.first().imageUrl,
            mappedData.first().imageUrl
        )

        assertEquals(
            "the api and mapped items should contain the same title",
            apiData.first().title,
            mappedData.first().title
        )
    }


}