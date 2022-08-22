package com.beerfind

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.beerfind.data.Pub

class PubListAdapter(private val list: List<Pub>)
    : RecyclerView.Adapter<PubViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PubViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PubViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: PubViewHolder, position: Int) {
        val pub: Pub = list[position]
        holder.bind(pub)
    }

    override fun getItemCount(): Int = list.size

}

class PubViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.pub_list_item, parent, false)) {

    private var pubNameView: TextView? = null
    private var pubDescView: TextView? = null


    init {
        pubNameView = itemView.findViewById(R.id.pub_name)
        pubDescView = itemView.findViewById(R.id.pub_description)
    }

    fun bind(pub: Pub) {
        pubNameView?.text = pub.name
        pubDescView?.text = pub.toString()
    }

}
