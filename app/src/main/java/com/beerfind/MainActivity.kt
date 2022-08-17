package com.beerfind

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import java.util.*


class MainActivity : AppCompatActivity() {

    // on below line we are creating variable for view pager,
    // viewpager adapter and the image list.
    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var tabLayout: TabLayout
    private lateinit var listOfCities: MutableList<City>

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
        listOfCities.add(City(R.drawable.brno, "Brno", 49.194727, 16.609419, 16.5))
        listOfCities.add(City(R.drawable.bratislava, "Bratislava", 48.146128, 17.109559, 16.5))
        listOfCities.add(City(R.drawable.prague, "Praha", 50.084962, 14.421403, 16.0))
        listOfCities.add(City(R.drawable.kosice, "Košice", 48.720046, 21.258331, 16.5))
        listOfCities.add(City(R.drawable.lucenec, "Lučenec", 48.329432, 19.663638, 15.0))
        listOfCities.add(City(R.drawable.vidina, "Vidiná", 48.358428, 19.654713, 16.5))

        // on below line we are initializing our view
        // pager adapter and adding image list to it.
        viewPagerAdapter = ViewPagerAdapter(this@MainActivity, listOfCities)

        // on below line we are setting
        // adapter to our view pager.
        viewPager.adapter = viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager, true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.lang_menu, menu)
        return true
    }

    private fun setLocalLanguage(lang_code: String){
        val locale = Locale(lang_code)
        Locale.setDefault(locale)

        val config = Configuration()
        config.locale = locale

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
