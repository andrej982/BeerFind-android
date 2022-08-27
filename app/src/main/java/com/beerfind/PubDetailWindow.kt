package com.beerfind

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment


class PubDetailWindow : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner)
        dialog!!.window?.attributes?.gravity = Gravity.BOTTOM
        dialog!!.window?.attributes?.windowAnimations = R.style.dialog_animation
        return inflater.inflate(R.layout.info_window, container, false)
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pubDesc: TextView = view.findViewById(R.id.pub_description)
        val pubName = this.arguments?.getString("name")
        val pubStr = this.arguments?.getString("description")
        pubDesc.text = HtmlCompat.fromHtml(
            "<b>$pubName</b><br>${pubStr?.substringBefore("\n")}<br>${pubStr?.substringAfter("\n")}",
            HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    override fun onDismiss(dialog: DialogInterface) {
        (activity as CityDisplayActivity).resetIcon()
        super.onDismiss(dialog)
    }
}
