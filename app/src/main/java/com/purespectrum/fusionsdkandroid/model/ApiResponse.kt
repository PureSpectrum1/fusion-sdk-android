package com.purespectrum.fusionsdkandroid.model

data class ApiResponse<T>(
    val respondentId: String,
    val surveys: List<Survey>
)
