package com.purespectrum.fusionsdkandroid

import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.purespectrum.fusionsdkandroid.ui.FusionCardConfiguration

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cardContainer = findViewById<LinearLayout>(R.id.cardContainer)

        FusionSdk.showSurveyCards(
            context = this,
            parentLayout = cardContainer,
            config = FusionCardConfiguration.Builder()
                .accentColor(Color.parseColor("#81D4FA")) // Purple
                .textColor(Color.BLACK)                              // Time text
                .starColor(Color.parseColor("#FFD700"))   // Gold stars
                .inactiveStarColor(Color.LTGRAY)                    // Gray stars
                .backgroundColor(Color.WHITE)                       // Card background
                .build(),
            baseUrl = "https://url.com/",
            accessToken = "String",
            respondentId = "String",
            locale = "String"
        )
    }
}
