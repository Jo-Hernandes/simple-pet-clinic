package com.jonathas.petclinic

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jonathas.httpservice.model.ApiResponse
import com.jonathas.httpservice.model.ApiResponseError
import com.jonathas.httpservice.model.PetModel
import com.jonathas.httpservice.model.SettingsModel
import com.jonathas.petclinic.models.CurrentSettingsModel
import com.jonathas.petclinic.models.PetItemModel
import com.jonathas.petclinic.ui.ui.main.MainScreenEvent
import com.jonathas.petclinic.ui.ui.main.MainViewModel
import com.jonathas.petclinic.ui.ui.main.domain.FetchPetListUseCase
import com.jonathas.petclinic.ui.ui.main.domain.FetchSettingsUseCase
import com.jonathas.petclinic.ui.ui.main.domain.IsOpenUseCase
import com.jonathas.petclinic.utils.DispatcherProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel

    @MockK
    lateinit var fetchPetListUseCase: FetchPetListUseCase

    @MockK
    lateinit var fetchSettingsUseCase: FetchSettingsUseCase

    @MockK
    lateinit var isOpenUseCase: IsOpenUseCase


    companion object {
        private val testDispatcher = UnconfinedTestDispatcher(TestCoroutineScheduler())

        object TestDispatcherProvider : DispatcherProvider {

            override fun main(): CoroutineDispatcher = testDispatcher
            override fun default(): CoroutineDispatcher = testDispatcher
            override fun io(): CoroutineDispatcher = testDispatcher
            override fun unconfined(): CoroutineDispatcher = testDispatcher
        }
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)

        viewModel = MainViewModel(
            fetchPetListUseCase = fetchPetListUseCase,
            fetchSettingsUseCase = fetchSettingsUseCase,
            isOpenUseCase = isOpenUseCase,
            dispatchers = TestDispatcherProvider
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `update petList when fetch pets is successful`() = runTest {
        val petModel = PetModel(
            contentUrl = "https://www.nyan.cat/",
            dateAdded = "today",
            imageUrl = "",
            title = "nyan cat"
        )
        val petItem = PetItemModel(
            imageUrl = "",
            contentUrl = "https://www.nyan.cat/",
            title = "nyan cat"
        )

        every { fetchPetListUseCase() } returns ApiResponse(data = listOf(petModel), error = null)
        every { fetchPetListUseCase.mapData(any()) } returns listOf(petItem)

        viewModel.loadPetList()
        viewModel.petList.observeForTesting {
            assertTrue("list should contain a item based on the fetch", it.isNotEmpty())
        }
    }

    @Test
    fun `if fetch pets usecase returns error, should show snackbar`() = runTest {
        every { fetchPetListUseCase() } returns ApiResponse(error = ApiResponseError.Unknown)

        viewModel.loadPetList()

        viewModel.screenEvent.observeForTesting {
            assertTrue(
                "viewmodel should trigger show snackbar event",
                it is MainScreenEvent.ShowSnackBar
            )
        }
    }

    @Test
    fun `on button press, should trigger alert event saying its negative when outside work hours`() {
        every { isOpenUseCase(any(), any()) } returns false

        viewModel.handleButtonPress()

        viewModel.screenEvent.observeForTesting {
            assertTrue(
                "should trigger alert",
                it is MainScreenEvent.ShowCommunicationAlert
            )
            assertEquals(
                "on after hours should return false",
                false,
                (it as MainScreenEvent.ShowCommunicationAlert).isWorkHour
            )
        }
    }

    @Test
    fun `on button press, should trigger alert event saying its positive when on working hours`() {
        every { isOpenUseCase(any(), any()) } returns true

        viewModel.handleButtonPress()

        viewModel.screenEvent.observeForTesting {
            assertTrue(
                "should trigger alert",
                it is MainScreenEvent.ShowCommunicationAlert
            )
            assertEquals(
                "on working hours should return true",
                true,
                (it as MainScreenEvent.ShowCommunicationAlert).isWorkHour
            )
        }
    }

    @Test
    fun `when user select pet, should trigger event to show content url`() {
        val petItem = PetItemModel(
            imageUrl = "",
            contentUrl = "https://www.nyan.cat/",
            title = "nyan cat"
        )

        viewModel.handlePetSelected(petItem)

        viewModel.screenEvent.observeForTesting {
            assertTrue(
                "should trigger show web screen",
                it is MainScreenEvent.ShowWebContent
            )

            assertEquals(
                "should open the correct content url",
                petItem.contentUrl,
                (it as MainScreenEvent.ShowWebContent).contentUrl
            )
        }
    }

    @Test
    fun `when fetching settings, set data on correct live data`() = runTest {
        val mockedSettings = CurrentSettingsModel(
            showCallButton = true, showChatButton = true, bannerText = ""
        )

        val mockedResponse = ApiResponse(data = SettingsModel())

        every { fetchSettingsUseCase() } returns mockedResponse
        every { fetchSettingsUseCase.mapData(any()) } returns mockedSettings

        viewModel.currentSettings.observeForever {
            assertEquals(
                "should return the same mapped response from fetch settings usecase",
                mockedSettings, it
            )
        }
    }

    @Test
    fun `when fetching settings data, should parse the time on the isOpenUseCase`() = runTest {
        val mockedSettings = CurrentSettingsModel(
            showCallButton = true, showChatButton = true, bannerText = ""
        )

        val apiResponse = ApiResponse(data = SettingsModel(workHours = "workhours parsing"))

        every { fetchSettingsUseCase() } returns apiResponse
        every { fetchSettingsUseCase.mapData(any()) } returns mockedSettings

        viewModel.loadSettings()
        verify { isOpenUseCase.parseWorkHours(apiResponse.data?.workHours ?: "") }
    }

    @Test
    fun `when fetching settings if error, should show error screen`() = runTest {
        val mockedSettings = CurrentSettingsModel(
            showCallButton = true, showChatButton = true, bannerText = ""
        )
        val apiError = ApiResponseError.Unknown
        every { fetchSettingsUseCase() } returns ApiResponse(error = apiError)
        every { fetchSettingsUseCase.mapData(any()) } returns mockedSettings

        viewModel.screenEvent.observeForTesting {
            assertTrue(
                "event should trigger show error screen",
                it is MainScreenEvent.ShowErrorScreen
            )

            assertEquals(
                "event show trigger error screen with correct error",
                apiError,
                (it as MainScreenEvent.ShowErrorScreen).apiError
            )
        }
    }

    @Test
    fun `when retry is successful should trigger to dismiss error screen`() = runTest {
        val mockedSettings = CurrentSettingsModel(
            showCallButton = true, showChatButton = true, bannerText = ""
        )

        val apiResponse = ApiResponse(data = SettingsModel(workHours = "workhours parsing"))

        every { fetchSettingsUseCase() } returns apiResponse
        every { fetchSettingsUseCase.mapData(any()) } returns mockedSettings

        viewModel.onRetryPressed()

        viewModel.screenEvent.observeForTesting {
            assertEquals(
                "if success on retry, should dismiss the error screen",
                MainScreenEvent.DismissError,
                it
            )
        }
    }

}