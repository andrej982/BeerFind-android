package com.beerfind

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    // on below line we are creating variable for view pager,
    // viewpager adapter and the image list.
    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var listOfCities: MutableList<City>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initializing variables
        // of below line with their id.
        viewPager = findViewById(R.id.idViewPager)

        // on below line we are initializing
        // our image list and adding data to it.
        listOfCities = ArrayList()
        listOfCities.add(City(R.drawable.brno, "Brno", 49.194727, 16.609419, 14.0))
        listOfCities.add(City(R.drawable.bratislava, "Bratislava", 48.146128, 17.109559, 14.0))
        listOfCities.add(City(R.drawable.prague, "Praha", 50.084962, 14.421403, 14.0))
        listOfCities.add(City(R.drawable.kosice, "Košice", 48.720046, 21.258331, 14.5))
        listOfCities.add(City(R.drawable.lucenec, "Lučenec", 48.329432, 19.663638, 15.0))
        listOfCities.add(City(R.drawable.vidina, "Vidiná", 48.358428, 19.654713, 16.5))

        // on below line we are initializing our view
        // pager adapter and adding image list to it.
        viewPagerAdapter = ViewPagerAdapter(this@MainActivity, listOfCities)

        // on below line we are setting
        // adapter to our view pager.
        viewPager.adapter = viewPagerAdapter
    }

    override fun attachBaseContext(newBase: Context) {

        val localeToSwitchTo = Locale("sk")
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase, localeToSwitchTo)
        super.attachBaseContext(localeUpdatedContext)
    }
}
