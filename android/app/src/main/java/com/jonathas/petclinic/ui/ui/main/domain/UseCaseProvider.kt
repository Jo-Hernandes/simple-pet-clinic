package com.jonathas.petclinic.ui.ui.main.domain

import com.jonathas.httpservice.restRepository.PetClinicRepository

class UseCaseProvider(
    dataSource: PetClinicRepository
) {

    val fetchPetsUseCase: FetchPetListUseCase by lazy { FetchPetListUseCase(dataSource) }
    val fetchSettingsUseCase: FetchSettingsUseCase by lazy { FetchSettingsUseCase(dataSource) }

}