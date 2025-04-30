package com.purespectrum.fusionsdkandroid.network

import com.purespectrum.fusionsdkandroid.model.ApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {
    @GET("surveys/fusionMatch")
    fun getData(
        @Header("access-token") token: String,
        @Query("respondentId") respondentId: String,
        @Query("respondentLocalization") respondentLocalization: String,
        @Query("surveyScoreValue") surveyScoreValue: Boolean,
        @Query("memberId") memberId: String?,
        @Query("hashed_id") hashedId: String?,
        @QueryMap(encoded = true) profileData: Map<String, String> = emptyMap()
    ): Call<ApiResponse>
}