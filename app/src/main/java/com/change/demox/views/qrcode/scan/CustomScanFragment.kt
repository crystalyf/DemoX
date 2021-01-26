package com.change.demox.views.qrcode.scan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import com.change.demox.R
import com.change.demox.views.qrcode.scan.component.QrcodeCaptureManager
import kotlinx.android.synthetic.main.fragment_custom_scan.*


/**
zing 扫描二维码
 */

class CustomScanFragment : Fragment() {

    private var captureManager: QrcodeCaptureManager? = null
    private val mSavedInstanceState: Bundle? = null

    //is the Result of Selected Don't Ask Again
    private var isPermissionResult = false
    val REQUEST_CAMERA = 10001

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = View.inflate(container!!.context, R.layout.fragment_custom_scan, null)
        initView()
        return view
    }

    private fun initView() {

    }

    private fun initCamera() {
        captureManager = QrcodeCaptureManager(activity, dbv_custom)
        captureManager?.initializeFromIntent(activity?.intent, mSavedInstanceState)
        captureManager?.decode()
    }

    override fun onResume() {
        super.onResume()
        if (!checkPermissions()) {
            if (!isPermissionResult) {
                requestPermissions()
            }
        } else {
            if (captureManager == null) {
                initCamera()
            }
            captureManager!!.onResume()
        }
        isPermissionResult = false
    }

    override fun onPause() {
        super.onPause()
        if (captureManager != null) {
            captureManager!!.onPause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (captureManager != null) {
            captureManager!!.onDestroy()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initCamera()
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CAMERA)) {
                    requireActivity().finish()
                } else {
                    isPermissionResult = true
                    AlertDialog.Builder(requireActivity())
                            .setTitle("R.string.qr_search_get_permissions_title")
                            .setMessage("R.string.qr_search_get_permissions_message")
                            .setPositiveButton("R.string.qr_search_get_permissions_positive_button") { dialog, which ->
                                val intent = Intent()
                                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                val uri = Uri.fromParts("package", requireActivity().packageName, null)
                                intent.data = uri
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                requireActivity().finish()
                            }
                            .setNegativeButton("R.string.qr_search_get_permissions_negative_button") { dialog, which -> requireActivity().finish() }
                            .show()
                }
            }
        }
    }

    /**
     * Return the current state of the permissions needed.
     */
    private fun checkPermissions(): Boolean {
        var result = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            result = (PermissionChecker.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
                    == PermissionChecker.PERMISSION_GRANTED)
        }
        return result
    }

    private fun requestPermissions() {
        val shouldProvideRationale = shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
        if (shouldProvideRationale) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA)
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA)
        }
    }
}
