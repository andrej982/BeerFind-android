package com.beerfind

import android.widget.Toast
import com.beerfind.data.Pub
import org.osmdroid.api.IMapController
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

lateinit var cluster: RadiusMarkerClusterer
lateinit var cityMap: MapView
lateinit var mapController: IMapController
lateinit var transitionToast: Toast
lateinit var selectedMarker: Marker
lateinit var pubs: ArrayList<Pub>
var myLocationOverlay: MyLocationNewOverlay? = null
