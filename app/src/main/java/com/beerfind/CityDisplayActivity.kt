package com.beerfind

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import org.osmdroid.api.IMapController
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer
import org.osmdroid.config.Configuration
import org.osmdroid.views.MapView

lateinit var cluster: RadiusMarkerClusterer
lateinit var cityMap: MapView
lateinit var mapController: IMapController


class CityDisplayActivity : AppCompatActivity() {

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

        val bundle: Bundle = intent.extras!!
        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        loadFragment(MapFragment(), bundle)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.pub_map -> {
                    loadFragment(MapFragment(), bundle)
                    true
                }
                R.id.pub_list -> {
                    loadFragment(ListFragment(), bundle)
                    true
                }
                else -> {true}
            }
        }
        bottomNav.setOnItemReselectedListener{
            // empty to prevent re-selecting current fragment
        }
    }

    fun resetIcons(){
        val icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_beer_pin, null)
        for (marker in cluster.items) {
            marker.icon = icon as Drawable
        }
        cityMap.invalidate()
    }

    private  fun loadFragment(fragment: Fragment, bundle: Bundle?){
        val transaction = supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }
}
