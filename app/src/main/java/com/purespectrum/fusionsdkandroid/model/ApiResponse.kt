package com.purespectrum.fusionsdkandroid.model

data class ApiResponse(
    val respondentId: String,
    val surveys: List<Surveys>
)

data class Surveys(
    val surveyId: String,
    val cpi: Double,
    val ir: Int,
    val estimatedLoi: Int,
    val fullOrPartialMatch: String,
    val entryLink: String
)