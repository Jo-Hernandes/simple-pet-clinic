package com.jonathas.petclinic.ui.ui.main.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jonathas.httpservice.ServiceModule
import com.jonathas.petclinic.ui.ui.main.MainViewModel
import com.jonathas.petclinic.utils.DefaultDispatcherProvider

class MainViewModelProvider : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val useCaseProvider = UseCaseProvider(ServiceModule.getRestServiceRepository())
        return MainViewModel(
            fetchPetListUseCase = useCaseProvider.fetchPetsUseCase,
            fetchSettingsUseCase = useCaseProvider.fetchSettingsUseCase,
            isOpenUseCase = IsOpenUseCase(),
            dispatchers = DefaultDispatcherProvider
        ) as T
    }
}