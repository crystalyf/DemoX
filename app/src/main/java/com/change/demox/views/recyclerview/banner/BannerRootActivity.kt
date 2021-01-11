package com.change.demox.views.recyclerview.banner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R

/**
 * 从这个项目抽出来的此部分功能： https://github.com/bingoogolapple/BGABanner-Android
 *
 */

class BannerRootActivity : AppCompatActivity() {

    private val tag = "BannerHome"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner_root)
        initView()
    }

    private fun initView() {
        var fragmentResult = supportFragmentManager.findFragmentByTag(tag)
        if (fragmentResult == null) {
            fragmentResult = HomeFragment()
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