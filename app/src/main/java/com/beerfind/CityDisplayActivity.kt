package com.beerfind

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.osmdroid.views.MapController
import org.osmdroid.views.MapView
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;

class CityDisplayActivity : AppCompatActivity() {

    private val permissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_display)

        val list = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        // Initialize a new instance of ManagePermissions class and check permissions
        managePermissions = ManagePermissions(this,list,permissionsRequestCode)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            managePermissions.checkPermissions()

        // adds title and back button on the top
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = intent.getStringExtra("cityName")

        val cityMap: MapView = findViewById(R.id.cityMap)
        cityMap.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        val mapController: MapController = cityMap.controller as MapController
        val point = GeoPoint(515000000.0, -150000.0)
        mapController.setCenter(point)
    }
}
