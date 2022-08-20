package com.beerfind

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment


class ListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onHiddenChanged(hidden: Boolean) {

        if (!hidden) {
            val bundle = this.arguments as Bundle
            val listView = requireView().findViewById(R.id.list_of_pubs) as ListView
            val pubs = ArrayList<Pub>()

            val point = Location("")
            point.latitude = bundle.getDouble("latitude")
            point.longitude = bundle.getDouble("longitude")

            for (pub in cluster.items) {
                val pubName = pub.subDescription.substringBefore(",")
                val pubAddr = pub.subDescription.substringAfter(", ")
                val pubPosition = Location("")
                pubPosition.latitude = pub.position.latitude
                pubPosition.longitude = pub.position.longitude
                val distance = point.distanceTo(pubPosition)
                pubs.add(Pub(pubName, pubAddr, distance, pub.position, requireContext()))
            }
            pubs.sortBy { it.distance }
            listView.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, pubs)
            listView.isClickable = true
            listView.onItemClickListener = OnItemClickListener { _, _, position, _ ->
                (requireActivity() as CityDisplayActivity).bottomNav.selectedItemId = R.id.pub_map
                mapController.animateTo(pubs[position].geoPoint, 19.0, 1000)
            }
        }
        super.onHiddenChanged(hidden)
    }
}
