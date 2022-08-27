package com.beerfind

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beerfind.data.Pub
import com.beerfind.data.PubViewModel
import org.osmdroid.util.GeoPoint


class ListFragment : Fragment() {
    private lateinit var pubViewModel: PubViewModel
    private lateinit var toastMsg: Toast

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pubViewModel = ViewModelProvider(requireActivity()).get(PubViewModel::class.java)
        toastMsg = Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT)
        super.onViewCreated(view, savedInstanceState)
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
            val pubAdapter = PubListAdapter(pubs, requireContext())
            listView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = pubAdapter
            }
            pubViewModel.favouritePubsLiveData.observe(viewLifecycleOwner) { list ->
                for (pub in pubs) {
                    if (list.find{(it.name + it.address) == (pub.name + pub.address)} != null) {
                        pub.isFavourite = 1
                    }
                    else {
                        pub.isFavourite = 0
                    }
                }
                pubs.sortWith(compareByDescending<Pub>{it.isFavourite}.thenBy{it.distance})
                pubAdapter.setItems(pubs)
                pubAdapter.notifyDataSetChanged()
            }
            val swipeCallback = object: SwipeCallback() {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.absoluteAdapterPosition
                    if (direction == ItemTouchHelper.RIGHT) {
                        removePubFromDatabase(pubs[position])
                        listView.adapter?.notifyItemChanged(position)
                    }
                    if (direction == ItemTouchHelper.LEFT) {
                        insertPubToDatabase(pubs[position])
                        listView.adapter?.notifyItemChanged(position)
                    }
                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeCallback)
            itemTouchHelper.attachToRecyclerView(listView)
            listView.isClickable = true
        }
        super.onHiddenChanged(hidden)
    }

    fun insertPubToDatabase(pub: Pub) {
        pubViewModel.addPub(pub)
        toastMsg.cancel()
        toastMsg.setText("${getString(R.string.added_favourites)} ${pub.name}")
        toastMsg.show()
    }

    fun removePubFromDatabase(pub: Pub) {
        pubViewModel.deletePub(pub.name, pub.address)
        toastMsg.cancel()
        toastMsg.setText("${getString(R.string.removed_favourites)} ${pub.name}")
        toastMsg.show()
    }
}
