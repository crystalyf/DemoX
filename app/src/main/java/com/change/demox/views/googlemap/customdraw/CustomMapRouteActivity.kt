package com.change.demox.views.googlemap.customdraw

import android.os.Bundle
import com.change.base.BaseActivity
import com.change.demox.R

/**
 * 自定义Google Map 轨迹 Activity
 */
class CustomMapRouteActivity : BaseActivity() {

    private val tag = "customMapRouteFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_map_route)
        initView()
    }

    private fun initView() {
        var fragmentResult = supportFragmentManager.findFragmentByTag(tag)
        if (fragmentResult == null) {
            fragmentResult = CustomMapRouteFragment()
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