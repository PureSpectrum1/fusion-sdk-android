package com.purespectrum.fusionsdkandroid.ui

import android.graphics.Color

/**
 * Configuration for the visual style of survey cards and related UI elements.
 *
 * Use the [Builder] to construct an instance of this configuration.
 */
data class FusionCardConfiguration(
    // Card specific styles
    val cardBackgroundColor: Int,
    val cardCornerRadiusDp: Float,
    val cardElevationDp: Float,
    val cardPaddingHorizontalDp: Float,
    val cardPaddingVerticalDp: Float,

    // Text styles
    val cpiAmountColor: Int, // For the main reward amount text (e.g., "340")
    val cpiAmountFontSizeSp: Float,
    val cpiCurrencyColor: Int, // For the currency text (e.g., "Coins")
    val cpiCurrencyFontSizeSp: Float,
    val loiTextColor: Int, // For the LOI text (e.g., "2 Min")
    val loiFontSizeSp: Float,

    // Star rating styles
    val starColor: Int, // Color for filled stars
    val inactiveStarColor: Int, // Color for empty/inactive stars
    val showRatingStars: Boolean,

    // LOI Icon styles
    val loiIconVisibility: Boolean,
    val loiIconColor: Int, // Color for the LOI icon (e.g., clock)

    // WebView Activity Toolbar styles
    val webViewToolbarColor: Int,
    val webViewToolbarTitleColor: Int,
    val webViewToolbarTitle: String?, // Optional title for webview

    // Empty state styles
    val emptyStateMessage: String,
    val emptyStateMessageColor: Int,
    val emptyStateMessageFontSizeSp: Float,
    val showEmptyState: Boolean
) {
    class Builder {
        private var cardBackgroundColor: Int = Color.WHITE
        private var cardCornerRadiusDp: Float = 12.0f
        private var cardElevationDp: Float = 4.0f
        private var cardPaddingHorizontalDp: Float = 16.0f
        private var cardPaddingVerticalDp: Float = 16.0f

        private var cpiAmountColor: Int = Color.parseColor("#00AEEF") // Default light blue
        private var cpiAmountFontSizeSp: Float = 34.0f
        private var cpiCurrencyColor: Int = Color.parseColor("#00AEEF")
        private var cpiCurrencyFontSizeSp: Float = 16.0f
        private var loiTextColor: Int = Color.BLACK
        private var loiFontSizeSp: Float = 18.0f

        private var starColor: Int = Color.parseColor("#FFC107") // Default gold
        private var inactiveStarColor: Int = Color.parseColor("#E0E0E0") // Default light gray
        private var showRatingStars: Boolean = true

        private var loiIconVisibility: Boolean = true
        private var loiIconColor: Int = Color.BLACK // Default to loiTextColor

        private var webViewToolbarColor: Int = Color.parseColor("#007AFF") // Default blue
        private var webViewToolbarTitleColor: Int = Color.WHITE
        private var webViewToolbarTitle: String? = null // Default is app name or "Survey"

        // Empty state defaults
        private var emptyStateMessage: String = "No surveys available"
        private var emptyStateMessageColor: Int = Color.GRAY
        private var emptyStateMessageFontSizeSp: Float = 16f
        private var showEmptyState: Boolean = true

        fun cardBackgroundColor(color: Int) = apply { this.cardBackgroundColor = color }
        fun cardCornerRadiusDp(radius: Float) = apply { this.cardCornerRadiusDp = radius }
        fun cardElevationDp(elevation: Float) = apply { this.cardElevationDp = elevation }
        fun cardPaddingHorizontalDp(padding: Float) = apply { this.cardPaddingHorizontalDp = padding }
        fun cardPaddingVerticalDp(padding: Float) = apply { this.cardPaddingVerticalDp = padding }

        fun cpiAmountColor(color: Int) = apply { this.cpiAmountColor = color }
        fun cpiAmountFontSizeSp(size: Float) = apply { this.cpiAmountFontSizeSp = size }
        fun cpiCurrencyColor(color: Int) = apply { this.cpiCurrencyColor = color }
        fun cpiCurrencyFontSizeSp(size: Float) = apply { this.cpiCurrencyFontSizeSp = size }
        fun loiTextColor(color: Int) = apply { this.loiTextColor = color }
        fun loiFontSizeSp(size: Float) = apply { this.loiFontSizeSp = size }

        fun starColor(color: Int) = apply { this.starColor = color }
        fun inactiveStarColor(color: Int) = apply { this.inactiveStarColor = color }
        fun showRatingStars(show: Boolean) = apply { this.showRatingStars = show }

        fun loiIconVisibility(visible: Boolean) = apply { this.loiIconVisibility = visible }
        fun loiIconColor(color: Int) = apply { this.loiIconColor = color }

        fun webViewToolbarColor(color: Int) = apply { this.webViewToolbarColor = color }
        fun webViewToolbarTitleColor(color: Int) = apply { this.webViewToolbarTitleColor = color }
        fun webViewToolbarTitle(title: String?) = apply { this.webViewToolbarTitle = title }

        // Empty state builder methods
        fun emptyStateMessage(message: String) = apply { this.emptyStateMessage = message }
        fun emptyStateMessageColor(color: Int) = apply { this.emptyStateMessageColor = color }
        fun emptyStateMessageFontSizeSp(size: Float) = apply { this.emptyStateMessageFontSizeSp = size }
        fun showEmptyState(show: Boolean) = apply { this.showEmptyState = show }

        fun accentColor(color: Int) = apply {
            this.cpiAmountColor = color
            this.loiIconColor = color
        }

        fun textColor(color: Int) = apply {
            this.loiTextColor = color
        }


        fun build() = FusionCardConfiguration(
            cardBackgroundColor = cardBackgroundColor,
            cardCornerRadiusDp = cardCornerRadiusDp,
            cardElevationDp = cardElevationDp,
            cardPaddingHorizontalDp = cardPaddingHorizontalDp,
            cardPaddingVerticalDp = cardPaddingVerticalDp,
            cpiAmountColor = cpiAmountColor,
            cpiAmountFontSizeSp = cpiAmountFontSizeSp,
            cpiCurrencyColor = cpiCurrencyColor,
            cpiCurrencyFontSizeSp = cpiCurrencyFontSizeSp,
            loiTextColor = loiTextColor,
            loiFontSizeSp = loiFontSizeSp,
            starColor = starColor,
            inactiveStarColor = inactiveStarColor,
            showRatingStars = showRatingStars,
            loiIconVisibility = loiIconVisibility,
            loiIconColor = loiIconColor,
            webViewToolbarColor = webViewToolbarColor,
            webViewToolbarTitleColor = webViewToolbarTitleColor,
            webViewToolbarTitle = webViewToolbarTitle,
            emptyStateMessage = emptyStateMessage,
            emptyStateMessageColor = emptyStateMessageColor,
            emptyStateMessageFontSizeSp = emptyStateMessageFontSizeSp,
            showEmptyState = showEmptyState
        )
    }
}
