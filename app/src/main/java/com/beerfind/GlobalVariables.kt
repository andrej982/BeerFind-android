package com.beerfind

import android.widget.Toast
import org.osmdroid.api.IMapController
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer
import org.osmdroid.views.MapView

lateinit var cluster: RadiusMarkerClusterer
lateinit var cityMap: MapView
lateinit var mapController: IMapController
lateinit var transitionToast: Toast
