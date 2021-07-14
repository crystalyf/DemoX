package com.change.demox.camera

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R
import com.change.demox.camera.albumandcamera.CameraAndShowActivity
import com.change.demox.camera.onlycamera.CameraActivity
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
        btn_only_camera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
        btn_camera_and_album.setOnClickListener {
            val intent = Intent(this, CameraAndShowActivity::class.java)
            startActivity(intent)
        }
    }
}