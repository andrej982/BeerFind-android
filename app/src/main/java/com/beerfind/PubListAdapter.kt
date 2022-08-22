package com.beerfind

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.beerfind.data.Pub

class PubListAdapter(private val list: List<Pub>, private val context: Context)
    : RecyclerView.Adapter<PubListAdapter.PubViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PubViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PubViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: PubViewHolder, position: Int) {
        val pub: Pub = list[position]
        holder.bind(pub)
    }

    override fun getItemCount(): Int = list.size

    inner class PubViewHolder(inflater: LayoutInflater, parent: ViewGroup):
        RecyclerView.ViewHolder(inflater.inflate(R.layout.pub_list_item, parent, false)),
        View.OnClickListener {
        private val pubName: TextView = itemView.findViewById(R.id.pub_name)
        private val pubDesc: TextView = itemView.findViewById(R.id.pub_description)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(pub: Pub) {
            pubName.text = pub.name
            pubDesc.text = pub.toString()
        }

        override fun onClick(v: View?) {
            val position = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                (context as CityDisplayActivity).bottomNav.selectedItemId = R.id.pub_map
                mapController.animateTo(list[position].geoPoint, 19.0, 1000)
            }
        }
    }
}
