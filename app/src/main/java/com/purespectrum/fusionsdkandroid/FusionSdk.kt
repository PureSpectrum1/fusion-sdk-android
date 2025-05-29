package com.purespectrum.fusionsdkandroid

import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.purespectrum.fusionsdkandroid.api.ApiClient
import com.purespectrum.fusionsdkandroid.ui.FusionCardConfiguration
import com.purespectrum.fusionsdkandroid.ui.FusionSurveyAdapter
import com.purespectrum.fusionsdkandroid.ui.SurveyWebViewActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import retrofit2.HttpException

object FusionSdk {
    private var recyclerView: RecyclerView? = null
    private var surveyAdapter: FusionSurveyAdapter? = null

    fun showSurveyCards(
        context: Context,
        targetView: ViewGroup,
        config: FusionCardConfiguration,
        baseUrl: String = "https://fusionapi.spectrumsurveys.com/",
        accessToken: String,
        respondentId: String,
        locale: String,
        memberId: String? = null,
        hashedId: String? = null,
        profileData: Map<String, String> = emptyMap(),
        onError: ((FusionError) -> Unit)? = null
    ) {
        val apiService = ApiClient.create(baseUrl)

        if (targetView is RecyclerView) {
            recyclerView = targetView
        } else {
            targetView.removeAllViews()
            recyclerView = RecyclerView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, // Width match_parent
                    ViewGroup.LayoutParams.WRAP_CONTENT  // Height wrap_content for horizontal scroll
                )
            }
            targetView.addView(recyclerView)
        }

        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        if (surveyAdapter == null || recyclerView?.adapter == null) {
            surveyAdapter = FusionSurveyAdapter(context, config) { survey ->
                val intent = Intent(context, SurveyWebViewActivity::class.java).apply {
                    putExtra(SurveyWebViewActivity.EXTRA_URL, survey.entryLink)
                    putExtra(SurveyWebViewActivity.EXTRA_TOOLBAR_COLOR, config.webViewToolbarColor)
                    putExtra(SurveyWebViewActivity.EXTRA_TOOLBAR_TITLE_COLOR, config.webViewToolbarTitleColor)
                    config.webViewToolbarTitle?.let { putExtra(SurveyWebViewActivity.EXTRA_TOOLBAR_TITLE, it) }
                }
                if (context !is android.app.Activity) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
            recyclerView?.adapter = surveyAdapter
        } else {
            surveyAdapter?.updateConfig(config)

            if (recyclerView?.adapter == null) recyclerView?.adapter = surveyAdapter
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getData(
                    token = accessToken,
                    respondentId = respondentId,
                    respondentLocalization = locale,
                    surveyScoreValue = true,
                    starCount = true,
                    memberId = memberId,
                    hashedId = hashedId,
                    profileData = profileData
                )
                val surveys = response.body()?.surveys ?: emptyList()
                println(response)
                withContext(Dispatchers.Main) {
                    surveyAdapter?.submitList(surveys)
                    if (surveys.isEmpty()) {
                        println("FusionSDK: No surveys available.")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    surveyAdapter?.submitList(emptyList())
                    onError?.invoke(mapExceptionToFusionError(e))
                }
            }
        }
    }

    fun hideSurveyCards(targetView: ViewGroup) {
        if (recyclerView?.parent == targetView) {
            targetView.removeView(recyclerView)
        }
        recyclerView?.adapter = null
        surveyAdapter?.submitList(emptyList())
        println("FusionSDK: Surveys hidden.")
    }

    fun shutdown() {
        recyclerView = null
        surveyAdapter = null
        println("FusionSDK: Shutdown (cleared internal references).")
    }

    private fun mapExceptionToFusionError(e: Exception): FusionError {
        return when (e) {
            is IOException -> FusionError.NetworkError(e.message ?: "Network error")
            is HttpException -> {
                when (e.code()) {
                    in 400..499 -> {
                        if (e.code() == 401 || e.code() == 403) {
                            FusionError.AuthenticationError(e.message ?: "Authentication failed")
                        } else {
                            FusionError.ClientError(e.code(), e.message ?: "Client error")
                        }
                    }
                    in 500..599 -> FusionError.ServerError(e.code(), e.message ?: "Server error")
                    else -> FusionError.UnknownError(e.message ?: "Unknown error")
                }
            }
            else -> FusionError.UnknownError(e.message ?: "Unknown error")
        }
    }
}

