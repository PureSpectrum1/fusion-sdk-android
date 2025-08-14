package com.purespectrum.fusionsdkandroid.api

import com.purespectrum.fusionsdkandroid.model.ApiResponse
import com.purespectrum.fusionsdkandroid.model.CurrencyResponse
import com.purespectrum.fusionsdkandroid.model.Survey
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {
    @GET("surveys/match")
    suspend fun getData(
        @Header("access-token") token: String,
        @Query("respondentId") respondentId: String,
        @Query("respondentLocalization") respondentLocalization: String,
        @Query("surveyScoreValue") surveyScoreValue: Boolean,
        @Query("starCount") starCount: Boolean,
        @Query("memberId") memberId: String?,
        @Query("hashed_id") hashedId: String?,
        @Query("isSdk") isSdk: Boolean,
        @QueryMap(encoded = true) profileData: Map<String, String> = emptyMap()
    ): retrofit2.Response<ApiResponse>

    @GET("sdkCurrency")
    suspend fun getCurrencyInfo(
        @Header("access-token") token: String
    ): retrofit2.Response<CurrencyResponse>
}
