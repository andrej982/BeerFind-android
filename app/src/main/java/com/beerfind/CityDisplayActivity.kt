package com.beerfind

import android.Manifest
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import org.osmdroid.api.IMapController
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer
import org.osmdroid.bonuspack.location.NominatimPOIProvider
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class CityDisplayActivity : AppCompatActivity() {

    private val permissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_display)
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val ctx: Context = this.applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))

        val permissionList = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        // Initialize a new instance of ManagePermissions class and check permissions
        managePermissions = ManagePermissions(this,permissionList,permissionsRequestCode)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            managePermissions.checkPermissions()

        // adds title and back button on the top
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val cityName = intent.getStringExtra("cityName")
        supportActionBar!!.title = cityName

        val cityMap: MapView = findViewById(R.id.cityMap)
        cityMap.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        cityMap.controller.setZoom(intent.getDoubleExtra("zoom", 0.0))
        cityMap.setMultiTouchControls(true)
        cityMap.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        val mapController: IMapController = cityMap.controller
        val point = GeoPoint(intent.getDoubleExtra("latitude", 0.0), intent.getDoubleExtra("longitude", 0.0))
        mapController.setCenter(point)

        drawPubs(cityMap, point)
    }

    private fun drawPubs(cityMap: MapView, point: GeoPoint) {
        val icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_beer_pin, null)
        val clusterIcon = ResourcesCompat.getDrawable(resources, R.drawable.ic_beer, null) as Drawable

        // POI = Point Of Interest
        val poiProvider = NominatimPOIProvider("BeerFind_v0.1")
        val pois = poiProvider.getPOICloseTo(point, "pub", 50, 0.1)
        val cluster = RadiusMarkerClusterer(this)

        cluster.setIcon(clusterIcon.toBitmap())
        cluster.textPaint.textSize = 12 * resources.displayMetrics.density
        cluster.mAnchorV = Marker.ANCHOR_BOTTOM
        cluster.mTextAnchorU = 0.70f
        cluster.mTextAnchorV = 0.27f
        cityMap.overlays.add(cluster)

        if (pois != null) {
            for (poi in pois) {
                val marker = Marker(cityMap)
                marker.position = poi.mLocation
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                marker.icon = icon as Drawable
                marker.title = poi.mType
                marker.subDescription = poi.mDescription

                cluster.add(marker)
            }
        }
    }
}
