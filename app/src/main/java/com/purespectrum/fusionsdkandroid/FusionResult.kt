package com.purespectrum.fusionsdkandroid

import com.purespectrum.fusionsdkandroid.model.Survey

sealed class FusionResult {
    data class Success(
        val surveys: List<Survey>,
        val totalCount: Int
    ) : FusionResult()

    data class NoSurveysAvailable(
        val message: String = "No surveys are currently available for your profile."
    ) : FusionResult()

    data class Error(
        val fusionError: FusionError
    ) : FusionResult()
}

fun FusionResult.isSuccessful(): Boolean {
    return this is FusionResult.Success || this is FusionResult.NoSurveysAvailable
}

fun FusionResult.isError(): Boolean {
    return this is FusionResult.Error
}

fun FusionResult.getSurveyCount(): Int {
    return when (this) {
        is FusionResult.Success -> surveys.size
        is FusionResult.NoSurveysAvailable -> 0
        is FusionResult.Error -> 0
    }
}

