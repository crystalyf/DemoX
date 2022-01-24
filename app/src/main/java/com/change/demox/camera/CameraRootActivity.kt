package com.change.demox.camera

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R
import com.change.demox.camera.albumandcamera.CameraAndShowActivity
import com.change.demox.camera.cameraandimageview.CameraAndShowImageViewActivity
import com.change.demox.camera.onlycamera.CameraActivity
import com.change.demox.utils.PermissionUtils
import com.change.demox.utils.PermissionUtils.PERMISSION_REQUEST_CODE
import com.change.demox.utils.PermissionUtils.PERMISSION_SETTING_CODE
import kotlinx.android.synthetic.main.activity_camera_root.*
import kotlinx.android.synthetic.main.activity_edit_text.*

/**
 * Camera拍照根Activity：
 *
 */
class CameraRootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_root)
        initView()
    }

    private fun initView() {
        PermissionUtils.checkPermission(this, PermissionUtils.permissionsList, Runnable {
        })
        btn_only_camera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
        btn_camera_and_album.setOnClickListener {
            val intent = Intent(this, CameraAndShowActivity::class.java)
            startActivity(intent)
        }
        btn_camera_and_show_imageview.setOnClickListener {
            val intent = Intent(this, CameraAndShowImageViewActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * 第四步，请求权限的结果回调
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                var allGranted = true

                grantResults.forEach {
                    if (it != PackageManager.PERMISSION_GRANTED) {
                        allGranted = false
                    }
                }

                if (allGranted) {  //已获得全部权限
                    Log.i("tag","onRequestPermissionsResult 已获得全部权限")
                } else {
                    Log.i("tag","权限请求被拒绝了,不能继续依赖该权限的相关操作了，展示setting ")

                }
            }
        }
    }


    /**
     * 当从设置权限页面返回后，重新请求权限
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PERMISSION_SETTING_CODE -> { //第五步，当从设置权限页面返回后，重新请求权限
                PermissionUtils.checkPermission(this, PermissionUtils.permissionsList, Runnable {
                })
            }
        }
    }
}