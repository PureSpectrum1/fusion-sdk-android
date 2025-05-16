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
        profileData: Map<String, String> = emptyMap()
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
                    println("FusionSDK Error: Failed to fetch surveys - ${e.localizedMessage}")
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
}

