package com.beerfind

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beerfind.data.Pub
import org.osmdroid.util.GeoPoint


class ListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                adapter = PubListAdapter(pubs, requireContext())
            }
            val swipeCallback = object: SwipeCallback() {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.absoluteAdapterPosition
                    if (direction == ItemTouchHelper.RIGHT) {
                        listView.adapter?.notifyItemChanged(position)
                        Toast.makeText(
                            requireContext(),
                            "${getString(R.string.removed_favourites)} ${pubs[position].name}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    if (direction == ItemTouchHelper.LEFT) {
                        listView.adapter?.notifyItemChanged(position)
                        Toast.makeText(
                            requireContext(),
                            "${getString(R.string.added_favourites)} ${pubs[position].name}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeCallback)
            itemTouchHelper.attachToRecyclerView(listView)
            listView.isClickable = true
        }
        super.onHiddenChanged(hidden)
    }
}
