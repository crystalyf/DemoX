package com.change.demox.views.flexboxlayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.change.demox.R


class FlexboxLayoutRootActivity : AppCompatActivity() {

    private val tag = "FlexboxLayoutRoot"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flexboxlayout_root)
        initView()
    }

    private fun initView() {
        var fragmentResult = supportFragmentManager.findFragmentByTag(tag)
        if (fragmentResult == null) {
            fragmentResult = MemberListByReadAtFragment()
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