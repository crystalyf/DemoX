package com.change.demox.camera.cameraandimageview

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.change.demox.R
import com.change.demox.extension.getViewModelFactory
import com.change.demox.utils.GlideUtils

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
            callCamera()
        }
        viewModel.photo.observe(viewLifecycleOwner, Observer {
            GlideUtils.loadBitmap(imageView, it)
        })
    }

    //唤起camera
    private fun callCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
       activity?.startActivityForResult(intent, CameraAndShowImageViewActivity.REQUEST_CODE_CAPTURE_SMALL)
    }


}