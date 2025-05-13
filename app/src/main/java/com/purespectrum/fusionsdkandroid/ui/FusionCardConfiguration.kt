package com.purespectrum.fusionsdkandroid.ui

import android.graphics.Color

data class FusionCardConfiguration(
    val accentColor: Int = Color.parseColor("#81D4FA"),
    val textColor: Int = Color.BLACK,
    val starColor: Int = Color.parseColor("#FFD700"),
    val inactiveStarColor: Int = Color.LTGRAY,
    val backgroundColor: Int = Color.WHITE
) {
    class Builder {
        private var accentColor = Color.parseColor("#81D4FA")
        private var textColor = Color.BLACK
        private var starColor = Color.parseColor("#FFD700")
        private var inactiveStarColor = Color.LTGRAY
        private var backgroundColor = Color.WHITE

        fun accentColor(color: Int) = apply { this.accentColor = color }
        fun textColor(color: Int) = apply { this.textColor = color }
        fun starColor(color: Int) = apply { this.starColor = color }
        fun inactiveStarColor(color: Int) = apply { this.inactiveStarColor = color }
        fun backgroundColor(color: Int) = apply { this.backgroundColor = color }

        fun build() = FusionCardConfiguration(
            accentColor,
            textColor,
            starColor,
            inactiveStarColor,
            backgroundColor
        )
    }
}
