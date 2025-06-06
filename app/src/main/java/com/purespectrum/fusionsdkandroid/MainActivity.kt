package com.purespectrum.fusionsdkandroid

import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.purespectrum.fusionsdkandroid.ui.FusionCardConfiguration

class MainActivity : AppCompatActivity() {

    private lateinit var cardContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cardContainer = findViewById(R.id.cardContainer)

        loadSurveys()
    }

    private fun loadSurveys() {
        val customCardConfig = FusionCardConfiguration.Builder()
            .accentColor(Color.parseColor("#00AEEF"))
            .textColor(Color.WHITE)
            .starColor(Color.parseColor("#FFC107"))
            .inactiveStarColor(Color.parseColor("#E0E0E0"))
            .cardBackgroundColor(Color.BLACK)
            .cardCornerRadiusDp(12f)
            .cardElevationDp(4f)
            .cpiAmountFontSizeSp(34f)
            .cpiCurrencyColor(Color.parseColor("#00AEEF"))
            .cpiCurrencyFontSizeSp(16f)
            .loiFontSizeSp(18f)
            .showRatingStars(true)
            .loiIconVisibility(true)
            .loiIconColor(Color.parseColor("#00AEEF"))
            .webViewToolbarColor(Color.parseColor("#007AFF"))
            .webViewToolbarTitleColor(Color.WHITE)
            .webViewToolbarTitle("Complete Survey")
            .build()

        FusionSdk.showSurveyCards(
            context = this,
            targetView = cardContainer,
            config = customCardConfig,
            accessToken = "token", // Example token
            respondentId = "id", // Example id
            locale = "locale" // Example locale
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        FusionSdk.shutdown()
    }
}

