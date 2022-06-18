package com.jonathas.httpservice.model

data class ApiResponse<T>(

    val data: T? = null,

    val error: ApiResponseError? = null

)

sealed class ApiResponseError() {

    object empty : ApiResponseError()

    object noConnection : ApiResponseError()

    object notFound : ApiResponseError()

    object internalServerError : ApiResponseError()

    object unknown : ApiResponseError()
}