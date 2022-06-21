package com.jonathas.httpservice.restRepository.client

import com.jonathas.httpservice.model.ApiResponse
import com.jonathas.httpservice.model.ApiResponseError
import com.jonathas.httpservice.model.PetModel
import com.jonathas.httpservice.model.SettingsModel
import com.jonathas.httpservice.restRepository.PetClinicRepository
import okhttp3.Request
import java.net.ConnectException

import kotlinx.serialization.*
import kotlinx.serialization.json.Json


@ExperimentalSerializationApi
class PetClinicClient : BaseClientBuilder<PetClinicRepository>() {

    private fun buildGetRequest(path: String) = Request.Builder()
        .url("${baseUrl}/${path}")
        .header(CONTENT_TYPE_HEADER, CONTENT_JSON)
        .build()

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

        private fun <T> doRequest(
            request: Request,
            onResult: (String) -> ApiResponse<T>
        ): ApiResponse<T> {
            return try {
                val response = httpClient.newCall(request).execute()
                val body = response.body?.string() ?: ""
                when {
                    response.code == 200 && body.isBlank() -> ApiResponse(error = ApiResponseError.Empty)
                    response.code == 404 -> ApiResponse(error = ApiResponseError.NotFound)
                    response.code == 500 -> ApiResponse(error = ApiResponseError.InternalServerError)
                    response.code == 200 -> onResult(body)
                    else -> ApiResponse(error = ApiResponseError.Unknown)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                when (e) {
                    is ConnectException -> ApiResponse(error = ApiResponseError.NoConnection)
                    else -> ApiResponse(error = ApiResponseError.Unknown)
                }
            }
        }
    }
}