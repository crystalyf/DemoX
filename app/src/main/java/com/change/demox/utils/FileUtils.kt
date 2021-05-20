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
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.change.demox.R
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.util.BitmapLoadUtils
import java.io.*
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

    /**
     *  通过文件Uri得到文件路径 （Uri -> FilePath）
     *
     *  文件uri是以此开头的映射地址 :content://
     *
     *  Android的Uri由以下三部分组成： "content://"、数据的路径、标示ID(可选)

    　　举些例子，如： 

    　　　　所有联系人的Uri： content://contacts/people

    　　　　某个联系人的Uri: content://contacts/people/5

    　　　　所有图片Uri: content://media/external

    　　　　某个图片的Uri：content://media/external/images/media/4
     */
    fun getFilePathFromUri(uri: Uri, context: Context): String? {
        val returnCursor = context.contentResolver.query(uri, null, null, null, null)
        val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val file = File(context.filesDir, name)
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            var read = 0
            val maxBufferSize = 1 * 1024 * 1024
            val bytesAvailable: Int = inputStream?.available()!!
            val bufferSize = Math.min(bytesAvailable, maxBufferSize)
            val buffers = ByteArray(bufferSize)
            while (inputStream.read(buffers).also { read = it } != -1) {
                outputStream.write(buffers, 0, read)
            }
            inputStream.close()
            outputStream.close()
        } catch (e: Exception) {

        }
        return file.path
    }

    /**
     * 文件是否大于4MB？
     */
    fun isFileUp4MB(length: Long): Boolean {
        return length >= 1048576 * 4
    }


    /**
     * 将图像压缩到目标尺寸以下 （ 以下4个function()是四合一使用 ）
     */
    fun compressBmpFileToTargetSize(file: File, targetSize: Long? = 1048576 * 3): File {
        if (file.length() > targetSize ?: 0) {
            val ratio = 2
            val options = BitmapFactory.Options()
            var bitmap = BitmapFactory.decodeFile(file.absolutePath, options)

            //判断图像读取的时候是否发生过旋转，如果有，那么转回来
            val rotate = readPictureDegree(file.absolutePath)
            if (rotate != ExifInterface.ORIENTATION_UNDEFINED) {
                bitmap = rotateBitmap(bitmap, rotate)
            }
            var targetWidth = options.outWidth / ratio
            var targetHeight = options.outHeight / ratio

            //将图像压缩到相应的大小
            val baos = ByteArrayOutputStream()
            val quality = 100
            var result: Bitmap? = generateScaledBmp(bitmap, targetWidth, targetHeight, baos, quality)

            var count = 0
            while (baos.size() > targetSize ?: 0 && count <= 10) {
                targetWidth /= ratio
                targetHeight /= ratio
                count++
                baos.reset()
                result = generateScaledBmp(result!!, targetWidth, targetHeight, baos, quality)
            }
            try {
                val fos = FileOutputStream(file)
                fos.write(baos.toByteArray())
                fos.flush()
                fos.close()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        return file
    }

    private fun generateScaledBmp(srcBmp: Bitmap, targetWidth: Int, targetHeight: Int, baos: ByteArrayOutputStream, quality: Int): Bitmap? {
        val result = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        val rect = Rect(0, 0, result.width, result.height)
        canvas.drawBitmap(srcBmp, null, rect, null)
        if (!srcBmp.isRecycled) {
            srcBmp.recycle()
        }
        result.compress(Bitmap.CompressFormat.JPEG, quality, baos)
        return result
    }

    /**
     * 读取照片的旋转角度
     */
    private fun readPictureDegree(path: String): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(path)
            val orientation: Int = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return degree
    }

    /**
     * 写真を回転させる
     */
    private fun rotateBitmap(bitmap: Bitmap, rotate: Int): Bitmap? {
        val w = bitmap.width
        val h = bitmap.height
        val matrix = Matrix()
        matrix.postRotate(rotate.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true)
    }

    /**
     *  判断该文件是否存在？ true：是false：否
     *
     *  strFile : 文件路径
     */
    fun fileIsExist(strFile: String?): Boolean {
        try {
            val f = File(strFile)
            if (!f.exists()) {
                return false
            }
        } catch (e: java.lang.Exception) {
            return false
        }
        return true
    }




}