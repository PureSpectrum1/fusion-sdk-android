package com.purespectrum.fusionsdkandroid

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.purespectrum.fusionsdkandroid.mapExceptionToFusionError
import com.purespectrum.fusionsdkandroid.model.Survey

@SuppressLint("StaticFieldLeak")
object FusionSdk {
    private var recyclerView: RecyclerView? = null
    private var surveyAdapter: FusionSurveyAdapter? = null
    private var emptyStateTextView: TextView? = null
    private const val TAG = "FusionSDK"
    private const val EMPTY_STATE_VIEW_TAG = "FusionEmptyStateViewTag"

    fun showSurveyCards(
        context: Context,
        targetView: ViewGroup,
        config: FusionCardConfiguration,
        accessToken: String,
        respondentId: String,
        locale: String,
        profileData: Map<String, String> = emptyMap(),
        memberId: String? = null,
        hashedId: String? = null,
        onError: ((FusionError) -> Unit)? = null,
        onResult: ((FusionResult) -> Unit)? = null,
        baseUrl: String = "https://fusionapi.spectrumsurveys.com/"
    ) {
        val apiService = ApiClient.create(baseUrl)
        val currencyService = ApiClient.create(baseUrl)

        if (recyclerView == null || recyclerView?.parent != targetView) {
            targetView.findViewWithTag<View>(EMPTY_STATE_VIEW_TAG)?.let { targetView.removeView(it) }
            recyclerView?.let { targetView.removeView(it) }

            recyclerView = RecyclerView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                visibility = View.GONE
            }
            targetView.addView(recyclerView)
            Log.d(TAG, "RecyclerView initialized and added to target view.")
        }

        emptyStateTextView = targetView.findViewWithTag(EMPTY_STATE_VIEW_TAG)
        if (emptyStateTextView == null && config.showEmptyState) {
            val inflater = LayoutInflater.from(context)
            emptyStateTextView = inflater.inflate(R.layout.empty_state_view, targetView, false) as? TextView
            emptyStateTextView?.tag = EMPTY_STATE_VIEW_TAG
            if (emptyStateTextView != null) {
                targetView.addView(emptyStateTextView)
                emptyStateTextView?.visibility = View.GONE
                Log.d(TAG, "Empty state TextView created and added.")
            } else {
                Log.e(TAG, "Failed to inflate or cast empty_state_view.xml to TextView.")
            }
        }
        emptyStateTextView?.visibility = View.GONE

        CoroutineScope(Dispatchers.IO).launch {
            var currencyName = context.getString(R.string.fusion_sdk_cpi_currency_default)

            try {
                Log.d(TAG, "Fetching currency information...")
                val currencyResponse = currencyService.getCurrencyInfo(token = accessToken)

                if (currencyResponse.isSuccessful && currencyResponse.body() != null) {
                    currencyName = currencyResponse.body()?.currencyName ?: "Points"
                    Log.d(TAG, "Successfully fetched currency name: $currencyName")
                } else {
                    Log.w(TAG, "Failed to fetch currency name, using default: $currencyName")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching currency name: ${e.message}", e)
            }

            withContext(Dispatchers.Main) {
                if (surveyAdapter == null || recyclerView?.adapter == null) {
                    surveyAdapter = FusionSurveyAdapter(
                        context = context,
                        config = config,
                        currencyName = currencyName,
                        onItemClick = { survey ->
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
                            Log.d(TAG, "Survey card clicked: ${survey.surveyId}, opening WebView.")
                        }
                    )
                    recyclerView?.adapter = surveyAdapter
                    Log.d(TAG, "Survey adapter created and set with currency: $currencyName")
                } else {
                    surveyAdapter?.updateCurrencyName(currencyName)
                    Log.d(TAG, "Updated existing adapter with currency: $currencyName")
                }

                fetchSurveys(apiService, accessToken, respondentId, locale, memberId, hashedId, profileData, config, onError, onResult)
            }
        }
    }

    private fun fetchSurveys(
        apiService: com.purespectrum.fusionsdkandroid.api.ApiService,
        accessToken: String,
        respondentId: String,
        locale: String,
        memberId: String?,
        hashedId: String?,
        profileData: Map<String, String>,
        config: FusionCardConfiguration,
        onError: ((FusionError) -> Unit)?,
        onResult: ((FusionResult) -> Unit)? = null
    ) {
        recyclerView?.visibility = View.GONE
        emptyStateTextView?.visibility = View.GONE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d(TAG, "Fetching surveys...")
                val response = apiService.getData(
                    token = accessToken,
                    respondentId = respondentId,
                    respondentLocalization = locale,
                    surveyScoreValue = true,
                    starCount = true,
                    memberId = memberId,
                    hashedId = hashedId,
                    isSdk = true,
                    profileData = profileData
                )

                if (response.isSuccessful) {
                    val surveys = response.body()?.surveys ?: emptyList()
                    Log.d(TAG, "Successfully fetched ${surveys.size} surveys.")
                    withContext(Dispatchers.Main) {
                        surveyAdapter?.submitList(surveys)
                        if (surveys.isEmpty()) {
                            onResult?.invoke(FusionResult.NoSurveysAvailable("No surveys available"))
                        } else {
                            onResult?.invoke(FusionResult.Success(surveys, surveys.size))
                            recyclerView?.visibility = View.VISIBLE
                            emptyStateTextView?.visibility = View.GONE
                        }
                    }
                } else {
                    val httpException = HttpException(response)
                    Log.e(TAG, "API call failed with code: ${response.code()}")
                    val fusionError = mapExceptionToFusionError(httpException)
                    withContext(Dispatchers.Main) {
                        surveyAdapter?.submitList(emptyList())
                        handleEmptyOrErrorState(config, fusionError, onError)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching surveys: ${e.message}", e)
                val fusionError = mapExceptionToFusionError(e)
                withContext(Dispatchers.Main) {
                    surveyAdapter?.submitList(emptyList())
                    handleEmptyOrErrorState(config, fusionError, onError)
                }
            }
        }
    }

    private fun handleEmptyOrErrorState(config: FusionCardConfiguration, error: FusionError, onError: ((FusionError) -> Unit)?) {
        recyclerView?.visibility = View.GONE
        if (config.showEmptyState && emptyStateTextView != null) {
            emptyStateTextView?.text = when (error) {
                is FusionError.NoSurveysAvailable -> config.emptyStateMessage
                else -> config.emptyStateMessage
            }
            emptyStateTextView?.setTextColor(config.emptyStateMessageColor)
            emptyStateTextView?.setTextSize(TypedValue.COMPLEX_UNIT_SP, config.emptyStateMessageFontSizeSp)
            emptyStateTextView?.visibility = View.VISIBLE
        } else {
            emptyStateTextView?.visibility = View.GONE
        }
        onError?.invoke(error)
    }

    fun hideSurveyCards(targetView: ViewGroup) {
        recyclerView?.visibility = View.GONE
        emptyStateTextView?.visibility = View.GONE
        surveyAdapter?.submitList(emptyList())
        Log.d(TAG, "Surveys hidden.")
    }

    fun shutdown() {
        recyclerView = null
        surveyAdapter = null
        emptyStateTextView = null
        Log.d(TAG, "FusionSDK shutdown complete.")
    }
}
