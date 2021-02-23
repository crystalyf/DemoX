package com.change.demox.views.dialog.dialogfullscreen

import android.os.Bundle
import com.change.base.BaseActivity
import com.change.demox.R
import kotlinx.android.synthetic.main.activity_dialog_fullscreen.*

/**
 *  全屏显示的Dialog
 *
 */
class DialogFullScreenActivity : BaseActivity() {


    private var dialog: NotificationWebViewDialog? = null
    private val url = "https://www.eiken.or.jp/eiken/apply/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_fullscreen)
        initView()
    }

    private fun initView() {
        btn_pop.setOnClickListener {
            showWebViewDialog(url)
        }
    }

    private fun showWebViewDialog(webUrl: String) {
        dialog = NotificationWebViewDialog(this, webUrl)
        dialog?.show()
    }
}