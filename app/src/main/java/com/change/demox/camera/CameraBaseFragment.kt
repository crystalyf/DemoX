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
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.change.demox.camera.part.Media
import com.google.android.material.snackbar.Snackbar
import java.io.File


abstract class CameraBaseFragment<B : ViewBinding>() : Fragment() {
    abstract val binding: B

    protected val outputDirectory: String by lazy {
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

    private val permissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
    { permission ->
        if (permission.all { it.value }) {
            onPermissionGranted()
        } else {
            if (permission.containsKey("android.permission.CAMERA")) {
                if (permission["android.permission.CAMERA"] == false) {
                    view?.let { v ->
                        Snackbar.make(v, "app wants access to the camera", Snackbar.LENGTH_INDEFINITE)
                                .setAction("OK") { getAppDetailSettingIntent(context) }
                                .show()
                    }
                }
            } else if (permission.containsKey("android.permission.READ_EXTERNAL_STORAGE")) {
                if (permission["android.permission.READ_EXTERNAL_STORAGE"] == false) {
                    view?.let { v ->
                        Snackbar.make(v, "app wants access to the album", Snackbar.LENGTH_INDEFINITE)
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

    private fun getMediaQPlus(): List<Media> {
        val items = mutableListOf<Media>()
        val contentResolver = requireContext().applicationContext.contentResolver

        contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                arrayOf(
                        MediaStore.Video.Media._ID,
                        MediaStore.Video.Media.RELATIVE_PATH,
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

                val contentUri: Uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)

                if (path == outputDirectory) {
                    items.add(Media(contentUri.toString(), true, date))
                }
            }
        }

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

                val contentUri: Uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                if (path == outputDirectory) {
                    items.add(Media(contentUri.toString(), false, date))
                }
            }
        }
        return items
    }

    private fun getMediaQMinus(): List<Media> {
        val items = mutableListOf<Media>()

        File(outputDirectory).listFiles()?.forEach {
            val authority = requireContext().applicationContext.packageName + ".provider"
            val mediaUri = FileProvider.getUriForFile(requireContext(), authority, it)
            items.add(Media(mediaUri.toString(), it.extension == "mp4", it.lastModified()))
        }
        items.sortBy { item -> item.date }
        return items
    }

    /**
     * 権限管理ページにジャンプします
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