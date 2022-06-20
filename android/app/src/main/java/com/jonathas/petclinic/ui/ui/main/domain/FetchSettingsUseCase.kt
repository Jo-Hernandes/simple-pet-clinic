package com.jonathas.petclinic.ui.ui.main.domain

import com.jonathas.httpservice.model.SettingsModel
import com.jonathas.httpservice.restRepository.PetClinicRepository
import com.jonathas.petclinic.models.CurrentSettingsModel

class FetchSettingsUseCase(
    private val dataSource: PetClinicRepository
) {

    operator fun invoke() =
        dataSource.fetchConfig()

    fun mapData(data : SettingsModel) = CurrentSettingsModel(
        showCallButton = data.isCallEnabled ?: false,
        showChatButton = data.isCallEnabled ?: false,
        bannerText = data.workHours
    )
}