package com.jonathas.petclinic.ui.ui.main

import androidx.lifecycle.*
import com.jonathas.httpservice.model.ApiResponseError
import com.jonathas.httpservice.model.PetModel
import com.jonathas.httpservice.model.SettingsModel
import com.jonathas.petclinic.models.PetItemModel
import com.jonathas.petclinic.models.CurrentSettingsModel
import com.jonathas.petclinic.ui.ui.main.domain.FetchPetListUseCase
import com.jonathas.petclinic.ui.ui.main.domain.FetchSettingsUseCase
import com.jonathas.petclinic.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val fetchPetListUseCase: FetchPetListUseCase,
    private val fetchSettingsUseCase: FetchSettingsUseCase
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

    suspend fun loadSettings(): Boolean {
        withContext(Dispatchers.IO) {
            val response = fetchSettingsUseCase()
            response.data?.let { _currentSettings.postValue(it) }
        }
        return true
    }
}
