package com.change.demox.camera.cameraandimageview

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.change.demox.application.MyApplication
import com.change.demox.utils.FileUtils
import java.io.FileNotFoundException

class CameraAndShowImageViewModel() : ViewModel() {


    //照片数据源LiveData
    var photo = MutableLiveData<Bitmap>()
    //image 所显示的file 的uri
    lateinit var showImageFileUri: Uri

    //更新photo
    fun updatePhoto(bitmap: Bitmap) {
        photo.value = bitmap
    }

    /**
     * 从相册选完图之后的操作，显示位图，保存FileUri
     */
    fun afterChoosePictureFromAlbum(context: Context, fileUri: Uri) {
        var bitmap: Bitmap? = null
        try {
            //Uri转化为Bitmap
            bitmap = FileUtils.getBitmap(context, fileUri)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        showImageFileUri = fileUri
        bitmap.let { updatePhoto(it!!) }
    }

    /**
     * 分享图片(根据file uri)
     */
    fun shareImageWithFileUri() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, showImageFileUri)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        shareIntent
//            .putExtra(
//                Intent.EXTRA_TEXT,
//                "**my message with URL **"
//            )
        MyApplication.instance?.context?.startActivity(shareIntent)
    }

}