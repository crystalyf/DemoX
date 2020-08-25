/*
 * FileUtils.kt
 *
 * Created by jilingwen on 2019/12/16.
 * Copyright © 2019年 Eole. All rights reserved.
 */
package com.change.demox.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.change.demox.R
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.util.BitmapLoadUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max


object FileUtils {

    /**
     * グルプ画像のサイズ
     */
    private const val GROUP_ICON_SIZE = 300
    const val pickRequestCode = 99
    private const val iconFileName = "destinationIcon.png"
    private const val selectedFileName = "selectedImg.png"

    /**
     * 跳转到图像选择画面
     *
     * @param activity activity
     */
    fun pickFromGallery(activity: Activity) {
        if (ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    pickRequestCode
            )
        } else {
            val intent = Intent(Intent.ACTION_PICK, null)
                    .setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            activity.startActivityForResult(intent, pickRequestCode)
        }
    }

    /**
     * 在图像选择屏幕上，在onActivityResult期间执行的方法
     *
     * @param requestCode onActivityResultのrequestCode
     * @param resultCode　onActivityResultのresultCode
     * @param data　onActivityResultのdata
     * @param activity activity
     * @param success　成功 method
     * @param cropError　失敗 method
     */
    fun activityResult(
            requestCode: Int,
            resultCode: Int,
            data: Intent?,
            activity: Activity,
            success: (Uri?) -> Unit,
            cropError: (Throwable?) -> Unit
    ) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == pickRequestCode) {
                val selectedUri = data?.data
                if (selectedUri != null) {
                    // 开始裁剪
                    startCrop(selectedUri, activity)
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                data ?: return
                success(UCrop.getOutput(data))
            }
        } else {
        }
        if (requestCode == UCrop.RESULT_ERROR) {
            data?.let {
                cropError(UCrop.getError(data))
            } ?: cropError(null)
        }
    }

    /**
     * 画像調整
     *
     * @param uri
     * @param activity activity
     */
    private fun startCrop(uri: Uri, activity: Activity) {
        val destinationFileName = iconFileName
        val uCrop = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ||
                (getContentType(activity, uri) == "image/gif")
        ) {
            //动图处理流程
            val selectedBitmap: Bitmap? = getBitmap(activity, uri)
            val selectedImgFile = File(
                    activity.cacheDir,
                    selectedFileName
            )

            convertBitmapToFile(selectedImgFile, selectedBitmap)
            selectedBitmap?.recycle()
            //ps: UCrop.of(sourceUri：原图片URI地址, destinationUri：最后生成图片的URI地址)
            UCrop.of(
                    Uri.fromFile(selectedImgFile),
                    Uri.fromFile(File(activity.cacheDir, destinationFileName))
            )
        } else {
            //其他图的处理流程
            UCrop.of(
                    uri,
                    Uri.fromFile(File(activity.cacheDir, destinationFileName))
            )
        }

        val options = UCrop.Options()
        ////设置裁剪UI的页面信息
        options.setCircleDimmedLayer(true)
        options.setHideBottomControls(true)
        options.setShowCropGrid(false)
        options.setShowCropFrame(false)
        options.setToolbarTitle("图片调整")
        options.setToolbarCancelDrawable(R.drawable.ic_menu_back)
        options.setToolbarCropDrawable(R.drawable.complete)
        //设置裁剪出来图片的格式
        options.setCompressionFormat(Bitmap.CompressFormat.PNG)
        options.setRootViewBackgroundColor(activity.getColor(R.color.colorUcropLogo))
        options.setToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimary))
        options.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark))
        uCrop.withOptions(options)
        uCrop.withMaxResultSize(GROUP_ICON_SIZE, GROUP_ICON_SIZE)
        //动态的设置图片的宽高比，这里设置为1:1
        uCrop.withAspectRatio(1f, 1f)
        uCrop.start(activity)
    }

    /**
     * get bitmap of uri
     *
     * @param context
     * @param imageUri
     * @return
     */
    private fun getBitmap(context: Context, imageUri: Uri): Bitmap? {

        return try {
            val exifOrientation = BitmapLoadUtils.getExifOrientation(context, imageUri)
            val exifDegrees = BitmapLoadUtils.exifToDegrees(exifOrientation)
            val exifTranslation = BitmapLoadUtils.exifToTranslation(exifOrientation)

            val matrix = Matrix()
            if (exifDegrees != 0) {
                matrix.preRotate(exifDegrees.toFloat())
            }
            if (exifTranslation != 1) {
                matrix.postScale(exifTranslation.toFloat(), 1f)
            }
            var stream = context
                    .contentResolver
                    .openInputStream(imageUri)

            val opt = BitmapFactory.Options()
            opt.inJustDecodeBounds = true
            BitmapFactory.decodeStream(stream, null, opt)

            opt.inSampleSize = max(max(opt.outWidth, opt.outHeight) / 1000, 1)
            opt.inJustDecodeBounds = false

            stream?.close()
            stream = context
                    .contentResolver
                    .openInputStream(imageUri)

            val bitmap = BitmapFactory.decodeStream(stream, null, opt)

            val converted = Bitmap.createBitmap(
                    bitmap!!,
                    0,
                    0,
                    bitmap.width,
                    bitmap.height,
                    matrix,
                    true
            )
            stream?.close()
            if (!bitmap.sameAs(converted)) {
                bitmap.recycle()
                converted
            } else {
                bitmap
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 获取File的contentType
     *
     * @param context Context
     * @param uri URI
     * @return contentType
     */
    private fun getContentType(context: Context, uri: Uri?): String {
        if (uri == null) {
            return ""
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val resolver = context.contentResolver
            val cursor = resolver?.query(uri, null, null, null, null)
            cursor?.moveToFirst()
            val contentType = resolver?.getType(uri) ?: ""
            cursor?.close()
            contentType
        } else {
            val cursor = context.contentResolver?.query(uri, null, null, null, null);
            cursor?.moveToFirst()
            val contentType =
                    cursor?.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE))
                            ?: ""
            cursor?.close()
            contentType
        }
    }

    /**
     *  bitmap 转换成 file
     *
     * @param destinationFile
     * @param bitmap
     */
    private fun convertBitmapToFile(destinationFile: File, bitmap: Bitmap?) {
        //创建一个文件以写入位图数据
        destinationFile.createNewFile()
        //将位图转换为字节数组
        val bos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 50, bos)
        val bitmapData = bos.toByteArray()
        //将字节写入文件
        val fos = FileOutputStream(destinationFile)
        fos.write(bitmapData)
        fos.flush()
        fos.close()
    }

    /**
     *  画像調整のファイルを取る
     *
     * @param activity activity
     * @return
     */
    fun getIconCacheFile(activity: Context): File? {
        val file = File(activity.cacheDir, iconFileName)
        return if (file.exists()) {
            file
        } else {
            null
        }
    }

}