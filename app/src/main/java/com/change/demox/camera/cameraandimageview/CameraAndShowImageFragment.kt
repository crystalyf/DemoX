package com.change.demox.camera.cameraandimageview

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.change.demox.R
import com.change.demox.extension.getViewModelFactory
import com.change.demox.utils.FileUtils
import com.change.demox.utils.GlideUtils
import java.io.File

/**
 * Created by Fenrir-xingjunchao on 2022/1/21.
 *
 *  调用系统相机之后，显示照片在imageview上
 */
class CameraAndShowImageFragment : Fragment() {

    lateinit var imageView: ImageView
    lateinit var targetView: View
    private lateinit var viewModel: CameraAndShowImageViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            activity?.viewModels<CameraAndShowImageViewModel> { getViewModelFactory() }?.value!!
        targetView =
            View.inflate(
                container!!.context,
                R.layout.fragment_camera_and_show_image, null
            )
        initView()
        return targetView
    }

    private fun initView() {
        imageView = targetView.findViewById<ImageView>(R.id.img_photo)
        targetView.findViewById<Button>(R.id.btn_call_camera).setOnClickListener {
            callCameraCaptureRaw()
        }
        viewModel.photo.observe(viewLifecycleOwner, Observer {
            GlideUtils.loadBitmap(imageView, it)
        })
    }

    /**
     * 拍照之后，仅仅返回一个缩略图, 暂时no use
     */
    private fun callCameraCaptureSmall() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
       activity?.startActivityForResult(intent, CameraAndShowImageViewActivity.REQUEST_CODE_CAPTURE_SMALL)
    }

    /**
     * 拍照存到私有存储，并显示照片在imageView
     */
    private fun callCameraCaptureRaw() {
        //我们通过Intent会传递一个File的Uri给相机应用(跨应用传输了)。
        FileUtils.imageFile = FileUtils.createImageFile()
        FileUtils.imageFile?.let {
            // 启动系统相机
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val authority = requireContext().applicationContext.packageName + ".provider"
                val imgUri = FileProvider.getUriForFile(requireContext(), authority, it)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri)
            } else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(it))
            }
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
            intent.resolveActivity(activity?.packageManager!!)?.let {
               activity?.startActivityForResult(intent, CameraAndShowImageViewActivity.REQUEST_CODE_CAPTURE_RAW)
            }
        }
    }


}