package com.beerfind

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beerfind.data.Pub
import org.osmdroid.util.GeoPoint


class ListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        lateinit var point: GeoPoint

        if (!hidden) {
            val bundle = this.arguments as Bundle
            val listView = requireView().findViewById(R.id.list_of_pubs) as RecyclerView
            val pubs = ArrayList<Pub>()
            point = if (bundle.getBoolean("isGps", false))
                myLocationOverlay?.myLocation!! else
                GeoPoint(bundle.getDouble("latitude"), bundle.getDouble("longitude"))
            for (pub in cluster.items) {
                val pubName = pub.subDescription.substringBefore(",")
                val pubAddr = pub.subDescription.substringAfter(", ")
                val distance = point.distanceToAsDouble(pub.position)
                pubs.add(Pub(name = pubName,
                    address = pubAddr,
                    distance = distance,
                    geoPoint = pub.position,
                    context = requireContext())
                )
            }
            pubs.sortBy { it.distance }
            listView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = PubListAdapter(pubs)
            }
            listView.isClickable = true
//            listView.addOnItemTouchListener({ _, _, position, _ ->
//                (requireActivity() as CityDisplayActivity).bottomNav.selectedItemId = R.id.pub_map
//                mapController.animateTo(pubs[position].geoPoint, 19.0, 1000)
//            })
        }
        super.onHiddenChanged(hidden)
    }
}
