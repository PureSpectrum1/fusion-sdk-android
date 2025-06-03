package com.purespectrum.fusionsdkandroid.ui

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.purespectrum.fusionsdkandroid.R
import com.purespectrum.fusionsdkandroid.model.Survey

class FusionSurveyAdapter(
    private val context: Context,
    private val config: FusionCardConfiguration,
    private val onItemClick: (Survey) -> Unit
) : ListAdapter<Survey, FusionSurveyAdapter.SurveyViewHolder>(SurveyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_survey_card, parent, false)
        return SurveyViewHolder(view, config, onItemClick)
    }

    override fun onBindViewHolder(holder: SurveyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SurveyViewHolder(
        itemView: View,
        private val config: FusionCardConfiguration,
        private val onItemClick: (Survey) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val cardViewRoot: CardView? = itemView.findViewById(R.id.fusion_card_view_root)
        private val tvCpiAmount: TextView = itemView.findViewById(R.id.coinsText)
        private val tvCpiCurrency: TextView = itemView.findViewById(R.id.currencyText)
        private val ivLoiIcon: ImageView = itemView.findViewById(R.id.clockIcon)
        private val tvLoi: TextView = itemView.findViewById(R.id.minutesText)
        private val ratingBar: RatingBar? = itemView.findViewById(R.id.survey_rating_bar)

        fun bind(survey: Survey) {
            tvCpiAmount.text = survey.score.toInt().toString()

            tvCpiCurrency.text = itemView.context.getString(R.string.fusion_sdk_cpi_currency_default)
            tvCpiCurrency.visibility = View.VISIBLE

            tvLoi.text = itemView.context.getString(R.string.fusion_sdk_loi_minutes_format, survey.estimatedLoi)

            cardViewRoot?.setCardBackgroundColor(config.cardBackgroundColor)
            cardViewRoot?.radius = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, config.cardCornerRadiusDp, itemView.context.resources.displayMetrics
            )
            cardViewRoot?.cardElevation = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, config.cardElevationDp, itemView.context.resources.displayMetrics
            )

            val cardContentLayout = itemView.findViewById<ViewGroup>(R.id.card_content_layout)
            cardContentLayout?.setPadding(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, config.cardPaddingHorizontalDp, itemView.context.resources.displayMetrics).toInt(),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, config.cardPaddingVerticalDp, itemView.context.resources.displayMetrics).toInt(),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, config.cardPaddingHorizontalDp, itemView.context.resources.displayMetrics).toInt(),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, config.cardPaddingVerticalDp, itemView.context.resources.displayMetrics).toInt()
            )

            tvCpiAmount.setTextColor(config.cpiAmountColor)
            tvCpiAmount.setTextSize(TypedValue.COMPLEX_UNIT_SP, config.cpiAmountFontSizeSp)

            tvCpiCurrency.setTextColor(config.cpiCurrencyColor)
            tvCpiCurrency.setTextSize(TypedValue.COMPLEX_UNIT_SP, config.cpiCurrencyFontSizeSp)

            tvLoi.setTextColor(config.loiTextColor)
            tvLoi.setTextSize(TypedValue.COMPLEX_UNIT_SP, config.loiFontSizeSp)

            if (config.loiIconVisibility) {
                ivLoiIcon.visibility = View.VISIBLE
                try {
                    ivLoiIcon.colorFilter = PorterDuffColorFilter(config.loiIconColor, PorterDuff.Mode.SRC_IN)
                } catch (e: Exception) {
                    Log.e("FusionSurveyAdapter", "Error applying color filter to LOI icon", e)
                }
            } else {
                ivLoiIcon.visibility = View.GONE
            }

            if (config.showRatingStars) {
                ratingBar?.visibility = View.VISIBLE
                ratingBar?.rating = survey.stars.toFloat()
                ratingBar?.progressTintList = android.content.res.ColorStateList.valueOf(config.starColor)
                ratingBar?.progressBackgroundTintList = android.content.res.ColorStateList.valueOf(config.inactiveStarColor)
                ratingBar?.secondaryProgressTintList = android.content.res.ColorStateList.valueOf(config.starColor)
            } else {
                ratingBar?.visibility = View.GONE
            }

            itemView.setOnClickListener {
                onItemClick(survey)
            }
        }
    }

    class SurveyDiffCallback : DiffUtil.ItemCallback<Survey>() {
        override fun areItemsTheSame(oldItem: Survey, newItem: Survey): Boolean {
            return oldItem.surveyId == newItem.surveyId
        }

        override fun areContentsTheSame(oldItem: Survey, newItem: Survey): Boolean {
            return oldItem == newItem
        }
    }
}
