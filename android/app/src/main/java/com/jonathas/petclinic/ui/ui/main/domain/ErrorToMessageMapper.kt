package com.jonathas.petclinic.ui.ui.main.domain

import com.jonathas.httpservice.model.ApiResponseError
import com.jonathas.petclinic.R

object ErrorToMessageMapper {

    fun getMessage(error: ApiResponseError) =
        messageMap.getOrDefault(error, R.string.error_unknown)

    private val messageMap: HashMap<ApiResponseError, Int> = hashMapOf(
        ApiResponseError.Empty to R.string.error_empty,
        ApiResponseError.InternalServerError to R.string.error_server_error,
        ApiResponseError.NoConnection to R.string.error_no_connection,
        ApiResponseError.NotFound to R.string.error_not_found,
        ApiResponseError.Unknown to R.string.error_unknown
    )
}

fun ApiResponseError.asMessage() = ErrorToMessageMapper.getMessage(this)
