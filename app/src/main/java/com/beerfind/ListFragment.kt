package com.beerfind

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView


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

        val listView = requireView().findViewById(R.id.list_of_pubs) as ListView
        val pubs = ArrayList<String>()
        for (pub in cluster.items){
            pubs.add(pub.subDescription)
        }
        listView.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, pubs)
    }

}
