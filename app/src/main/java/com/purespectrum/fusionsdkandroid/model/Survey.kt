package com.purespectrum.fusionsdkandroid.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Survey(
    val surveyId: String,
    val cpi: Double,
    val score: Double,
    val ir: Int,
    val estimatedLoi: Int,
    val fullOrPartialMatch: String,
    val entryLink: String,
    val stars: Int
)