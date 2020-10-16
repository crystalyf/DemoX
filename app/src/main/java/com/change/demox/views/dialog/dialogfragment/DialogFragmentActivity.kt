package com.change.demox.views.dialog.dialogfragment

import android.os.Bundle
import com.change.base.BaseActivity
import com.change.demox.R


class DialogFragmentActivity : BaseActivity() {

    private val tag = "dialogFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_fragment)
        initView()
    }

    private fun initView() {
        var fragmentResult = supportFragmentManager.findFragmentByTag(tag)
        if (fragmentResult == null) {
            fragmentResult = DialogFragmentFragment()
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