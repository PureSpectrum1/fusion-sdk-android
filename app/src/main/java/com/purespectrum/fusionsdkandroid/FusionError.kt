package com.purespectrum.fusionsdkandroid

sealed class FusionError(val message: String) {
    class NetworkError(message: String) : FusionError(message)
    class AuthenticationError(message: String) : FusionError(message)
    class ServerError(val code: Int, message: String) : FusionError(message)
    class ClientError(val code: Int, message: String) : FusionError(message)
    class NoSurveysAvailable(message: String = "No surveys available") : FusionError(message)
    class UnknownError(message: String) : FusionError(message)
}
