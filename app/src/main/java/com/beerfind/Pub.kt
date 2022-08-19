package com.beerfind

import android.content.Context
import android.text.Spanned
import androidx.core.text.HtmlCompat
import kotlin.math.roundToInt

data class Pub constructor(
    val name: String,
    val address: String,
    val distance: Float,
    val context: Context
    ) {

    private fun getDistance(distance: Float): String {
        return if (distance > 1000) {
            "${context.getString(R.string.distance)} ${(distance / 1000).roundToInt()} km"
        } else {
            "${context.getString(R.string.distance)} ${(distance / 10).roundToInt() * 10} m"
        }
    }

    fun getString(): Spanned{
        return HtmlCompat.fromHtml("<b>$name</b><br>$address<br><br>${getDistance(distance)}",
            HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}
