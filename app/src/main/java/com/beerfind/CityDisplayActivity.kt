package com.beerfind

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class CityDisplayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_display)

        // adds title and back button on the top
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = intent.getStringExtra("cityName")
    }
}
