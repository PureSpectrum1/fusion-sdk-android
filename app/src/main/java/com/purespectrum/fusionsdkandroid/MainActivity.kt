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
            baseUrl = "http://www.company.com:81/a/b/c.html", // Example url
            accessToken = "d7yXBaH9jVtyxS6iYQd3bVmYGpOvbIkWxuwjMqY2PGBObOVGqbm1GLsqHGuoVBw7", // Example token
            respondentId = "user", // Example id
            locale = "en_US" // Example locale
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        // FusionSdk.hideSurveyCards(cardContainer) // Not strictly needed if activity is destroyed
        FusionSdk.shutdown()
    }
}

