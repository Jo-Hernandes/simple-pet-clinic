package com.jonathas.petclinic

import com.jonathas.httpservice.model.ApiResponse
import com.jonathas.httpservice.model.SettingsModel
import com.jonathas.httpservice.restRepository.PetClinicRepository
import com.jonathas.petclinic.ui.ui.main.usecases.FetchSettingsUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.*
import org.junit.Assert.*

class FetchSettingsUseCaseTest {

    lateinit var useCase: FetchSettingsUseCase

    @MockK
    lateinit var dataSource: PetClinicRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        useCase = FetchSettingsUseCase(dataSource)
    }

    @Test
    fun `when invoke, use the data source to fetch settings data`() {
        every { dataSource.fetchConfig() } returns ApiResponse()
        useCase()
        verify { dataSource.fetchConfig() }
    }

    @Test
    fun `when invoke use case, return data based on datasource`() {
        val apiResponse = ApiResponse(
            data = SettingsModel(
                isCallEnabled = true,
                isChatEnabled = true,
                "workhours"
            )
        )

        every { dataSource.fetchConfig() } returns apiResponse

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
            data = SettingsModel(
                isCallEnabled = true,
                isChatEnabled = true,
                "workhours"
            )
        )

        every { dataSource.fetchConfig() } returns apiResponse

        val apiData = useCase().data ?: SettingsModel()
        val mappedData = useCase.mapData(apiData)

        assertEquals(
            "the api and mapped chat enable data should be the same",
            apiData.isChatEnabled,
            mappedData.showChatButton
        )

        assertEquals(
            "the api and mapped call enable data should be the same",
            apiData.isCallEnabled,
            mappedData.showCallButton
        )

        assertEquals(
            "the api and mapped workhours data should be the same",
            apiData.workHours,
            mappedData.bannerText
        )
    }
}
