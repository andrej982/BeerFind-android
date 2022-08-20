package com.beerfind

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer
import org.osmdroid.bonuspack.location.NominatimPOIProvider
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.Marker


class MapFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var oldLocation: Location
    private lateinit var centerButton: ImageButton
    private val poiProvider = NominatimPOIProvider("BeerFind_v0.1")
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
        val point = GeoPoint(bundle.getDouble("latitude"), bundle.getDouble("longitude"))
        mapController.setCenter(point)
        drawPubs(point)

        if (bundle.getBoolean("isGps", false)) {
            val myPin = Marker(cityMap)
            myPin.position = point
            oldLocation = Location("")
            oldLocation.latitude = myPin.position.latitude
            oldLocation.longitude = myPin.position.longitude
            myPin.icon = ResourcesCompat.getDrawable(resources, R.drawable.my_pin, null)
            myPin.title = getString(R.string.my_location)
            cityMap.overlays.add(myPin)
            centerButton.visibility = View.VISIBLE
            centerButton.setOnClickListener {
                mapController.animateTo(myPin.position)
            }
            handler = Handler()
            handler.postDelayed(object : Runnable {
                override fun run() {
                    refresh(myPin)
                    handler.postDelayed(this, refreshDelay.toLong())
                }
            }, refreshDelay.toLong())
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
        val pubPois = poiProvider.getPOICloseTo(point, "pub", 50, 0.005)
        cluster = RadiusMarkerClusterer(context)

        cluster.setIcon(clusterIcon.toBitmap())
        cluster.textPaint.textSize = 12 * resources.displayMetrics.density
        cluster.mAnchorV = Marker.ANCHOR_CENTER
        cluster.mTextAnchorU = 0.70f
        cluster.mTextAnchorV = 0.27f
        cityMap.overlays.clear()
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
                selectedMarker = currentMarker
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

    private fun refresh(marker: Marker) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

            override fun isCancellationRequested() = false
        }).addOnSuccessListener { location: Location? ->
            if (location == null) {
                Toast.makeText(requireContext(), getString(R.string.no_location), Toast.LENGTH_SHORT).show()
            }
            else {
                marker.position = GeoPoint(location.latitude, location.longitude)
                if (oldLocation.distanceTo(location) > distanceWalked) {
                    oldLocation = location
                    drawPubs(marker.position)
                }
            }
        }
    }
}
