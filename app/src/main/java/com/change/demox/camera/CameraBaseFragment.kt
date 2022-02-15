package com.change.demox.camera

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.change.demox.camera.part.Media
import com.google.android.material.snackbar.Snackbar
import java.io.File


abstract class CameraBaseFragment<B : ViewBinding>() : Fragment() {
    abstract val binding: B

    protected val outputDirectory: String by lazy {
        //沙盒外部私有存储的【DCIM】-> [demox_camera]是存放照片的路径
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            "${Environment.DIRECTORY_DCIM}/demox_camera/"
        } else {
            "${requireContext().getExternalFilesDir(Environment.DIRECTORY_DCIM)?.path}/demox_camera/"
        }
    }

    // 应用程序camera正常运行所需的权限
    private val permissions = mutableListOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
    )

    private val permissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permission ->
            if (permission.all { it.value }) {
                onPermissionGranted()
            } else {
                if (permission.containsKey("android.permission.CAMERA")) {
                    if (permission["android.permission.CAMERA"] == false) {
                        view?.let { v ->
                            Snackbar.make(
                                v,
                                "app wants access to the camera",
                                Snackbar.LENGTH_INDEFINITE
                            )
                                .setAction("OK") { getAppDetailSettingIntent(context) }
                                .show()
                        }
                    }
                } else if (permission.containsKey("android.permission.READ_EXTERNAL_STORAGE")) {
                    if (permission["android.permission.READ_EXTERNAL_STORAGE"] == false) {
                        view?.let { v ->
                            Snackbar.make(
                                v,
                                "app wants access to the album",
                                Snackbar.LENGTH_INDEFINITE
                            )
                                .setAction("OK") { getAppDetailSettingIntent(context) }
                                .show()
                        }
                    }
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Adding an option to handle the back press in fragment
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (allPermissionsGranted()) {
            onPermissionGranted()
        } else {
            permissionRequest.launch(permissions.toTypedArray())
        }
    }

    protected fun getMedia(): List<Media> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        getMediaQPlus()
    } else {
        getMediaQMinus()
    }.reversed()


    /**  !!!!!公共目录（非私有存储），才能用这个写法
     * 获取手机中的所有媒体文件，并过滤获取某一目录下的媒体文件（AndroidQ 以上版本）
     *
     * 这套方法是针对公共存储的，不适用于私有存储 /storage/emulated/0/Pictures/XXXX的
     */
    private fun getMediaQPlus(): List<Media> {
        val items = mutableListOf<Media>()
        val contentResolver = requireContext().applicationContext.contentResolver

        //使用MediaStore查询外部存储里的Video文件, EXTERNAL_CONTENT_URI代表外部存储器，该值不变
        /**
         * MediaStore.Video.Media.RELATIVE_PATH
         *
         * 相对路径
         *
         *
         * MediaStore.Files.FileColumns.DATA 文件路径
         *
         *   int pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
         *    String path = cursor.getString(pathColumn)  //获取文件的路径，如/storage/emulated/0/Pictures/y_camera/1.image.png
         */
        contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.RELATIVE_PATH,   //RELATIVE_PATH是相对路径不是绝对路径
                MediaStore.Video.Media.DATE_TAKEN,
            ),
            null,
            null,
            "${MediaStore.Video.Media.DISPLAY_NAME} ASC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RELATIVE_PATH)
            val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_TAKEN)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val path = cursor.getString(pathColumn)
                val date = cursor.getLong(dateColumn)
                val contentUri: Uri =
                    ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                if (path == outputDirectory) {
                    items.add(Media(contentUri.toString(), true, date))
                }
            }
        }

        //使用MediaStore查询外部存储里的Images文件, EXTERNAL_CONTENT_URI代表外部存储器，该值不变
        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.RELATIVE_PATH,
                MediaStore.Images.Media.DATE_TAKEN,
            ),
            null,
            null,
            "${MediaStore.Images.Media.DISPLAY_NAME} ASC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.RELATIVE_PATH)
            val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val path = cursor.getString(pathColumn)
                val date = cursor.getLong(dateColumn)
                //这个方法负责把id和contentUri连接成一个新的Uri，用于为路径加上ID部分
                val contentUri: Uri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                if (path == outputDirectory) {
                    //造入数据源供列表显示照片
                    items.add(Media(contentUri.toString(), false, date))
                }
            }
        }
        return items
    }

    /**
     * 获取手机中的所有媒体文件，并过滤获取某一目录下的媒体文件（AndroidQ 以下版本）
     */
    private fun getMediaQMinus(): List<Media> {
        val items = mutableListOf<Media>()
        //listFiles()是获取该目录下所有文件和目录的绝对路径
        File(outputDirectory).listFiles()?.forEach {

            //fixme: not use 的废写法：7.0之后的存储权限变更是用于【应用间共享文件】的，仅在此应用内获取资源路径并显示，用不上 uri 。留此代码为了跨应用时候留下写法
//            val authority = requireContext().applicationContext.packageName + ".provider"
//            //把一个文件File，转换为URI
//            val mediaUri = FileProvider.getUriForFile(requireContext(), authority, it)

            //传绝对路径即可,造入数据源供列表显示照片
            items.add(Media(it.absolutePath, it.extension == "mp4", it.lastModified()))
        }
        items.sortBy { item -> item.date }
        return items
    }

    /**
     * 跳转到权限管理页面
     */
    private fun getAppDetailSettingIntent(context: Context?) {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= 9) {
            intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            intent.data = Uri.fromParts("package", context?.packageName, null)
        }
        startActivity(intent)
    }

    /**
     * Check for the permissions
     */
    protected fun allPermissionsGranted() = permissions.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }


    /**
     * A function which will be called after the permission check
     * */
    open fun onPermissionGranted() = Unit

    /**
     * An abstract function which will be called on the Back button press
     * */
    abstract fun onBackPressed()
}