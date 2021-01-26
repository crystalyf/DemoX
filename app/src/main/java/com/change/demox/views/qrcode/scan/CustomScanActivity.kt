package com.change.demox.views.qrcode.scan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R

/**
 * Zing 二维码功能
 *
 */

class CustomScanActivity : AppCompatActivity() {

    private val tag = "CustomScan"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_scan)
        initView()
    }

    private fun initView() {
        var fragmentResult = supportFragmentManager.findFragmentByTag(tag)
        if (fragmentResult == null) {
            fragmentResult = CustomScanFragment()
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
}