package com.beerfind

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.material.tabs.TabLayout
import java.util.*


class MainActivity : AppCompatActivity() {

    // on below line we are creating variable for view pager,
    // viewpager adapter and the image list.
    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var tabLayout: TabLayout
    private lateinit var listOfCities: MutableList<City>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    // used for permission management
    private val permissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initializing variables
        // of below line with their id.
        viewPager = findViewById(R.id.idViewPager)
        tabLayout = findViewById(R.id.tab_layout)

        // on below line we are initializing
        // our image list and adding data to it.
        listOfCities = ArrayList()
        listOfCities.add(City(R.drawable.brno, "Brno", 49.194727, 16.609419, 17.0))
        listOfCities.add(City(R.drawable.bratislava, "Bratislava", 48.146128, 17.109559, 17.0))
        listOfCities.add(City(R.drawable.prague, "Praha", 50.084962, 14.421403, 17.0))
        listOfCities.add(City(R.drawable.kosice, "Košice", 48.720046, 21.258331, 17.0))
        listOfCities.add(City(R.drawable.lucenec, "Lučenec", 48.329432, 19.663638, 17.0))
        listOfCities.add(City(R.drawable.vidina, "Vidiná", 48.358428, 19.654713, 16.5))

        // on below line we are initializing our view
        // pager adapter and adding image list to it.
        viewPagerAdapter = ViewPagerAdapter(this@MainActivity, listOfCities)

        // on below line we are setting
        // adapter to our view pager.
        viewPager.adapter = viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager, true)

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val locationImage: ImageView = findViewById(R.id.location)
        locationImage.setOnClickListener {
            transitionToast = Toast.makeText(this, getString(R.string.loading), Toast.LENGTH_LONG)
            transitionToast.show()
            val intent = Intent(this, CityDisplayActivity::class.java)
            intent.putExtra("cityName", getString(R.string.my_location))
                .putExtra("zoom", 17.0)
                .putExtra("isGps", true)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.lang_menu, menu)
        return true
    }

    private fun setLocalLanguage(lang_code: String){
        val locale = Locale(lang_code)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
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
