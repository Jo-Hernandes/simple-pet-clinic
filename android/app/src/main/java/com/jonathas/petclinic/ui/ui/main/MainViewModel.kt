package com.jonathas.petclinic.ui.ui.main

import androidx.lifecycle.*
import com.jonathas.httpservice.model.ApiResponseError
import com.jonathas.httpservice.model.PetModel
import com.jonathas.httpservice.model.SettingsModel
import com.jonathas.petclinic.models.PetItemModel
import com.jonathas.petclinic.models.CurrentSettingsModel
import com.jonathas.petclinic.ui.ui.error.SettingsErrorHandler
import com.jonathas.petclinic.ui.ui.main.domain.ErrorToMessageMapper
import com.jonathas.petclinic.ui.ui.main.domain.FetchPetListUseCase
import com.jonathas.petclinic.ui.ui.main.domain.FetchSettingsUseCase
import com.jonathas.petclinic.ui.ui.main.domain.IsOpenUseCase
import com.jonathas.petclinic.utils.DispatcherProvider
import com.jonathas.petclinic.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime

class MainViewModel(
    private val fetchPetListUseCase: FetchPetListUseCase,
    private val fetchSettingsUseCase: FetchSettingsUseCase,
    private val isOpenUseCase: IsOpenUseCase,
    private val dispatchers: DispatcherProvider
) : ViewModel(), SettingsErrorHandler {

    private val _screenEvent: SingleLiveEvent<MainScreenEvent> = SingleLiveEvent()
    val screenEvent: LiveData<MainScreenEvent> get() = _screenEvent

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _petList: MutableLiveData<List<PetModel>> = MutableLiveData()
    val petList: LiveData<List<PetItemModel>>
        get() = Transformations.map(_petList, fetchPetListUseCase::mapData)

    private val _currentSettings: MutableLiveData<SettingsModel> =
        MutableLiveData()
    val currentSettings: LiveData<CurrentSettingsModel>
        get() = Transformations.map(_currentSettings, fetchSettingsUseCase::mapData)

    private val _showLoading: MutableLiveData<Boolean> = MutableLiveData()
    override val showLoading: LiveData<Boolean> get() = _showLoading

    fun loadPetList() = viewModelScope.launch {
        _isLoading.postValue(true)

        withContext(dispatchers.io()) {
            val response = fetchPetListUseCase()
            response.data?.let { _petList.postValue(it) }
            response.error?.let {
                _screenEvent.postValue(MainScreenEvent.ShowSnackBar(it.asMessage()))
            }
        }
        _isLoading.postValue(false)
    }


    fun handleButtonPress() = _screenEvent.postValue(
        MainScreenEvent.ShowCommunicationAlert(
            isOpenUseCase(
                LocalDate.now(),
                LocalTime.now()
            )
        )
    )

    fun handlePetSelected(pet: PetItemModel) =
        _screenEvent.postValue(MainScreenEvent.ShowWebContent(pet.contentUrl))

    suspend fun loadSettings(): Boolean = withContext(dispatchers.io()) {
        val response = fetchSettingsUseCase()
        response.data?.let {
            _currentSettings.postValue(it)
            isOpenUseCase.parseWorkHours(it.workHours)
        } ?: response.error?.let {
            _screenEvent.postValue(MainScreenEvent.ShowErrorScreen(it))
        }
        return@withContext response.data != null
    }

    override fun onRetryPressed() {
        _showLoading.postValue(true)
        viewModelScope.launch {
            if (loadSettings()) {
                _screenEvent.postValue(MainScreenEvent.DismissError)
            }
            _showLoading.postValue(false)
        }
    }
}

fun ApiResponseError.asMessage() = ErrorToMessageMapper.getMessage(this)

