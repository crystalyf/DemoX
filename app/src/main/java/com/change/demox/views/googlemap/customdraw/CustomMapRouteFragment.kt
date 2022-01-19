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
import com.google.android.gms.maps.model.*
import java.util.*

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

    //折线组成的节点
    private val point1 = LatLng(38.867973, 121.521591)
    private val point2 = LatLng(38.867856, 121.517642)
    private val point3 = LatLng(38.861411, 121.518102)
    private val point4 = LatLng(38.858207, 121.528358)
    private val point5 = LatLng(38.855005, 121.539027)
    private val point6 = LatLng(38.854276, 121.532263)
    private val point7 = LatLng(38.848398, 121.529065)
    private val point8 = LatLng(38.849952, 121.518541)

    //折线
    private var polyLine1: Polyline? = null
    private var polyLine2: Polyline? = null
    private var polyLine3: Polyline? = null
    private var polyLine4: Polyline? = null

    //折线形态
    private val PATTERN_DASH_LENGTH_PX = 30
    private val PATTERN_GAP_LENGTH_PX = 24
    private val dot = Dot()
    private val dash = Dash(PATTERN_DASH_LENGTH_PX.toFloat())
    private val gap = Gap(PATTERN_GAP_LENGTH_PX.toFloat())
    private val patternDotted = Arrays.asList(dot, gap)
    private val patternDashed = Arrays.asList(dash, gap)

    //折线宽
    private val STROKE_WIDTH_PX = 13

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = View.inflate(
            container?.context,
            R.layout.fragment_custom_map_route, null
        )
        val mapFragment: SupportMapFragment? =
            parentFragmentManager.findFragmentById(R.id.google_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        with(googleMap) {

            //第1条折线（实现）
            polyLine1 = addPolyline(PolylineOptions().apply {
                add(point1, point2, point3, point4, point5)
                width(STROKE_WIDTH_PX.toFloat())
                color(resources.getColor(R.color.colorBlue))
                geodesic(true)
            })
            //第2条折线（圆点线）
            polyLine2 = addPolyline(PolylineOptions().apply {
                add(point5, point6)
                width(30.toFloat())
                color(resources.getColor(R.color.colorGreen))
                pattern(patternDotted)
                geodesic(true)
            })
            //第3条折线(实线)
            polyLine3 = addPolyline(PolylineOptions().apply {
                add(point6, point7)
                width(STROKE_WIDTH_PX.toFloat())
                color(resources.getColor(R.color.colorBlue))
                geodesic(true)
            })
            //第4条折线（虚线）
            polyLine4 = addPolyline(PolylineOptions().apply {
                add(point7, point8)
                width(STROKE_WIDTH_PX.toFloat())
                color(resources.getColor(R.color.colorRed))
                pattern(patternDashed)
                geodesic(true)
            })

            //移动到指定经纬度
            moveCamera(CameraUpdateFactory.newLatLngZoom(targetLocation, ZOOM_LEVEL))
        }
    }

}