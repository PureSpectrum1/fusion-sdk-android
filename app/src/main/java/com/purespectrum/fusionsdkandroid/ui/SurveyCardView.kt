package com.purespectrum.fusionsdkandroid.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.purespectrum.fusionsdkandroid.R
import com.purespectrum.fusionsdkandroid.model.Survey
import android.content.Intent

fun createSurveyCard(
    context: Context,
    survey: Survey,
    config: FusionCardConfiguration
): View {
    val view = LayoutInflater.from(context).inflate(R.layout.item_survey_card, null, false)

    val coinsText = view.findViewById<TextView>(R.id.coinsText)
    val minutesText = view.findViewById<TextView>(R.id.minutesText)
    val starContainer = view.findViewById<LinearLayout>(R.id.starContainer)
    val clockIcon = view.findViewById<ImageView>(R.id.clockIcon)

    coinsText.text = survey.score.toInt().toString()
    minutesText.text = "${survey.estimatedLoi} Min"

    coinsText.setTextColor(config.accentColor)
    minutesText.setTextColor(config.textColor)
    clockIcon.setColorFilter(config.accentColor)
    view.setBackgroundColor(config.backgroundColor)

    starContainer.removeAllViews()
    for (i in 1..5) {
        val star = ImageView(context)
        star.layoutParams = LinearLayout.LayoutParams(40, 40).apply {
            setMargins(4, 0, 4, 0)
        }
        star.setImageResource(android.R.drawable.btn_star_big_on)
        star.setColorFilter(if (i <= survey.stars) config.starColor else config.inactiveStarColor)
        starContainer.addView(star)
    }

    view.setOnClickListener {
        val intent = Intent(context, SurveyWebViewActivity::class.java)
        intent.putExtra("url", survey.entryLink)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // required if using application context
        context.startActivity(intent)
    }

    return view
}
