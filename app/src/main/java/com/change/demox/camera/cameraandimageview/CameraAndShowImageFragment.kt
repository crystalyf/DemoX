package com.change.demox.camera.cameraandimageview

import android.content.Intent
import android.graphics.Bitmap
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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
import java.io.IOException
import java.lang.IllegalArgumentException

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
        targetView.findViewById<Button>(R.id.btn_call_target_album).setOnClickListener {
            openAlbumInTargetFolder()
        }
        targetView.findViewById<Button>(R.id.btn_get_location).setOnClickListener {
            getLocationFromPhoto()
        }
        targetView.findViewById<Button>(R.id.btn_share_image).setOnClickListener {
            viewModel.shareImageWithFileUri()
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

    /**
     * 打开相册（指定文件夹的位置）
     */
    private fun openAlbumInTargetFolder() {
        //图像和相册打开（最普通）
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        //指定位置行不通，都会打开【最近】文件夹

//        val intent = Intent(Intent.ACTION_VIEW)
//        intent.data =  File(FileUtils.getOutPutDirectory()).let { Uri.fromFile(it) }

//        val intent = Intent("android.intent.action.VIEW")
//        intent.addCategory("android.intent.category.DEFAULT")
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        intent.setDataAndType(FileUtils.imageFile.let { Uri.fromFile(it) }, "image/*")

//        var intent = Intent()
//        intent.data = Uri.parse(FileUtils.getOutPutDirectory())

//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        intent.type = "image/*"


//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//        intent.addCategory(Intent.CATEGORY_DEFAULT)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        intent.setDataAndType(Uri.parse(FileUtils.getOutPutDirectory()), "file/*")

        activity?.startActivityForResult(intent, CameraAndShowImageViewActivity.REQUEST_CODE_GO_TO_ALBUM)
    }


    /**
     * 获取指定照片经纬度信息
     */
    private fun getLocationFromPhoto() {
        var lat = 0.0f
        var lng = 0.0f
//        try {
//            val exifInterface = ExifInterface(FileUtils.imageFile?.absolutePath!!)
//            val datetime: String = exifInterface.getAttribute(ExifInterface.TAG_DATETIME) // 拍摄时间
//            val deviceName: String = exifInterface.getAttribute(ExifInterface.TAG_MAKE) // 设备品牌
//            val deviceModel: String = exifInterface.getAttribute(ExifInterface.TAG_MODEL) // 设备型号
//            val latValue: String = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
//            val lngValue: String = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
//            val latRef: String = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF)
//            val lngRef: String = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF)
//            if (latValue != null && latRef != null && lngValue != null && lngRef != null) {
//                lat = convertRationalLatLonToFloat(latValue, latRef)
//                lng = convertRationalLatLonToFloat(lngValue, lngRef)
//            }
//            Log.v("location", datetime+";"+lat.toString() + ";" + lng.toString())
//        } catch (e: IllegalArgumentException) {
//            e.printStackTrace()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
    }

    /**
     * 经纬度转换工具方法
     */
    private fun convertRationalLatLonToFloat(rationalString: String, ref: String): Float {
        val parts = rationalString.split(",".toRegex()).toTypedArray()
        var pair: Array<String>
        pair = parts[0].split("/".toRegex()).toTypedArray()
        val degrees = pair[0].trim { it <= ' ' }.toDouble() / pair[1].trim { it <= ' ' }
            .toDouble()
        pair = parts[1].split("/".toRegex()).toTypedArray()
        val minutes = pair[0].trim { it <= ' ' }.toDouble() / pair[1].trim { it <= ' ' }
            .toDouble()
        pair = parts[2].split("/".toRegex()).toTypedArray()
        val seconds = pair[0].trim { it <= ' ' }.toDouble() / pair[1].trim { it <= ' ' }
            .toDouble()
        val result = degrees + minutes / 60.0 + seconds / 3600.0
        return if (ref == "S" || ref == "W") {
            (-result).toFloat()
        } else result.toFloat()
    }


}