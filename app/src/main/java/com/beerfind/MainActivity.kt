package com.beerfind

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager

class MainActivity : AppCompatActivity() {

    // on below line we are creating variable for view pager,
    // viewpager adapter and the image list.
    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var listOfImages: MutableList<Image>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initializing variables
        // of below line with their id.
        viewPager = findViewById(R.id.idViewPager)

        // on below line we are initializing
        // our image list and adding data to it.
        listOfImages = ArrayList()
        listOfImages.add(Image(R.drawable.brno, "Brno"))
        listOfImages.add(Image(R.drawable.bratislava, "Bratislava"))
        listOfImages.add(Image(R.drawable.prague, "Praha"))
        listOfImages.add(Image(R.drawable.kosice, "Košice"))
        listOfImages.add(Image(R.drawable.lucenec, "Lučenec"))
        listOfImages.add(Image(R.drawable.vidina, "Vidiná"))

        // on below line we are initializing our view
        // pager adapter and adding image list to it.
        viewPagerAdapter = ViewPagerAdapter(this@MainActivity, listOfImages)

        // on below line we are setting
        // adapter to our view pager.
        viewPager.adapter = viewPagerAdapter
    }
}
