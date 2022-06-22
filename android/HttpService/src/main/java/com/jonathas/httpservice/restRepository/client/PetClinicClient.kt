package com.jonathas.httpservice.restRepository.client

import com.jonathas.httpservice.model.ApiResponse
import com.jonathas.httpservice.model.PetModel
import com.jonathas.httpservice.model.SettingsModel
import com.jonathas.httpservice.restRepository.PetClinicRepository
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

@ExperimentalSerializationApi
class PetClinicClient : BaseClientBuilder<PetClinicRepository>() {

    override fun buildClient(): PetClinicRepository = object : PetClinicRepository {

        override fun fetchConfig(): ApiResponse<SettingsModel> =
            doRequest(buildGetRequest("settings")) { response ->
                val obj = Json.decodeFromString<SettingsModel>(response)
                ApiResponse(data = obj)
            }

        override fun fetchPetsData(): ApiResponse<List<PetModel>> =
            doRequest(buildGetRequest("pets")) { response ->
                val obj = Json.decodeFromString<List<PetModel>>(response)
                ApiResponse(data = obj)
            }
    }
}
