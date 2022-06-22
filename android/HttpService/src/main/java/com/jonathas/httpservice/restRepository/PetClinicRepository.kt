package com.jonathas.httpservice.restRepository

import com.jonathas.httpservice.model.ApiResponse
import com.jonathas.httpservice.model.PetModel
import com.jonathas.httpservice.model.SettingsModel

interface PetClinicRepository {

    fun fetchConfig(): ApiResponse<SettingsModel>

    fun fetchPetsData(): ApiResponse<List<PetModel>>
}
