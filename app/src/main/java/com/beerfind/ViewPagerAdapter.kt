package com.beerfind

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import java.io.Serializable
import java.util.*

class ViewPagerAdapter(private val context: Context, private val listOfCities: List<City>) : PagerAdapter() {
    // on below line we are creating a method
    // as get count to return the size of the list.
    override fun getCount(): Int {
        return listOfCities.size
    }

    // on below line we are returning the object
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as RelativeLayout
    }

    // on below line we are initializing
    // our item and inflating our layout file
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // on below line we are initializing
        // our layout inflater.
        val mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // on below line we are inflating our custom
        // layout file which we have created.
        val itemView: View = mLayoutInflater.inflate(R.layout.image_slider_item, container, false)

        // on below line we are initializing
        // our image view with the id.
        val imageView: ImageView = itemView.findViewById<View>(R.id.idIVImage) as ImageView
        val textView: TextView = itemView.findViewById(R.id.cityName) as TextView

        // on below line we are setting
        // image resource for image view.
        val currentCity: City = listOfCities[position]
        imageView.setImageResource(currentCity.imageNumber)
        textView.setText(currentCity.imageCaption)

        // start city display activity on image click
        imageView.setOnClickListener(){
            val intent = Intent(context, CityDisplayActivity::class.java)
            intent.putExtra("cityName", currentCity.imageCaption)
                .putExtra("latitude", currentCity.latitude)
                .putExtra("longitude", currentCity.longitude)
                .putExtra("zoom", currentCity.zoom)
            if (currentCity.pubs != null)
                intent.putExtra("pubs", currentCity.pubs as Serializable)
            context.startActivity(intent)
        }

        // on the below line we are adding this
        // item view to the container.
        Objects.requireNonNull(container).addView(itemView)

        // on below line we are simply
        // returning our item view.
        return itemView
    }

    // on below line we are creating a destroy item method.
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // on below line we are removing view
        container.removeView(`object` as RelativeLayout)
    }
}
