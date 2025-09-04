package com.purespectrum.fusionsdkandroid.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrencyResponse(
    val currencyName: String,
    val conversionValue: Int
)
