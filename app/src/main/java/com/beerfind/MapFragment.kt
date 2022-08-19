package com.beerfind

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer
import org.osmdroid.bonuspack.location.NominatimPOIProvider
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker


class MapFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cityMap = requireView().findViewById(R.id.cityMap)
        cityMap.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        val bundle = this.arguments as Bundle
        cityMap.controller.setZoom(bundle.getDouble("zoom"))
        cityMap.setMultiTouchControls(true)
        cityMap.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        mapController = cityMap.controller
        val point = GeoPoint(bundle.getDouble("latitude"), bundle.getDouble("longitude"))
        mapController.setCenter(point)

        drawPubs(cityMap, point)

        if (bundle.getBoolean("isGps", false)) {
            val myPin = Marker(cityMap)
            myPin.position = point
            myPin.icon = ResourcesCompat.getDrawable(resources, R.drawable.my_pin, null)
            myPin.title = getString(R.string.my_location)
            cityMap.overlays.add(myPin)
        }
    }

    private fun drawPubs(cityMap: MapView, point: GeoPoint) {
        val icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_beer_pin, null)
        val iconHighlight = ResourcesCompat.getDrawable(resources, R.drawable.ic_beer_pin_focus, null)
        val clusterIcon = ResourcesCompat.getDrawable(resources, R.drawable.ic_beer, null) as Drawable
        val bundle = Bundle()

        // POI = Point Of Interest
        val poiProvider = NominatimPOIProvider("BeerFind_v0.1")
        val pubPois = poiProvider.getPOICloseTo(point, "pub", 50, 0.005)
        cluster = RadiusMarkerClusterer(context)

        cluster.setIcon(clusterIcon.toBitmap())
        cluster.textPaint.textSize = 12 * resources.displayMetrics.density
        cluster.mAnchorV = Marker.ANCHOR_CENTER
        cluster.mTextAnchorU = 0.70f
        cluster.mTextAnchorV = 0.27f
        cityMap.overlays.add(cluster)

        for (poi in pubPois) {
            val marker = Marker(cityMap)
            marker.position = poi.mLocation
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.icon = icon as Drawable
            marker.title = poi.mType
            marker.subDescription = poi.mDescription
            marker.setOnMarkerClickListener { currentMarker, _ ->
                currentMarker.icon = iconHighlight
                mapController.animateTo(currentMarker.position)
                cityMap.invalidate()
                bundle.putString("description", currentMarker.subDescription)
                showDialog(bundle)
            }
            cluster.add(marker)
        }
    }

    private fun showDialog(bundle: Bundle): Boolean {
        val dialog = PubDetailWindow()
        val transaction = childFragmentManager.beginTransaction()
        dialog.arguments = bundle
        dialog.show(transaction, null)
        return true
    }
}
