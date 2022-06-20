package com.jonathas.petclinic.ui.ui.main

import android.icu.util.Calendar
import androidx.lifecycle.*
import com.jonathas.httpservice.model.ApiResponseError
import com.jonathas.httpservice.model.PetModel
import com.jonathas.httpservice.model.SettingsModel
import com.jonathas.petclinic.R
import com.jonathas.petclinic.models.PetItemModel
import com.jonathas.petclinic.models.CurrentSettingsModel
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
) : ViewModel() {

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
    val showErrorSnackbar: LiveData<ApiResponseError> get() = _showErrorSnackbar

    private val _showAlert: SingleLiveEvent<Int> = SingleLiveEvent()
    val showAlert: LiveData<Int> get() = _showAlert

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

    suspend fun loadSettings(): Boolean {
        withContext(Dispatchers.IO) {
            val response = fetchSettingsUseCase()
            response.data?.let {
                _currentSettings.postValue(it)
                isOpenUseCase.parseWorkHours(it.workHours)
            }
        }
        return true
    }
}
