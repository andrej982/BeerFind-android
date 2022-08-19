package com.beerfind

import android.content.Context
import kotlin.math.roundToInt

 data class Pub constructor(
    val name: String,
    val address: String,
    val distance: Float,
    val context: Context
    ) {

    override fun toString(): String {
        return "$name\n$address\n\n${getDistance(distance)}"
    }

    private fun getDistance(distance: Float): String {
        return if (distance > 1000) {
            "${context.getString(R.string.distance)} ${(distance / 1000).roundToInt()} km"
        } else {
            "${context.getString(R.string.distance)} ${(distance / 10).roundToInt() * 10} m"
        }
    }
}
