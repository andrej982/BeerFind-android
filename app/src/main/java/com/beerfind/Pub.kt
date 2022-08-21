package com.beerfind

import android.content.Context
import androidx.core.text.HtmlCompat
import org.osmdroid.util.GeoPoint
import kotlin.math.roundToInt

data class Pub constructor(
    val name: String,
    val address: String,
    val distance: Float,
    val geoPoint: GeoPoint,
    val context: Context
    ) {

    private fun getDistance(distance: Float): String {
        return if (distance > 1000) {
            "${context.getString(R.string.distance)} ${(distance / 1000).roundToInt()} km"
        } else {
            "${context.getString(R.string.distance)} ${(distance / 10).roundToInt() * 10} m"
        }
    }

    override fun toString(): String {
        return "$address\n\n${getDistance(distance)}"
    }
}
