package com.jonathas.httpservice.model

data class ApiResponse<T>(
    val data: T? = null,
    val error: ApiResponseError? = null
)

sealed class ApiResponseError {

    object Empty : ApiResponseError()

    object NoConnection : ApiResponseError()

    object NotFound : ApiResponseError()

    object InternalServerError : ApiResponseError()

    object Unknown : ApiResponseError()
}