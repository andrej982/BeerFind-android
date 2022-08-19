package com.beerfind

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
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

    private lateinit var cluster: RadiusMarkerClusterer
    private lateinit var cityMap: MapView
    private lateinit var mapController: IMapController
    private lateinit var bottomNav: NavigationBarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_display)
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val ctx: Context = this.applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))

        // adds title and back button on the top
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val cityName = intent.getStringExtra("cityName")
        supportActionBar!!.title = cityName

        cityMap = findViewById(R.id.cityMap)
        cityMap.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        cityMap.controller.setZoom(intent.getDoubleExtra("zoom", 0.0))
        cityMap.setMultiTouchControls(true)
        cityMap.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        mapController = cityMap.controller
        val point = GeoPoint(intent.getDoubleExtra("latitude", 0.0), intent.getDoubleExtra("longitude", 0.0))
        mapController.setCenter(point)

        drawPubs(cityMap, point)

        if (intent.hasExtra("isGps")) {
            val myPin = Marker(cityMap)
            myPin.position = point
            myPin.icon = ResourcesCompat.getDrawable(resources, R.drawable.my_pin, null)
            myPin.title = getString(R.string.my_location)
            cityMap.overlays.add(myPin)
        }

//        bottomNav = findViewById(R.id.bottomNav)
//        val listFragment = ListFragment()
//        loadFragment(MapFragment())
//        bottomNav.setOnItemReselectedListener {
//            when (it.itemId) {
//                R.id.pub_map -> {
//                    loadFragment(MapFragment())
//                    return@setOnItemReselectedListener
//                }
//                R.id.pub_list -> {
//                    loadFragment(ListFragment())
//                    return@setOnItemReselectedListener
//                }
//            }
//        }
    }

    private fun drawPubs(cityMap: MapView, point: GeoPoint) {
        val icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_beer_pin, null)
        val iconHighlight = ResourcesCompat.getDrawable(resources, R.drawable.ic_beer_pin_focus, null)
        val clusterIcon = ResourcesCompat.getDrawable(resources, R.drawable.ic_beer, null) as Drawable
        val bundle = Bundle()

        // POI = Point Of Interest
        val poiProvider = NominatimPOIProvider("BeerFind_v0.1")
        val pubPois = poiProvider.getPOICloseTo(point, "pub", 50, 0.005)
        cluster = RadiusMarkerClusterer(this)

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

    fun resetIcons(){
        val icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_beer_pin, null)
        for (marker in cluster.items) {
            marker.icon = icon as Drawable
        }
        cityMap.invalidate()
    }

    private fun showDialog(bundle: Bundle): Boolean {
        val dialog = PubDetailWindow()
        val transaction = supportFragmentManager.beginTransaction()
        dialog.arguments = bundle
        dialog.show(transaction, null)
        return true
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.cityMap,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
