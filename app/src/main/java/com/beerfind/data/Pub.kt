package com.beerfind.data

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.beerfind.R
import org.osmdroid.util.GeoPoint
import kotlin.math.roundToInt

@Entity(tableName = "pubs")
data class Pub(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String = "",
    @ColumnInfo(name = "address")
    val address: String = "",
    @Ignore
    val distance: Double,
    @Ignore
    val geoPoint: GeoPoint?,
    @Ignore
    val context: Context?,
    @Ignore
    var isFavourite: Int = 0,
    ) {
    constructor(id: Int, name: String, address: String): this(id, name, address, 0.0, null, null, 0)

    private fun getDistance(distance: Double): String {
        return if (distance > 1000) {
            "${context?.getString(R.string.distance)} ${(distance / 100).roundToInt() / 10.0} km"
        } else {
            "${context?.getString(R.string.distance)} ${(distance / 10).roundToInt() * 10} m"
        }
    }

    override fun toString(): String {
        return "$address\n\n${getDistance(distance)}"
    }
}
