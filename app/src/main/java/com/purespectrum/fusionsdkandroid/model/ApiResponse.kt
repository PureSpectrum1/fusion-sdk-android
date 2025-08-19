package com.purespectrum.fusionsdkandroid.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponse(
    val respondentId: String,
    val surveys: List<Survey>
)
