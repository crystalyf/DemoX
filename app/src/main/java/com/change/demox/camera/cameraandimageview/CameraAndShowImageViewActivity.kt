package com.change.demox.camera.cameraandimageview

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R
import com.change.demox.application.MyApplication
import com.change.demox.extension.getViewModelFactory
import com.change.demox.utils.FileUtils

/**
 *
 * 系统相机 照相+显示在imageview Activity
 */
class CameraAndShowImageViewActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE_CAPTURE_SMALL = 99
        const val REQUEST_CODE_CAPTURE_RAW = 98
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
                REQUEST_CODE_CAPTURE_RAW -> {
                    //直接Glide去压缩显示3MB的图太慢了，得提前压缩处理一下，先设置参数
                    val options: BitmapFactory.Options = BitmapFactory.Options()
                    val height = options.outHeight
                    val width = options.outWidth
                    var inSampleSize = 4 // 默认像素压缩比例，压缩为原图的1/2
                    val minLen = Math.min(height, width) // 原图的最小边长
                    if (minLen > 100) { // 如果原始图像的最小边长大于100dp（此处单位我认为是dp，而非px）
                        val ratio = minLen.toFloat() / 100.0f // 计算像素压缩比例
                        inSampleSize = ratio.toInt()
                    }
                    options.inJustDecodeBounds = false // 计算好压缩比例后，这次可以去加载原图了
                    options.inSampleSize = inSampleSize // 设置为刚才计算的压缩比例
                    val bitmap: Bitmap = BitmapFactory.decodeFile(FileUtils.imageFile?.absolutePath,options)
                    viewModel.updatePhoto(bitmap)
                }
            }
        }
    }
}