package com.jonathas.httpservice.restRepository.client

import com.jonathas.httpservice.model.ApiResponse
import com.jonathas.httpservice.model.PetModel
import com.jonathas.httpservice.model.SettingsModel
import com.jonathas.httpservice.restRepository.PetClinicRepository
import okhttp3.Request

class PetClinicClient : BaseClientBuilder<PetClinicRepository>() {

    private fun buildGetRequest(path: String) = Request.Builder()
        .url("${baseUrl}/${path}")
        .header(CONTENT_TYPE_HEADER, CONTENT_JSON )
        .addHeader("Accept", "application/json")
        .build()

    override fun buildClient(): PetClinicRepository = object : PetClinicRepository {
        override fun fetchConfig(): ApiResponse<SettingsModel> {

            val request: Request = buildGetRequest("config")
            val response = httpClient.newCall(request).execute()

            return ApiResponse()

        }

        override fun fetchPetsData(): ApiResponse<List<PetModel>> {
            val request: Request = buildGetRequest("pets")
            val response = httpClient.newCall(request).execute()

            return ApiResponse()
        }

    }

}