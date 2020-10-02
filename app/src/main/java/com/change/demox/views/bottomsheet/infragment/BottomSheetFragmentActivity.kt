package com.change.demox.views.bottomsheet.infragment

import android.os.Bundle
import com.change.base.BaseActivity
import com.change.demox.R


class BottomSheetFragmentActivity : BaseActivity() {

    private val tag = "bottomSheetFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_sheet_fragment)
        initView()
    }

    private fun initView() {
        var fragmentResult = supportFragmentManager.findFragmentByTag(tag)
        if (fragmentResult == null) {
            fragmentResult = BottomSheetFragment()
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