package com.purespectrum.fusionsdkandroid

import android.content.Context
import android.view.ViewGroup
import com.purespectrum.fusionsdkandroid.api.ApiClient
import com.purespectrum.fusionsdkandroid.ui.FusionCardConfiguration
import com.purespectrum.fusionsdkandroid.ui.createSurveyCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


object FusionSdk {
    fun showSurveyCards(
        context: Context,
        parentLayout: ViewGroup,
        config: FusionCardConfiguration,
        baseUrl: String = "https://fusionapi.spectrumsurveys.com/",
        accessToken: String,
        respondentId: String,
        locale: String,
    ) {
        val apiService = ApiClient.create(baseUrl)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getData(
                    token = accessToken,
                    respondentId = respondentId,
                    respondentLocalization = locale,
                    surveyScoreValue = true,
                    starCount = true,
                    memberId = null, // TODO
                    hashedId = null // TODO
                )
                val surveys = response.body()?.surveys ?: emptyList()

                withContext(Dispatchers.Main) {
                    surveys.forEach { survey ->
                        val card = createSurveyCard(context, survey, config)
                        parentLayout.addView(card)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
