package com.beerfind

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class CityDisplayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_display)

        // adds back button on the top
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val name = intent.getStringExtra("cityName")
        supportActionBar!!.title = name
    }
}
