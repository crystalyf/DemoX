package com.change.demox.camera.onlycamera

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R

/**
 *
 * Camera2 照相
 */
class CameraActivity : AppCompatActivity() {
    private val tag = "TakePhotoFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        initView()
    }

    private fun initView() {
        var fragmentResult = supportFragmentManager.findFragmentByTag(tag)
        if (fragmentResult == null) {
            fragmentResult = TakePhotoFragment()
            supportFragmentManager
                    .beginTransaction()
                    .add(
                            R.id.frame_container,
                            fragmentResult,
                            tag
                    )
                    .commit()
        }
    }
}