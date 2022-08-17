package com.beerfind

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment


class PubDetailWindow : DialogFragment() {

    private lateinit var dialogView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner);
        dialogView = inflater.inflate(R.layout.info_window, container, false)
        return inflater.inflate(R.layout.info_window, container, false)
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val description = this.arguments?.getString("description") as String
        Toast.makeText(context, description, Toast.LENGTH_SHORT).show()
//        val textView: TextView = dialog!!.findViewById(R.id.pub_description)
//        textView.text = description
    }
}
