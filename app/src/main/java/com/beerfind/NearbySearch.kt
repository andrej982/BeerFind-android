package com.beerfind

import com.google.maps.GeoApiContext
import com.google.maps.PlacesApi
import com.google.maps.model.LatLng
import com.google.maps.model.PlaceType
import com.google.maps.model.PlacesSearchResponse
import com.google.maps.model.RankBy
import org.osmdroid.util.GeoPoint


class NearbySearch {
    fun run(point: GeoPoint): PlacesSearchResponse {
        var request = PlacesSearchResponse()
        val context = GeoApiContext.Builder()
            .apiKey(BuildConfig.MAPS_API_KEY)
            .build()
        val location = LatLng(point.latitude, point.longitude)
        try {
            request = PlacesApi.nearbySearchQuery(context, location)
                .rankby(RankBy.DISTANCE)
                .language("en")
                .type(PlaceType.BAR)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return request
    }
}
