package com.beerfind

import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import kotlin.math.roundToInt


class ListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = this.arguments as Bundle
        val listView = requireView().findViewById(R.id.list_of_pubs) as ListView
        val pubs = ArrayList<String>()

        val point = Location("")
        point.latitude = bundle.getDouble("latitude")
        point.longitude = bundle.getDouble("longitude")

        for (pub in cluster.items) {
            val pubName = pub.subDescription.split(",")[0]
            val pubPosition = Location("")
            pubPosition.latitude = pub.position.latitude
            pubPosition.longitude = pub.position.longitude
            val distance = point.distanceTo(pubPosition)
            pubs.add("$pubName\n\n${getDistance(distance)}")
        }
        listView.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, pubs)
    }

    private fun getDistance(distance: Float): String {
        return if (distance > 1000) {
            "${getString(R.string.distance)} ${(distance / 1000).roundToInt()} km"
        } else {
            "${getString(R.string.distance)} ${(distance / 10).roundToInt() * 10} m"
        }
    }
}
