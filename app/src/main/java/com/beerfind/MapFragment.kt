package com.beerfind

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class MapFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var oldLocation: GeoPoint
    private lateinit var centerButton: ImageButton
    private val refreshDelay = 5000
    private val distanceWalked = 200
    private lateinit var handler: Handler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        centerButton = requireActivity().findViewById(R.id.centerButton)
        cityMap = requireView().findViewById(R.id.cityMap)
        cityMap.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        val bundle = this.arguments as Bundle
        cityMap.controller.setZoom(bundle.getDouble("zoom"))
        cityMap.setMultiTouchControls(true)
        cityMap.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        mapController = cityMap.controller

        if (bundle.getBoolean("isGps", false)) {
            myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), cityMap)
            myLocationOverlay!!.enableMyLocation()
            myLocationOverlay!!.runOnFirstFix {
                oldLocation = myLocationOverlay!!.myLocation
                requireActivity().runOnUiThread {
                    drawPubs(myLocationOverlay!!.myLocation)
                    cityMap.overlays.add(myLocationOverlay)
                    mapController.setCenter(myLocationOverlay!!.myLocation)
                    (requireActivity() as CityDisplayActivity).enableBar()
                    zoomOnCluster()
                }
            }
            centerButton.visibility = View.VISIBLE
            centerButton.setOnClickListener {
                oldLocation = myLocationOverlay!!.myLocation
                mapController.animateTo(myLocationOverlay!!.myLocation)
                myLocationOverlay!!.enableFollowLocation()
            }
            handler = Handler(Looper.getMainLooper())
            handler.postDelayed(object : Runnable {
                override fun run() {
                    refresh(myLocationOverlay!!.myLocation)
                    handler.postDelayed(this, refreshDelay.toLong())
                }
            }, refreshDelay.toLong())
        }
        else {
            val point = GeoPoint(bundle.getDouble("latitude"), bundle.getDouble("longitude"))
            drawPubs(point)
            mapController.setCenter(point)
//            zoomOnCluster()
        }
    }

    override fun onDestroy() {
        if (::handler.isInitialized)
            handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    private fun drawPubs(point: GeoPoint) {
        val icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_beer_pin, null)
        val iconHighlight = ResourcesCompat.getDrawable(resources, R.drawable.ic_beer_pin_focus, null)
        val clusterIcon = ResourcesCompat.getDrawable(resources, R.drawable.ic_beer, null) as Drawable
        val bundle = Bundle()

        // POI = Point Of Interest
        val placesSearchResults = NearbySearch().run(point).results
        cluster = RadiusMarkerClusterer(context)

        cluster.setIcon(clusterIcon.toBitmap())
        cluster.textPaint.textSize = 12 * resources.displayMetrics.density
        cluster.mAnchorV = Marker.ANCHOR_CENTER
        cluster.mTextAnchorU = 0.70f
        cluster.mTextAnchorV = 0.27f
        cityMap.overlays.clear()
        cityMap.overlays.add(cluster)

        for (poi in placesSearchResults) {
            val marker = Marker(cityMap)
            marker.position = GeoPoint(poi.geometry.location.lat, poi.geometry.location.lng)
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.icon = icon as Drawable
            marker.title = poi.name
            marker.subDescription = "${poi.vicinity}\n\n${poi.rating} (${poi.userRatingsTotal})"
            Log.d("debug andrej", poi.toString())
            marker.setOnMarkerClickListener { currentMarker, _ ->
                currentMarker.icon = iconHighlight
                selectedMarker = currentMarker
                mapController.animateTo(currentMarker.position)
                cityMap.invalidate()
                bundle.putString("name", currentMarker.title)
                bundle.putString("description", currentMarker.subDescription)
                showDialog(bundle)
            }
            cluster.add(marker)
        }
    }

    private fun zoomOnCluster() {
//        cityMap.controller.zoomToSpan(cluster.bounds.latitudeSpan, cluster.bounds.longitudeSpanWithDateLine)
        cityMap.zoomToBoundingBox(cluster.bounds, true)
    }

    private fun showDialog(bundle: Bundle): Boolean {
        val dialog = PubDetailWindow()
        val transaction = childFragmentManager.beginTransaction()
        dialog.arguments = bundle
        dialog.show(transaction, null)
        return true
    }

    private fun refresh(point: GeoPoint) {
        if (oldLocation.distanceToAsDouble(point) > distanceWalked) {
            oldLocation = point
            drawPubs(point)
            cityMap.overlays.add(myLocationOverlay)
        }
    }
}
