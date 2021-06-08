package com.change.demox.views.dialog.dialoground

import android.os.Bundle
import com.change.base.BaseActivity
import com.change.demox.R
import kotlinx.android.synthetic.main.activity_dialog_fullscreen.*

/**
 *  全屏显示的Dialog
 *
 */
class DialogRoundActivity : BaseActivity() {


    private var dialog: RoundDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_round)
        initView()
    }

    private fun initView() {
        btn_pop.setOnClickListener {
            showRoundDialog()
        }
    }

    private fun showRoundDialog() {
        dialog = RoundDialog(this)
        dialog?.show()
    }
}