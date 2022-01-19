package com.change.demox.views.googlemap.customdraw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.change.demox.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_custom_map_route.*
import kotlinx.android.synthetic.main.fragment_custom_map_route.view.*

/**
 * Created by Fenrir-xingjunchao on 2022/1/19.
 *
 *  自定义Google Map 轨迹
 *
 */
class CustomMapRouteFragment : Fragment(), OnMapReadyCallback {

    var googleMap: GoogleMap? = null
    private val targetLocation = LatLng(38.859116, 121.525676)
    private val ZOOM_LEVEL = 14f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = View.inflate(
            container?.context,
            R.layout.fragment_custom_map_route, null
        )
        val mapFragment : SupportMapFragment? = parentFragmentManager.findFragmentById(R.id.google_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        with(googleMap) {
            //移动到指定经纬度
            moveCamera(CameraUpdateFactory.newLatLngZoom(targetLocation, ZOOM_LEVEL))
        }
    }

}