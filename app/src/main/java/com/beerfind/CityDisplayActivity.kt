package com.beerfind

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import java.util.*


class CityDisplayActivity : AppCompatActivity() {

    private val permissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_display)

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
        cityMap.setTileSource(TileSourceFactory.MAPNIK)
        cityMap.controller.setZoom(intent.getDoubleExtra("zoom", 0.0));
        cityMap.setMultiTouchControls(true)
        cityMap.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        val mapController: IMapController = cityMap.controller
        val point = GeoPoint(intent.getDoubleExtra("latitude", 0.0), intent.getDoubleExtra("longitude", 0.0))
        mapController.setCenter(point)
    }

    private fun setLocalLanguage(lang_code: String){
        val locale = Locale(lang_code)
        Locale.setDefault(locale)

        val config = android.content.res.Configuration()
        config.locale = locale

        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        val intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.lang_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.lang_en -> {
            setLocalLanguage("en")
            true
        }

        R.id.lang_sk -> {
            setLocalLanguage("sk")
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}
