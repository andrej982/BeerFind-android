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
import org.osmdroid.config.Configuration


class CityDisplayActivity : AppCompatActivity() {

    lateinit var bottomNav: NavigationBarView

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

        val mapFragment = MapFragment()
        val listFragment = ListFragment()
        var active: Fragment = mapFragment
        // Initial fragment
        mapFragment.arguments = bundle
        listFragment.arguments = bundle
        val transaction = supportFragmentManager
        transaction.beginTransaction().add(R.id.container, listFragment, "list").hide(listFragment).commit()
        transaction.beginTransaction().add(R.id.container, mapFragment, "map").commit()

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.pub_map -> {
                    transaction.beginTransaction().hide(active).show(mapFragment).commit()
                    active = mapFragment
                    true
                }
                R.id.pub_list -> {
                    transaction.beginTransaction().hide(active).show(listFragment).commit()
                    active = listFragment
                    true
                }
                else -> {true}
            }
        }
        bottomNav.setOnItemReselectedListener{
            // empty to prevent re-selecting current fragment
        }
    }

    override fun onStart() {
        super.onStart()
        transitionToast.cancel()
    }

    fun resetIcon(){
        val icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_beer_pin, null)
        selectedMarker.icon = icon as Drawable
        cityMap.invalidate()
    }
}
