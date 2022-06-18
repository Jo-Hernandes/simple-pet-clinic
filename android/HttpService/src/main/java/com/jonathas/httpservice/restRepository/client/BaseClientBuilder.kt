package com.jonathas.httpservice.restRepository.client

import com.jonathas.httpservice.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
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