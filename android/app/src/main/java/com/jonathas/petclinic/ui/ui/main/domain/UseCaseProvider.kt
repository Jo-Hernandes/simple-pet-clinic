package com.jonathas.petclinic.ui.ui.main.domain

import com.jonathas.httpservice.restRepository.PetClinicRepository
import com.jonathas.petclinic.ui.ui.main.usecases.FetchPetListUseCase
import com.jonathas.petclinic.ui.ui.main.usecases.FetchSettingsUseCase

class UseCaseProvider(
    dataSource: PetClinicRepository
) {

    val fetchPetsUseCase: FetchPetListUseCase by lazy { FetchPetListUseCase(dataSource) }
    val fetchSettingsUseCase: FetchSettingsUseCase by lazy { FetchSettingsUseCase(dataSource) }
}
