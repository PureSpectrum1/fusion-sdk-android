package com.purespectrum.fusionsdkandroid

import java.io.IOException
import retrofit2.HttpException

sealed class FusionError(open val message: String) {
    data class NetworkError(override val message: String) : FusionError(message)
    data class AuthenticationError(override val message: String) : FusionError(message)
    data class ServerError(val code: Int, override val message: String) : FusionError(message)
    data class ClientError(val code: Int, override val message: String) : FusionError(message)
    data class NoSurveysAvailable(override val message: String = "No surveys available") : FusionError(message)
    data class UnknownError(override val message: String) : FusionError(message)
}

internal fun mapExceptionToFusionError(e: Exception): FusionError {
    return when (e) {
        is IOException -> FusionError.NetworkError(e.message ?: "Network error occurred")
        is HttpException -> {
            val errorBody = e.response()?.errorBody()?.string()
            val defaultMessage = e.message() ?: "HTTP error occurred"
            val message = if (!errorBody.isNullOrBlank()) "$defaultMessage: $errorBody" else defaultMessage

            when (e.code()) {
                401, 403 -> FusionError.AuthenticationError(message)
                in 400..499 -> FusionError.ClientError(e.code(), message)
                in 500..599 -> FusionError.ServerError(e.code(), message)
                else -> FusionError.UnknownError("Unhandled HTTP error (${e.code()}): $message")
            }
        }
        else -> FusionError.UnknownError(e.message ?: "An unknown error occurred")
    }
}

