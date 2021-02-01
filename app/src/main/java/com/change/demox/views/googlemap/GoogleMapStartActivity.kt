package com.change.demox.views.googlemap

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R
import kotlinx.android.synthetic.main.activity_googlemap_start.*


/**
 * 从app内scheme启动谷歌地图APP，并且启动后显示传入的经纬度location
 *
 * 谷歌地图官方文档：https://developers.google.cn/maps/documentation/urls/get-started
 *
 */
class GoogleMapStartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_googlemap_start)
        initView()
    }

    private fun initView() {
        btn_googlemap_start.setOnClickListener {
            //打开谷歌地图
            startGoogleMap()
        }
    }

    /**
     * intent方式启动谷歌地图应用（通过url scheme唤起软件），并且显示指定传入的经纬度。
     *
     */
    private fun startGoogleMap() {
        val lng = "121.675399"
        val lat = "38.924674"
        if (isPackageExisted(this, "com.google.android.apps.maps")) {
            // val strUri = "https://www.google.com/maps/search/?api=1&query="+lat+","+lng"

            //这个URL格式能设置zoom
            val strUri = "https://www.google.com/maps/@?api=1&map_action=map&center=38.924674,121.675399&zoom=15"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
            startActivity(intent)
        } else {
            Toast.makeText(this, "手机上未安装谷歌地图", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * 判断手机上是否安装此Package Name的应用
     *
     * @param context
     * @param targetPackage
     * @return
     */
    private fun isPackageExisted(context: Context, targetPackage: String): Boolean {
        val pm: PackageManager = context.packageManager
        try {
            val info = pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA)
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
        return true
    }

}