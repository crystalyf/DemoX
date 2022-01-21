package com.change.demox.camera.cameraandimageview

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R
import com.change.demox.extension.getViewModelFactory

/**
 *
 * 系统相机 照相+显示在imageview Activity
 */
class CameraAndShowImageViewActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE_CAPTURE_SMALL = 99
    }

    private val tag = "CameraAndShowImageFragment"
    private lateinit var viewModel: CameraAndShowImageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_and_show_image_view)
        viewModel = viewModels<CameraAndShowImageViewModel> { getViewModelFactory() }.value
        initView()
    }

    private fun initView() {
        var fragmentResult = supportFragmentManager.findFragmentByTag(tag)
        if (fragmentResult == null) {
            fragmentResult = CameraAndShowImageFragment()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_CAPTURE_SMALL -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    viewModel.updatePhoto(bitmap)
                }
            }
        }
    }
}