package com.jonathas.petclinic.ui.ui.main

import androidx.lifecycle.*
import com.jonathas.httpservice.model.ApiResponseError
import com.jonathas.httpservice.model.PetModel
import com.jonathas.httpservice.model.SettingsModel
import com.jonathas.petclinic.R
import com.jonathas.petclinic.models.PetItemModel
import com.jonathas.petclinic.models.CurrentSettingsModel
import com.jonathas.petclinic.ui.ui.error.SettingsErrorHandler
import com.jonathas.petclinic.ui.ui.main.domain.ErrorToMessageMapper
import com.jonathas.petclinic.ui.ui.main.domain.FetchPetListUseCase
import com.jonathas.petclinic.ui.ui.main.domain.FetchSettingsUseCase
import com.jonathas.petclinic.ui.ui.main.domain.IsOpenUseCase
import com.jonathas.petclinic.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime

class MainViewModel(
    private val fetchPetListUseCase: FetchPetListUseCase,
    private val fetchSettingsUseCase: FetchSettingsUseCase,
    private val isOpenUseCase: IsOpenUseCase
) : ViewModel(), SettingsErrorHandler {

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _petList: MutableLiveData<List<PetModel>> = MutableLiveData()
    val petList: LiveData<List<PetItemModel>>
        get() = Transformations.map(_petList, fetchPetListUseCase::mapData)

    private val _currentSettings: MutableLiveData<SettingsModel> =
        MutableLiveData()
    val currentSettings: LiveData<CurrentSettingsModel>
        get() = Transformations.map(_currentSettings, fetchSettingsUseCase::mapData)

    private val _showErrorSnackbar: SingleLiveEvent<ApiResponseError> = SingleLiveEvent()
    val showErrorSnackbar: LiveData<Int>
        get() = Transformations.map(_showErrorSnackbar) {
            ErrorToMessageMapper.getMessage(it)
        }

    private val _showAlert: SingleLiveEvent<Int> = SingleLiveEvent()
    val showAlert: LiveData<Int> get() = _showAlert

    private val _showErrorFragment: SingleLiveEvent<ApiResponseError> = SingleLiveEvent()
    val showErrorFragment: LiveData<Int>
        get() = Transformations.map(_showErrorFragment) {
            ErrorToMessageMapper.getMessage(it)
        }

    private val _showLoading: MutableLiveData<Boolean> = MutableLiveData()
    override val showLoading: LiveData<Boolean> get() = _showLoading

    private val _dismissError: SingleLiveEvent<Unit> = SingleLiveEvent()
    val dismissError: LiveData<Unit> get() = _dismissError

    fun loadPetList() {
        viewModelScope.launch {
            _isLoading.postValue(true)

            withContext(Dispatchers.IO) {
                val response = fetchPetListUseCase()
                response.data?.let { _petList.postValue(it) }
                response.error?.let { _showErrorSnackbar.postValue(it) }
            }

            _isLoading.postValue(false)
        }
    }

    fun handleButtonPress() {
        _showAlert.postValue(
            when (isOpenUseCase(LocalDate.now(), LocalTime.now())) {
                true -> R.string.button_open_text
                false -> R.string.button_closed_text
            }
        )
    }

    suspend fun loadSettings(): Boolean = withContext(Dispatchers.IO) {
        val response = fetchSettingsUseCase()
        response.data?.let {
            _currentSettings.postValue(it)
            isOpenUseCase.parseWorkHours(it.workHours)
        } ?: response.error?.let {
            _showErrorFragment.postValue(it)
        }
        return@withContext response.data != null
    }

    override fun onRetryPressed() {
        _showLoading.postValue(true)
        viewModelScope.launch {
            if (loadSettings()) {
                _dismissError.call()
            }
            _showLoading.postValue(false)
        }
    }
}
