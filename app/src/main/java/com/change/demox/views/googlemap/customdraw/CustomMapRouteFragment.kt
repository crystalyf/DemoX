package com.change.demox.views.googlemap.customdraw

import android.content.ContentValues
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.change.demox.R
import com.change.demox.application.MyApplication
import com.change.demox.utils.FileUtils
import com.change.demox.utils.ViewUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_dialog.*
import java.util.*

/**
 * Created by Fenrir-xingjunchao on 2022/1/19.
 *
 *  自定义Google Map 轨迹
 *
 */
class CustomMapRouteFragment : Fragment(), GoogleMap.OnCameraMoveListener, OnMapReadyCallback {

    lateinit var targetView: View
    var googleMap: GoogleMap? = null
    private val targetLocation = LatLng(38.859116, 121.525676)
    private val ZOOM_LEVEL = 14f

    //折线组成的节点
    private val point1 = LatLng(38.867973, 121.521591)
    private val point1_1 = LatLng(38.865091, 121.515034)
    private val point2 = LatLng(38.867856, 121.517642)
    private val point3 = LatLng(38.861411, 121.518102)
    private val point4 = LatLng(38.858207, 121.528358)
    private val point5 = LatLng(38.855005, 121.539027)
    private val point6 = LatLng(38.854276, 121.532263)
    private val point7 = LatLng(38.848398, 121.529065)
    private val point8 = LatLng(38.849952, 121.518541)
    private val point9 = LatLng(38.847645, 121.517221)

    //折线
    private var polyLine1: Polyline? = null
    private var polyLine1_1: Polyline? = null
    private var polyLine1_2: Polyline? = null
    private var polyLine2: Polyline? = null
    private var polyLine3: Polyline? = null
    private var polyLine4: Polyline? = null
    private var polyLine5: Polyline? = null

    //折线形态
    private val PATTERN_DASH_LENGTH_PX = 30
    private val PATTERN_GAP_LENGTH_PX = 24
    private val dot = Dot()
    private val dash = Dash(PATTERN_DASH_LENGTH_PX.toFloat())
    private val gap = Gap(PATTERN_GAP_LENGTH_PX.toFloat())
    private val patternDotted = Arrays.asList(dot, gap)
    private val patternDashed = Arrays.asList(dash, gap)

    //折线宽
    private val STROKE_WIDTH_PX = 23

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        targetView = View.inflate(
            container?.context,
            R.layout.fragment_custom_map_route, null
        )
        val mapFragment: SupportMapFragment? =
            parentFragmentManager.findFragmentById(R.id.google_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        initView()
        return targetView
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        with(googleMap) {
            setOnCameraMoveListener(this@CustomMapRouteFragment)
            //第1条折线（实线）
            polyLine1 = addPolyline(PolylineOptions().apply {
                startCap(RoundCap())
                endCap(RoundCap())
                jointType(JointType.ROUND)
                add(point1, point2,point3)
                width(STROKE_WIDTH_PX.toFloat())
                color(resources.getColor(R.color.colorThemeOrange))
                geodesic(true)
            })
            polyLine1_1 = addPolyline(PolylineOptions().apply {
                startCap(RoundCap())
                endCap(RoundCap())
                jointType(JointType.ROUND)
                add(point3, point4)
                width(STROKE_WIDTH_PX.toFloat())
                color(resources.getColor(R.color.colorBlue))
                geodesic(true)
            })
            polyLine1_2 = addPolyline(PolylineOptions().apply {
                startCap(RoundCap())
                endCap(RoundCap())
                jointType(JointType.ROUND)
                add(point4, point5)
                width(STROKE_WIDTH_PX.toFloat())
                color(resources.getColor(R.color.colorGray))
                geodesic(true)
            })
            //第2条折线（圆点线）
            polyLine2 = addPolyline(PolylineOptions().apply {
                startCap(RoundCap())
                endCap(RoundCap())
                jointType(JointType.ROUND)
                add(point5, point6)
                width(30.toFloat())
                color(resources.getColor(R.color.colorGreen))
                pattern(patternDotted)
                geodesic(true)
            })
            //第3条折线(实线)
            polyLine3 = addPolyline(PolylineOptions().apply {
                startCap(RoundCap())
                endCap(RoundCap())
                jointType(JointType.ROUND)
                add(point6, point7)
                width(STROKE_WIDTH_PX.toFloat())
                color(resources.getColor(R.color.colorBlue))
                geodesic(true)
            })
            //第4条折线（虚线）
            polyLine4 = addPolyline(PolylineOptions().apply {
                //开始位置圆角
                startCap(RoundCap())
                endCap(RoundCap())
                add(point7, point8)
                width(STROKE_WIDTH_PX.toFloat())
                color(resources.getColor(R.color.colorRed))
                pattern(patternDashed)
                geodesic(true)
            })
            //第5条折线(实线)
            polyLine5 = addPolyline(PolylineOptions().apply {
                startCap(RoundCap())
                endCap(RoundCap())
                add(point8, point9)
                width(STROKE_WIDTH_PX.toFloat())
                color(resources.getColor(R.color.colorBlue))
                geodesic(true)
            })
            //移动到指定经纬度
            moveCamera(CameraUpdateFactory.newLatLngZoom(targetLocation, ZOOM_LEVEL))
            /**
             * 下面两个函数是测试附加功能的
             */
            //getScreenshot()
           // startActivity(Intent(this@CustomMapRouteFragment.activity, CoverActivity::class.java))
        }
    }

    private fun initView() {
        targetView.findViewById<View>(R.id.flb_left).setOnClickListener {
            Toast.makeText(activity, "左悬浮按钮点击", Toast.LENGTH_SHORT).show()
        }
        targetView.findViewById<View>(R.id.flb_right).setOnClickListener {
            Toast.makeText(activity, "右悬浮按钮点击", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCameraMove() {
        //当前缩放比例
        Log.v("zoomVale:" ,this.googleMap?.cameraPosition?.zoom.toString())
    }

    /**
     * 根据view生成图片
     */
    private fun getScreenshot() {
        //view转bitmap （inflate系列）
        val bm = ViewUtils.saveBitmapFromLayout2(targetView, targetView.width, targetView.height)
        //将位图保存到指定公共文件路径
        FileUtils.imageFile = FileUtils.createImageFile()
        FileUtils.convertBitmapToFile(FileUtils.imageFile!!, bm)
        //插入到媒体库
        val values = ContentValues()
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, FileUtils.imageFile?.name)
        values.put(MediaStore.Images.Media.DATA, FileUtils.imageFile?.absolutePath)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        MyApplication.instance?.context?.contentResolver?.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )
        //扫描，更新媒体库
        MediaScannerConnection.scanFile(activity,
            arrayOf(FileUtils.imageFile?.absolutePath),
            arrayOf("image/jpeg", "image/png"),
            object : MediaScannerConnection.OnScanCompletedListener {
                override fun onScanCompleted(path: String, uri: Uri) {
                    Log.v("ExternalStorage", "Scanned :$path")
                    Log.v("ExternalStorageUri", "-> uri=$uri")
                }
            })
    }



}