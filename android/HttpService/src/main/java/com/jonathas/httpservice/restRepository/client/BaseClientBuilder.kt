package com.jonathas.httpservice.restRepository.client

import com.jonathas.httpservice.BuildConfig
import com.jonathas.httpservice.model.ApiResponse
import com.jonathas.httpservice.model.ApiResponseError
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.net.ConnectException
import java.util.concurrent.TimeUnit

abstract class BaseClientBuilder<T> {

    protected val baseUrl: String = BuildConfig.API_ADDRESS_URL
    protected val contentTypeHeader: String = CONTENT_TYPE_HEADER
    protected val contentJson: String = CONTENT_JSON

    abstract fun buildClient(): T

    protected val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(buildLoggingInterceptor())
            }

            addInterceptor(buildRetryInterceptor())

            connectTimeout(BuildConfig.REQUEST_TIMEOUT_S, TimeUnit.SECONDS)
            readTimeout(BuildConfig.REQUEST_TIMEOUT_S, TimeUnit.SECONDS)
            writeTimeout(BuildConfig.REQUEST_TIMEOUT_S, TimeUnit.SECONDS)

            followRedirects(false)
        }.build()
    }

    protected fun buildGetRequest(path: String) = Request.Builder()
        .url("$baseUrl/$path")
        .header(CONTENT_TYPE_HEADER, CONTENT_JSON)
        .build()

    protected fun <T> doRequest(
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

    private fun buildLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.HEADERS
    }

    private fun buildRetryInterceptor() = Interceptor { chain ->
        var ok = false
        var response: Response? = null
        var tryCount = 0
        var exception = IOException()
        while (!ok && tryCount < MAXIMUM_RETRIES)
            try {
                if (!ok) response?.close()

                response = chain.proceed(chain.request())
            } catch (e: IOException) {
                exception = e
            } finally {
                ok = response?.isSuccessful == true
                tryCount++
            }

        response ?: throw exception
    }

    companion object {
        private const val MAXIMUM_RETRIES = 3

        @JvmStatic
        protected val CONTENT_TYPE_HEADER = "Content-Type"

        @JvmStatic
        protected val CONTENT_JSON = "application/json"
    }
}
