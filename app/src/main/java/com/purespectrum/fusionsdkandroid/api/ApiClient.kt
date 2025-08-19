package com.purespectrum.fusionsdkandroid.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.purespectrum.fusionsdkandroid.model.SurveyJsonAdapter
import com.purespectrum.fusionsdkandroid.model.CurrencyResponseJsonAdapter

object ApiClient {
    private val moshi = Moshi.Builder()
        // Add the generated adapters explicitly
        .add(KotlinJsonAdapterFactory())
        .build()

    fun create(baseUrl: String): ApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ApiService::class.java)
    }
}