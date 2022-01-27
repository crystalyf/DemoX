package com.change.demox.views.googlemap.customdraw

import android.os.Bundle
import com.change.base.BaseActivity
import com.change.demox.R

/**
 * 用于遮挡地图的 Activity
 */
class CoverActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cover)
        initView()
    }

    private fun initView() {

    }
}